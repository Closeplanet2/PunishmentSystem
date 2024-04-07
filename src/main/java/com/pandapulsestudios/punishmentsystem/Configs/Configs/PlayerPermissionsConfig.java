package com.pandapulsestudios.punishmentsystem.Configs.Configs;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.Enums.SaveableType;
import com.pandapulsestudios.pulseconfig.Interfaces.Config.PulseConfig;
import com.pandapulsestudios.pulseconfig.Interfaces.SaveName;
import com.pandapulsestudios.pulseconfig.Objects.Savable.SaveableHashmap;
import com.pandapulsestudios.punishmentsystem.Enum.PluginPermissions;
import com.pandapulsestudios.punishmentsystem.Enum.PunishmentType;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerPermissionsConfig implements PulseConfig {
    @Override
    public String documentID() { return "PlayerPermissions"; }

    @Override
    public JavaPlugin mainClass() { return PunishmentSystem.Instance; }

    @Override
    public boolean useSubFolder() { return false; }

    public SaveableHashmap<PluginPermissions, String> permissionNodes = new SaveableHashmap<>(SaveableType.CONFIG, PluginPermissions.class, String.class);

    @Override
    public void FirstLoad() { DefaultConfig(); }

    @Override
    public void AfterLoad() { DefaultConfig(); }

    public PlayerPermissionsConfig(){
        ConfigAPI.Load(this, false);
    }

    public boolean CanComputePunishmentType(Player player, PunishmentType punishmentType){
        return player == null || player.hasPermission(permissionNodes.hashMap.getOrDefault(punishmentType.pluginPermissions, punishmentType.pluginPermissions.defaultPermission));
    }

    public boolean CanReloadConfigs(Player player){
        return player == null || player.hasPermission(permissionNodes.hashMap.getOrDefault(PluginPermissions.RELOAD_CONFIGS, PluginPermissions.RELOAD_CONFIGS.defaultPermission));
    }

    public boolean CanOpenReports(Player player){
        return player == null || player.hasPermission(permissionNodes.hashMap.getOrDefault(PluginPermissions.VIEW_REPORTS, PluginPermissions.VIEW_REPORTS.defaultPermission));
    }

    public boolean CanFlagReports(Player player){
        return player == null || player.hasPermission(permissionNodes.hashMap.getOrDefault(PluginPermissions.FLAG_REPORTS, PluginPermissions.FLAG_REPORTS.defaultPermission));
    }

    public boolean CanDeleteReports(Player player){
        return player == null || player.hasPermission(permissionNodes.hashMap.getOrDefault(PluginPermissions.FLAG_REPORTS, PluginPermissions.FLAG_REPORTS.defaultPermission));
    }

    private void DefaultConfig(){
        for(var pluginPermission : PluginPermissions.values()){
            if(!permissionNodes.hashMap.containsKey(pluginPermission)) permissionNodes.hashMap.put(pluginPermission, pluginPermission.defaultPermission);
        }
    }
}