package com.pandapulsestudios.punishmentsystem;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.APIS.MongoAPI;
import com.pandapulsestudios.pulseconfig.Interfaces.Mongo.PulseMongo;
import com.pandapulsestudios.punishmentsystem.API.PunishmentConfigAPI;
import com.pandapulsestudios.punishmentsystem.BukkitRunnable.TempPunishmentRunnable;
import com.pandapulsestudios.punishmentsystem.Configs.Configs.*;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import com.pandapulsestudios.pulsecommands.PulseCommands;
import com.pandapulsestudios.pulsecore.Java.ClassAPI;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public final class PunishmentSystem extends JavaPlugin {

    public static PunishmentSystem Instance;
    public static PlayerPermissionsConfig PlayerPermissionConfig;
    public static ServerMessagesConfig ServerMessagesConfig;
    public static CurrentPlayerStates CurrentPlayerStates;
    public static MessageWindows MessageWindows;

    public static ArrayList<TempPunishmentRunnable> tempPunishmentRunnables = new ArrayList<>();
    public static ArrayList<ServerReport> serverReports = new ArrayList<>();

    @Override
    public void onEnable() {
        Instance = this;
        MongoAPI.setLevel(Level.SEVERE);
        MongoAPI.CreateConnection("mongodb+srv://admin:admin1234@rollerite.ul69f5s.mongodb.net/?retryWrites=true&w=majority&appName=Rollerite", "Rollerite");
        var api = new API();
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + api.api.toString());

        ClassAPI.RegisterClasses(this);
        PulseCommands.RegisterRaw(this);
        PunishmentConfigAPI.ResetConfigs();
    }

    @Override
    public void onDisable() {
        for(var activeRunnable : tempPunishmentRunnables) activeRunnable.CancelEvent();
    }

    private static class API implements PulseMongo{
        @Override
        public String databaseName() { return "Rollerite"; }

        @Override
        public String documentID() { return "API"; }

        public UUID api = UUID.randomUUID();

        public API(){
            MongoAPI.Load(this, false);
        }
    }
}
