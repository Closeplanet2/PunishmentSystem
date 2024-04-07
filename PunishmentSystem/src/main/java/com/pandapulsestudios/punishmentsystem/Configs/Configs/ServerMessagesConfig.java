package com.pandapulsestudios.punishmentsystem.Configs.Configs;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.Enums.SaveableType;
import com.pandapulsestudios.pulseconfig.Interfaces.Config.PulseConfig;
import com.pandapulsestudios.pulseconfig.Interfaces.SaveName;
import com.pandapulsestudios.pulseconfig.Objects.Savable.SaveableHashmap;
import com.pandapulsestudios.pulsecore.Chat.ChatAPI;
import com.pandapulsestudios.pulsecore.Chat.MessageType;
import com.pandapulsestudios.punishmentsystem.Enum.ServerMessage;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerMessagesConfig implements PulseConfig {
    @Override
    public String documentID() { return "ServerMessagesConfig"; }

    @Override
    public JavaPlugin mainClass() { return PunishmentSystem.Instance;}

    @Override
    public boolean useSubFolder() { return false; }
    public SaveableHashmap<ServerMessage, String> storedServerMessages = new SaveableHashmap<>(SaveableType.CONFIG, ServerMessage.class, String.class);

    @Override
    public void FirstLoad() {
        DefaultConfig();
    }

    @Override
    public void AfterLoad() {
        DefaultConfig();
    }

    public ServerMessagesConfig(){
        ConfigAPI.Load(this, false);
    }

    public String GetStoredMessage(ServerMessage serverMessage){
        return storedServerMessages.hashMap.getOrDefault(serverMessage, serverMessage.defaultMessage);
    }

    private void DefaultConfig(){
        for(var serverMessage : ServerMessage.values()){
            if(!storedServerMessages.hashMap.containsKey(serverMessage)) storedServerMessages.hashMap.put(serverMessage, serverMessage.defaultMessage);
        }
    }
}