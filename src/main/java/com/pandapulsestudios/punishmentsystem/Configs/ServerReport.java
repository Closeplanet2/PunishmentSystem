/**
 *
 * @CLOSEPLANET2
 *
 */

package com.pandapulsestudios.punishmentsystem.Configs;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.APIS.MongoAPI;
import com.pandapulsestudios.pulseconfig.Interfaces.Config.PulseConfig;
import com.pandapulsestudios.pulseconfig.Interfaces.IgnoreSave;
import com.pandapulsestudios.pulseconfig.Interfaces.Mongo.PulseMongo;
import com.pandapulsestudios.pulsecore.FileSystem.DirAPI;
import com.pandapulsestudios.punishmentsystem.Enum.PunishmentType;
import com.pandapulsestudios.punishmentsystem.Enum.ServerMessage;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.text.Document;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ServerReport implements PulseConfig {

    @Override
    public JavaPlugin mainClass() { return PunishmentSystem.Instance; }

    @Override
    public boolean useSubFolder() { return true; }

    @Override
    public String documentID() { return documentID; }

    @IgnoreSave
    public String documentID = "";
    public String reportingPlayerName = "";
    public UUID reportingPlayerUUID = UUID.randomUUID();
    public String targetPlayerName = "";
    public UUID targetPlayerUUID = UUID.randomUUID();
    public PunishmentType punishmentType = PunishmentType.MUTE;
    public String reportMessage = "";
    public Date reportDate = new Date();
    public boolean flagged = false;

    public ServerReport(String documentID){
        this.documentID = documentID;
    }

    public ServerReport(Player admin, OfflinePlayer target, PunishmentType punishmentType, String reportMessage){
        this.documentID = UUID.randomUUID().toString();
        this.reportingPlayerName = admin == null ? "NO ADMIN SET" : admin.getName();
        this.reportingPlayerUUID = admin == null ? UUID.randomUUID() : admin.getUniqueId();
        this.targetPlayerName = target.getName();
        this.targetPlayerUUID = target.getUniqueId();
        this.punishmentType = punishmentType;
        this.reportMessage = reportMessage;
    }

    public void DisplayReport(Player player){
        player.sendMessage(ChatColor.GREEN + String.format("[%s]", documentID));
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Date: " + reportDate.toString());
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Reporting Player: "+ reportingPlayerName);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Target Player: " + targetPlayerName);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Punishment: " + punishmentType.name());
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Reason: " + reportMessage);
    }

    public void FlagReport(){

    }
}
