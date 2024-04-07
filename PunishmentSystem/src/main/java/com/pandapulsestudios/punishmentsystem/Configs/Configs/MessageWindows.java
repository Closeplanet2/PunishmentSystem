package com.pandapulsestudios.punishmentsystem.Configs.Configs;

import com.pandapulsestudios.pulseconfig.APIS.ConfigAPI;
import com.pandapulsestudios.pulseconfig.Enums.SaveableType;
import com.pandapulsestudios.pulseconfig.Interfaces.Config.PulseConfig;
import com.pandapulsestudios.pulseconfig.Objects.Savable.SaveableArrayList;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageWindows implements PulseConfig {
    @Override
    public String documentID() { return "MessageWindows"; }

    @Override
    public JavaPlugin mainClass() { return PunishmentSystem.Instance; }

    @Override
    public boolean useSubFolder() { return false; }

    public SaveableArrayList<String> banWindow = new SaveableArrayList<>(SaveableType.CONFIG, String.class);
    public SaveableArrayList<String> helpWindow = new SaveableArrayList<>(SaveableType.CONFIG, String.class);

    @Override
    public void FirstLoad() {
        DefaultBanWindow();
        DefaultHelpWindow();
    }

    @Override
    public void BeforeLoad() {
        DefaultBanWindow();
        DefaultHelpWindow();
    }

    @Override
    public void BeforeSave() {
        DefaultBanWindow();
        DefaultHelpWindow();
    }

    public MessageWindows(){
        ConfigAPI.Load(this, false);
    }

    public String ReturnBanWindow(ServerReport serverReport){
        var stringBuilder = new StringBuilder();
        for(var x : banWindow.arrayList) stringBuilder.append(x.replace("%ACTION_REASON%", serverReport.reportMessage));
        return stringBuilder.toString();
    }

    public void DisplayHelpMenu(Player player){
        for(var x : helpWindow.arrayList) player.sendMessage(x);
    }

    private void DefaultBanWindow(){
        if(!banWindow.arrayList.isEmpty()) return;
        banWindow.arrayList.add(StringUtils.repeat("\n", 5));
        banWindow.arrayList.add("%ACTION_REASON%");
        banWindow.arrayList.add(StringUtils.repeat("\n", 5));
    }

    private void DefaultHelpWindow(){
        if(!helpWindow.arrayList.isEmpty()) return;
    }
}