package com.pandapulsestudios.punishmentsystem.Enum;

public enum ServerMessage {
    PlayerHasNoPermission("#eb4034You don't have permission to use this command!"),
    ServerReportCancelled("#eb4034The Server report has been cancelled! [#5FADFF%CANCEL_REASON%#eb4034]!"),
    TargetPlayerOffline("#eb4034The target player [#5FADFF%target_player%#eb4034] is offline!"),
    TargetPlayerNull("#eb4034The target player cannot be found!"),
    PunishmentMessage("#5FADFF%target_player%#61ffef has been %ACTION_NAME%! Reason: #5FADFF%ACTION_REASON%"),
    YouAreMuted("#eb4034You are currently muted on the server!"),
    ReloadConfigs("##03fc4eYou have reloaded the configs!"),
    ResetConfigs("##03fc4eYou have reset the configs!");


    public final String defaultMessage;
    ServerMessage(String defaultMessage){
        this.defaultMessage = defaultMessage;
    }
}
