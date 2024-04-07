package com.pandapulsestudios.punishmentsystem.API;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulsecore.Chat.ChatAPI;
import com.pandapulsestudios.pulsecore.Chat.MessageType;
import com.pandapulsestudios.pulsecore.FileSystem.DirAPI;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ServerReportAPI {
    public static ArrayList<ServerReport> ReturnALL(){
        var data = new ArrayList<ServerReport>();
        var configPath = ConfigAPI.ReturnConfigPath(new ServerReport(""));
        var dirFiles = DirAPI.ReturnAllFilesFromDirectory(new File(configPath), false);
        for(var file : dirFiles){
            var fileName = file.getName().replace(".yml", "");
            var report = new ServerReport(fileName);
            ConfigAPI.Load(report, false);
            data.add(report);
        }
        return data;
    }

    public static void DisplayFullServerReportList(Player player, OfflinePlayer target){
        var displayed = false;

        ChatAPI.chatBuilder().messageType(MessageType.Player).playerToo(player).SendMessage(ChatColor.BOLD + "#f28abc[FLAGGED REPORTS]");
        for(var serverReport : PunishmentSystem.serverReports){
            if(target != null && !serverReport.targetPlayerUUID.equals(target.getUniqueId())) continue;
            if(!serverReport.flagged) continue;
            DisplayReport(player, serverReport, ChatColor.RED, true);
            displayed = true;
        }
        if(!displayed) player.sendMessage(ChatColor.YELLOW + "No reports found!");


        displayed = false;
        ChatAPI.chatBuilder().messageType(MessageType.Player).playerToo(player).SendMessage(ChatColor.BOLD + "#f28abc[FLAGGED REPORTS]");
        for(var serverReport : PunishmentSystem.serverReports){
            if(target != null && !serverReport.targetPlayerUUID.equals(target.getUniqueId())) continue;
            if(serverReport.flagged) continue;
            DisplayReport(player, serverReport, ChatColor.LIGHT_PURPLE, false);
            displayed = true;
        }
        if(!displayed) player.sendMessage(ChatColor.YELLOW + "No reports found!");
    }

    private static void DisplayReport(Player player, ServerReport serverReport, ChatColor mainColor, boolean isFlagged){
        var textComp1 = new TextComponent(mainColor + String.format("[%s] [%s] ", serverReport.reportDate, serverReport.targetPlayerName));
        player.spigot().sendMessage(textComp1);

        var textComp2 = new TextComponent(ChatColor.AQUA + "[VIEW REPORT]");
        textComp2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("[VIEW REPORT]")));
        textComp2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment reports open " + serverReport.documentID));

        if(!isFlagged){
            var textComp3 = new TextComponent(ChatColor.AQUA + " [FLAG REPORT]");
            textComp3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("[FLAG REPORT]")));
            textComp3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment reports flag " + serverReport.documentID));
            textComp2.addExtra(textComp3);
        }

        var textComp4 = new TextComponent(ChatColor.AQUA + " [DELETE REPORT]");
        textComp4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("[DELETE REPORT]")));
        textComp4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment reports delete " + serverReport.documentID));
        textComp2.addExtra(textComp4);

        player.spigot().sendMessage(textComp2);
    }

    public static ServerReport ReturnServerReportByID(String id){
        for(var serverReport : PunishmentSystem.serverReports){
            if(serverReport.documentID.equals(id)) return serverReport;
        }
        return null;
    }
}
