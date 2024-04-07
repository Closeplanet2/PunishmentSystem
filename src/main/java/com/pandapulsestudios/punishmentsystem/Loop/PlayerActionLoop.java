package com.pandapulsestudios.punishmentsystem.Loop;

import com.pandapulsestudios.pulsecore.Java.PulseAutoRegister;
import com.pandapulsestudios.pulsecore.Loops.PulseLoop;
import com.pandapulsestudios.pulsecore.Player.PlayerAPI;
import com.pandapulsestudios.pulsecore.Player.PlayerAction;
import com.pandapulsestudios.punishmentsystem.Configs.Configs.PlayerState;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;

import java.util.UUID;

@PulseAutoRegister
public class PlayerActionLoop implements PulseLoop {
    @Override
    public Long StartDelay() { return 0L; }

    @Override
    public Long LoopInterval() { return 20L;}

    @Override
    public void LoopFunction() {
        var allPlayerStates = PunishmentSystem.CurrentPlayerStates.currentPlayerStates.hashMap;
        for(var uuid : allPlayerStates.keySet()){
            UpdateMute(uuid, allPlayerStates.get(uuid));
        }
    }

    private void UpdateMute(UUID uuid, PlayerState playerState){
        if(playerState.isMuted && PlayerAPI.CanPlayerAction(PlayerAction.AsyncPlayerChatSend, uuid)){
            PlayerAPI.TogglePlayerAction(PlayerAction.AsyncPlayerChatSend, false, uuid);
        }else if(!playerState.isMuted && !PlayerAPI.CanPlayerAction(PlayerAction.AsyncPlayerChatSend, uuid)){
            PlayerAPI.TogglePlayerAction(PlayerAction.AsyncPlayerChatSend, true, uuid);
        }
    }
}