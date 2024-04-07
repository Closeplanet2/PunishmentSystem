/**
 *
 * @CLOSEPLANET2
 *
 */

package com.pandapulsestudios.punishmentsystem.API;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulsecore.Chat.ChatAPI;
import com.pandapulsestudios.pulsecore.Chat.MessageType;
import com.pandapulsestudios.punishmentsystem.BukkitRunnable.TempPunishmentRunnable;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import com.pandapulsestudios.punishmentsystem.Enum.PunishmentType;
import com.pandapulsestudios.punishmentsystem.Enum.ServerMessage;
import com.pandapulsestudios.punishmentsystem.Events.ServerReportCreation;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class PunishmentSystemAPI {
    public static ServerMessage AddPunishmentToTarget(Player admin, OfflinePlayer target, PunishmentType punishmentType, int extraData, String reportMessage, boolean createReport){
        extraData = Math.max(0, extraData);

        if(target == null){
            var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(ServerMessage.TargetPlayerNull);
            SendMessageToPlayers(messageToSendPlayer, admin);
            return ServerMessage.TargetPlayerNull;
        }

        if(!PunishmentSystem.PlayerPermissionConfig.CanComputePunishmentType(admin, punishmentType)){
            var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(ServerMessage.PlayerHasNoPermission);
            SendMessageToPlayers(messageToSendPlayer, admin);
            return ServerMessage.PlayerHasNoPermission;
        }

        if(punishmentType == PunishmentType.KICK){
            if(!target.isOnline()){
                var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(ServerMessage.TargetPlayerOffline);
                SendMessageToPlayers(messageToSendPlayer.replace("%target_player%", target.getName()), admin);
                return ServerMessage.TargetPlayerOffline;
            }
        }

        var serverReport = new ServerReport(admin, target, punishmentType, reportMessage);
        var serverReportCreation = new ServerReportCreation(serverReport);
        Bukkit.getServer().getPluginManager().callEvent(serverReportCreation);

        if(admin != null && serverReportCreation.isCancelled()){
            var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(ServerMessage.ServerReportCancelled);
            SendMessageToPlayers(messageToSendPlayer.replace("%CANCEL_REASON%", serverReportCreation.getCancelReason()), admin);
            return ServerMessage.ServerReportCancelled;
        }

        if(createReport){
            PunishmentSystem.serverReports.add(serverReport);
            ConfigAPI.Save(serverReport, false);
        }

        if(punishmentType == PunishmentType.KICK){
            return KickPlayer(admin, target, serverReport);
        } else if(punishmentType == PunishmentType.MUTE){
            return MutePlayer(admin, target, serverReport);
        } else if(punishmentType == PunishmentType.UN_MUTE){
            return UnMutePlayer(admin, target, serverReport);
        } else if(punishmentType == PunishmentType.TEMP_MUTE){
            return TempPunishmentRunnable.StartPlayerRunnable(admin, target, PunishmentType.MUTE, PunishmentType.UN_MUTE, serverReport.reportMessage, extraData);
        } else if(punishmentType == PunishmentType.BAN_NAME){
            return BanPlayerName(admin, target, extraData, serverReport);
        } else if(punishmentType == PunishmentType.UN_BAN){
            return UnBanPlayer(admin, target, serverReport);
        } else if(punishmentType == PunishmentType.BAN_IP && target.getPlayer() != null){
            return BanPlayerIP(admin, target.getPlayer(), extraData, serverReport);
        }

        return null;
    }

    private static ServerMessage KickPlayer(Player admin, OfflinePlayer target, ServerReport serverReport){
        var player = target.getPlayer();
        if(player != null) player.kickPlayer(serverReport.reportMessage);
        return SendPunishmentMessageToPlayers(target, serverReport, admin, target);
    }

    private static ServerMessage MutePlayer(Player admin, OfflinePlayer target, ServerReport serverReport){
        var playerState = PunishmentSystem.CurrentPlayerStates.GetPlayerState(target.getUniqueId());
        playerState.isMuted = true;
        PunishmentSystem.CurrentPlayerStates.SavePlayerState(target.getUniqueId(), playerState);
        return SendPunishmentMessageToPlayers(target, serverReport, admin, target);
    }

    private static ServerMessage UnMutePlayer(Player admin, OfflinePlayer target, ServerReport serverReport){
        var playerState = PunishmentSystem.CurrentPlayerStates.GetPlayerState(target.getUniqueId());
        playerState.isMuted = false;
        PunishmentSystem.CurrentPlayerStates.SavePlayerState(target.getUniqueId(), playerState);
        return SendPunishmentMessageToPlayers(target, serverReport, admin, target);
    }

    private static ServerMessage BanPlayerName(Player admin, OfflinePlayer target, int extraData, ServerReport serverReport){
        var playerState = PunishmentSystem.CurrentPlayerStates.GetPlayerState(target.getUniqueId());
        playerState.isBanned = true;
        PunishmentSystem.CurrentPlayerStates.SavePlayerState(target.getUniqueId(), playerState);

        BanPlayer(target.getName(), BanList.Type.NAME, extraData, serverReport);
        if(extraData > 0) TempPunishmentRunnable.StartPlayerRunnable(admin, target, null, PunishmentType.UN_BAN, serverReport.reportMessage, extraData);

        var player = target.getPlayer();
        if(player != null) player.kickPlayer(serverReport.reportMessage);
        return SendPunishmentMessageToPlayers(target, serverReport, admin, target);
    }

    private static ServerMessage BanPlayerIP(Player admin, Player target, int extraData, ServerReport serverReport){
        var playerState = PunishmentSystem.CurrentPlayerStates.GetPlayerState(target.getUniqueId());
        playerState.isBanned = true;
        playerState.ipBanned = target.getAddress().getHostName();
        PunishmentSystem.CurrentPlayerStates.SavePlayerState(target.getUniqueId(), playerState);

        BanPlayer(playerState.ipBanned, BanList.Type.IP, extraData, serverReport);
        if(extraData > 0) TempPunishmentRunnable.StartPlayerRunnable(admin, target, null, PunishmentType.UN_BAN, serverReport.reportMessage, extraData);

        var player = target.getPlayer();
        if(player != null) player.kickPlayer(serverReport.reportMessage);
        return SendPunishmentMessageToPlayers(target, serverReport, admin, target);
    }

    private static ServerMessage UnBanPlayer(Player admin, OfflinePlayer target, ServerReport serverReport){
        var playerState = PunishmentSystem.CurrentPlayerStates.GetPlayerState(target.getUniqueId());

        var banList = Bukkit.getBanList(BanList.Type.IP);
        if(banList.isBanned(playerState.ipBanned)) banList.pardon(playerState.ipBanned);
        banList = Bukkit.getBanList(BanList.Type.NAME);
        if(banList.isBanned(target.getName())) banList.pardon(target.getName());

        playerState.isBanned = false;
        playerState.ipBanned = "";
        PunishmentSystem.CurrentPlayerStates.SavePlayerState(target.getUniqueId(), playerState);
        return SendPunishmentMessageToPlayers(target, serverReport, admin, target);
    }

    private static void BanPlayer(String value, BanList.Type banlistType, int extraData, ServerReport serverReport){
        var banlist = Bukkit.getBanList(banlistType);
        var tempBan = extraData == 0 ? null : new Date(new Date().getTime() + extraData * 1000);
        banlist.addBan(value, PunishmentSystem.MessageWindows.ReturnBanWindow(serverReport), tempBan, null);
    }

    private static ServerMessage SendPunishmentMessageToPlayers(OfflinePlayer target, ServerReport serverReport, OfflinePlayer... offlinePlayers){
        var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(ServerMessage.PunishmentMessage);
        var stage1 = messageToSendPlayer.replace("%ACTION_REASON%", serverReport.reportMessage);
        var stage2 = stage1.replace("%target_player%", target.getName());
        var stage3= stage2.replace("%ACTION_NAME%", serverReport.punishmentType.actionName);
        SendMessageToPlayers(stage3, offlinePlayers);
        return ServerMessage.PunishmentMessage;
    }

    private static void SendMessageToPlayers(String message, OfflinePlayer... offlinePlayers){
        for(var offlinePlayer : offlinePlayers){
            if(offlinePlayer == null || !offlinePlayer.isOnline()) continue;
            ChatAPI.chatBuilder().messageType(MessageType.Player).playerToo(offlinePlayer.getPlayer()).SendMessage(message);
        }
    }

}
