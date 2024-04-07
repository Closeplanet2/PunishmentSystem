/**
 *
 * @CLOSEPLANET2
 *
 */

package com.pandapulsestudios.punishmentsystem.Enum;

public enum PluginPermissions {
    BAN_PLAYER_NAME("PunishmentSystem.BAN_PLAYER_NAME"),
    UN_BAN_PLAYER_NAME("PunishmentSystem.UN_BAN_PLAYER"),
    BAN_PLAYER_IP("PunishmentSystem.BAN_PLAYER_IP"),
    MUTE_PLAYER("PunishmentSystem.MUTE_PLAYER"),
    UN_MUTE_PLAYER("PunishmentSystem.UN_MUTE_PLAYER"),
    TEMP_MUTE_PLAYER("PunishmentSystem.TEMP_MUTE_PLAYER"),
    KICK_PLAYER("PunishmentSystem.KICK_PLAYER"),
    VIEW_MUTED("PunishmentSystem.VIEW_MUTED"),
    VIEW_BANNED("PunishmentSystem.VIEW_BANNED"),
    VIEW_REPORTS("PunishmentSystem.VIEW_REPORTS"),
    FLAG_REPORTS("PunishmentSystem.FLAG_REPORTS"),
    Delete_REPORTS("PunishmentSystem.Delete_REPORTS"),
    RELOAD_CONFIGS("PunishmentSystem.RELOAD_CONFIGS");

    public final String defaultPermission;
    PluginPermissions(String defaultPermission){
        this.defaultPermission = defaultPermission;
    }
}
