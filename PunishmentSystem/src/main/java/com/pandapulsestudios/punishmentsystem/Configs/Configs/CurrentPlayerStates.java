package com.pandapulsestudios.punishmentsystem.Configs.Configs;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.Enums.SaveableType;
import com.pandapulsestudios.pulseconfig.Interfaces.Config.PulseConfig;
import com.pandapulsestudios.pulseconfig.Objects.Savable.SaveableHashmap;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class CurrentPlayerStates implements PulseConfig {
    @Override
    public String documentID() { return "CurrentPlayerStates"; }

    @Override
    public JavaPlugin mainClass() { return PunishmentSystem.Instance; }

    @Override
    public boolean useSubFolder() { return false; }

    public SaveableHashmap<UUID, PlayerState> currentPlayerStates = new SaveableHashmap<>(SaveableType.CONFIG, UUID.class, PlayerState.class);

    @Override
    public void FirstLoad() {
        DefaultValues();
    }

    @Override
    public void AfterLoad() {
        DefaultValues();
    }

    public CurrentPlayerStates(){
        ConfigAPI.Load(this, false);
    }

    public PlayerState GetPlayerState(UUID player){
        var playerState = currentPlayerStates.hashMap.getOrDefault(player, null);
        if(playerState != null) return playerState;
        playerState = new PlayerState();
        SavePlayerState(player, playerState);
        return playerState;
    }

    public void SavePlayerState(UUID player, PlayerState playerState){
        currentPlayerStates.hashMap.put(player, playerState);
        ConfigAPI.Save(this, false);
    }

    private void DefaultValues(){
        for(var player : Bukkit.getOfflinePlayers()) GetPlayerState(player.getUniqueId());
    }
}
