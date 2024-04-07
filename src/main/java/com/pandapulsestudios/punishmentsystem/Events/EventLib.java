package com.pandapulsestudios.punishmentsystem.Events;

import com.pandapulsestudios.pulsecore.Chat.ChatAPI;
import com.pandapulsestudios.pulsecore.Chat.MessageType;
import com.pandapulsestudios.pulsecore.Java.PulseAutoRegister;
import com.pandapulsestudios.pulsecore.Player.PlayerAPI;
import com.pandapulsestudios.pulsecore.Player.PlayerAction;
import com.pandapulsestudios.punishmentsystem.Enum.ServerMessage;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@PulseAutoRegister
public class EventLib implements Listener {
    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent event){
        var player = event.getPlayer();
        var actionState = PlayerAPI.CanPlayerAction(PlayerAction.AsyncPlayerChatSend, player);
        if(actionState) return;
        var messageToSendPlayer = PunishmentSystem.ServerMessagesConfig.GetStoredMessage(ServerMessage.YouAreMuted);
        ChatAPI.chatBuilder().messageType(MessageType.Player).playerToo(player).SendMessage(messageToSendPlayer);
    }

    @EventHandler
    public void ServerReportCreation(ServerReportCreation event){
        event.setCancelled(true);
    }
}