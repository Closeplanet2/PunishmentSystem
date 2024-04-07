package com.pandapulsestudios.punishmentsystem.API;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.punishmentsystem.Configs.Configs.*;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;

public class PunishmentConfigAPI {
    public static void ReloadConfigs(){
        ConfigAPI.Load(PunishmentSystem.PlayerPermissionConfig, false);
        ConfigAPI.Load(PunishmentSystem.ServerMessagesConfig, false);
        ConfigAPI.Load(PunishmentSystem.CurrentPlayerStates, false);
        ConfigAPI.Load(PunishmentSystem.MessageWindows, false);
        PunishmentSystem.serverReports = ServerReportAPI.ReturnALL();
    }

    public static void ResetConfigs(){
        if(PunishmentSystem.PlayerPermissionConfig != null) ConfigAPI.Delete(PunishmentSystem.PlayerPermissionConfig);
        if(PunishmentSystem.ServerMessagesConfig != null) ConfigAPI.Delete(PunishmentSystem.ServerMessagesConfig);
        if(PunishmentSystem.CurrentPlayerStates != null) ConfigAPI.Delete(PunishmentSystem.CurrentPlayerStates);
        if(PunishmentSystem.MessageWindows != null) ConfigAPI.Delete(PunishmentSystem.MessageWindows);
        PunishmentSystem.PlayerPermissionConfig = new PlayerPermissionsConfig();
        PunishmentSystem.ServerMessagesConfig = new ServerMessagesConfig();
        PunishmentSystem.CurrentPlayerStates = new CurrentPlayerStates();
        PunishmentSystem.MessageWindows = new MessageWindows();
        PunishmentSystem.serverReports = ServerReportAPI.ReturnALL();
    }
}