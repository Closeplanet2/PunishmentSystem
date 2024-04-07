package com.pandapulsestudios.punishmentsystem.PlayerCommands;

import com.pandapulsestudios.pulsecommands.Enums.TabType;
import com.pandapulsestudios.pulsecommands.Interface.*;
import com.pandapulsestudios.pulsecommands.PlayerCommand;
import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulsecore.Chat.ChatAPI;
import com.pandapulsestudios.pulsecore.Chat.MessageType;
import com.pandapulsestudios.pulsecore.Java.PulseAutoRegister;
import com.pandapulsestudios.punishmentsystem.API.PunishmentConfigAPI;
import com.pandapulsestudios.punishmentsystem.API.PunishmentSystemAPI;
import com.pandapulsestudios.punishmentsystem.API.ServerReportAPI;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import com.pandapulsestudios.punishmentsystem.Enum.PunishmentType;
import com.pandapulsestudios.punishmentsystem.Enum.ServerMessage;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@PulseAutoRegister
public class PunishmentCommand extends PlayerCommand{
    private static String BuildReason(String[] reason, char middle){
        var builder = new StringBuilder();
        for(var x : reason) builder.append(String.format("%s%c", x, middle));
        var message = builder.toString();
        return message.substring(0, message.length() - 1);
    }

    public PunishmentCommand() { super("punishment", false); }

    @Override
    public void NoMethodFound(CommandSender commandSender, String s, String[] strings) {
        PunishmentSystem.MessageWindows.DisplayHelpMenu((Player) commandSender);
    }

    @PCMethod
    @PCSignature({"configs", "reload"})
    public void ReloadConfigs(CraftPlayer admin){
        if(!PunishmentSystem.PlayerPermissionConfig.CanReloadConfigs(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        PunishmentConfigAPI.ReloadConfigs();
        SendPlayerMessage(admin, ServerMessage.ReloadConfigs);
    }

    @PCMethod
    @PCSignature({"configs", "reset"})
    public void ResetConfigs(CraftPlayer admin){
        if(!PunishmentSystem.PlayerPermissionConfig.CanReloadConfigs(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        PunishmentConfigAPI.ResetConfigs();
        SendPlayerMessage(admin, ServerMessage.ResetConfigs);
    }

    @PCMethod
    @PCSignature({"send"})
    @PCAutoTab(pos = 1)
    @PCTab(pos = 2, type = TabType.Information_From_Function, data = "OfflinePlayerNames")
    @PCTab(pos = 3, type = TabType.Pure_Data, data = "[ENTER TEMP TIME]")
    @PCTab(pos = 4, type = TabType.Pure_Data, data = "[ENTER REASON]")
    public void PunishTargetPlayer(CraftPlayer admin, PunishmentType punishmentType, String target, int extraData, String... reason){
        var offlinePlayer = Bukkit.getOfflinePlayer(target);
        var serverMessage = PunishmentSystemAPI.AddPunishmentToTarget(admin, offlinePlayer, punishmentType, extraData, BuildReason(reason, ' '), true);
    }

    @PCMethod
    @PCSignature({"reports", "view"})
    public void ViewAllReports(CraftPlayer admin){
        if(!PunishmentSystem.PlayerPermissionConfig.CanOpenReports(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        ServerReportAPI.DisplayFullServerReportList(admin, null);
    }

    @PCMethod
    @PCSignature({"reports", "view"})
    @PCTab(pos = 1, type = TabType.Information_From_Function, data = "OfflinePlayerNames")
    public void ViewAllReports(CraftPlayer admin, String target){
        if(!PunishmentSystem.PlayerPermissionConfig.CanOpenReports(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        var offlinePlayer = Bukkit.getOfflinePlayer(target);
        ServerReportAPI.DisplayFullServerReportList(admin, offlinePlayer);
    }

    @PCMethod
    @PCSignature({"reports", "open"})
    public void OpenServerReport(CraftPlayer admin, String documentID){
        if(!PunishmentSystem.PlayerPermissionConfig.CanOpenReports(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        var serverReport = ServerReportAPI.ReturnServerReportByID(documentID);
        if(serverReport == null) admin.sendMessage(ChatColor.RED + "No reports found!");
        else serverReport.DisplayReport(admin);
    }

    @PCMethod
    @PCSignature({"reports", "flag"})
    public void FlagServerReport(CraftPlayer admin, String documentID){
        if(!PunishmentSystem.PlayerPermissionConfig.CanFlagReports(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        var serverReport = ServerReportAPI.ReturnServerReportByID(documentID);
        if(serverReport == null) admin.sendMessage(ChatColor.RED + "No reports found!");
        else{
            serverReport.flagged = true;
            ConfigAPI.Save(serverReport, false);
            ServerReportAPI.DisplayFullServerReportList(admin, null);
        }
    }

    @PCMethod
    @PCSignature({"reports", "delete"})
    public void DeleteServerReport(CraftPlayer admin, String documentID){
        if(!PunishmentSystem.PlayerPermissionConfig.CanDeleteReports(admin)){
            SendPlayerMessage(admin, ServerMessage.PlayerHasNoPermission);
            return;
        }

        var serverReport = ServerReportAPI.ReturnServerReportByID(documentID);
        if(serverReport == null) admin.sendMessage(ChatColor.RED + "No reports found!");
        else{
            ConfigAPI.Delete(serverReport);
            PunishmentSystem.serverReports.remove(serverReport);
            ServerReportAPI.DisplayFullServerReportList(admin, null);
        }
    }

    @PCMethodData
    public List<String> OfflinePlayerNames(String current){
        var data = new ArrayList<String>();
        for(var s : Bukkit.getOfflinePlayers()){
            if(s.getName().toLowerCase().contains(current.toLowerCase())) data.add(s.getName());
        }
        return data;
    }

    private void SendPlayerMessage(Player player, ServerMessage serverMessage){
        var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(serverMessage);
        ChatAPI.chatBuilder().messageType(MessageType.Player).playerToo(player).SendMessage(messageToSendPlayer);
    }
}