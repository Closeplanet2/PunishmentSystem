package com.pandapulsestudios.punishmentsystem.Enum;

public enum PunishmentType {
    BAN_NAME(PluginPermissions.BAN_PLAYER_NAME, "Banned", ""),
    UN_BAN(PluginPermissions.UN_BAN_PLAYER_NAME, "Un-Banned", "Temp Ban Ended!"),
    BAN_IP(PluginPermissions.BAN_PLAYER_IP, "Banned", ""),
    MUTE(PluginPermissions.MUTE_PLAYER, "Muted", ""),
    UN_MUTE(PluginPermissions.UN_MUTE_PLAYER, "Un-Muted", "Temp Mute Ended!"),
    TEMP_MUTE(PluginPermissions.TEMP_MUTE_PLAYER, "Temp Muted", ""),
    KICK(PluginPermissions.KICK_PLAYER, "Kicked", "");

    public final PluginPermissions pluginPermissions;
    public final String actionName;
    public final String tempTimeReason;
    PunishmentType(PluginPermissions pluginPermissions, String actionName, String tempTimeReason){
        this.pluginPermissions = pluginPermissions;
        this.actionName = actionName;
        this.tempTimeReason = tempTimeReason;
    }

}