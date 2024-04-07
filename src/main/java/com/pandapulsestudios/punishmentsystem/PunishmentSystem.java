package com.pandapulsestudios.punishmentsystem;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.APIS.MongoAPI;
import com.pandapulsestudios.pulseconfig.Interfaces.Mongo.PulseMongo;
import com.pandapulsestudios.punishmentsystem.API.PunishmentConfigAPI;
import com.pandapulsestudios.punishmentsystem.API.PunishmentSystemAPI;
import com.pandapulsestudios.punishmentsystem.BukkitRunnable.TempPunishmentRunnable;
import com.pandapulsestudios.punishmentsystem.Configs.Configs.*;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import com.pandapulsestudios.punishmentsystem.Enum.PunishmentType;
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
        ClassAPI.RegisterClasses(this);
        PulseCommands.RegisterRaw(this);
        PunishmentConfigAPI.ResetConfigs();

        PunishmentSystemAPI.AddPunishmentToTarget(admin, target, PunishmentType.MUTE, 0, "Report Message", true)
    }

    @Override
    public void onDisable() {
        for(var activeRunnable : tempPunishmentRunnables) activeRunnable.CancelEvent();
    }
}