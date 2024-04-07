package com.pandapulsestudios.punishmentsystem.BukkitRunnable;

import com.pandapulsestudios.punishmentsystem.API.PunishmentSystemAPI;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import com.pandapulsestudios.punishmentsystem.Enum.PunishmentType;
import com.pandapulsestudios.punishmentsystem.Enum.ServerMessage;
import com.pandapulsestudios.punishmentsystem.PunishmentSystem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TempPunishmentRunnable extends BukkitRunnable {
    public static ServerMessage StartPlayerRunnable(Player admin, OfflinePlayer target, PunishmentType start, PunishmentType next, String reason, int time){
        if(start != null) PunishmentSystemAPI.AddPunishmentToTarget(admin, target, start, 0, "[TEMP] " + reason, true);
        var runnable = new TempPunishmentRunnable(admin, target, next, time);
        runnable.runTaskTimer(PunishmentSystem.Instance, 0, 20);
        PunishmentSystem.tempPunishmentRunnables.add(runnable);
        return ServerMessage.PunishmentMessage;
    }

    private final Player admin;
    private final OfflinePlayer target;
    private final PunishmentType punishmentType;
    private int time;

    private TempPunishmentRunnable(Player admin, OfflinePlayer target, PunishmentType punishmentType, int time){
        this.admin = admin;
        this.target = target;
        this.punishmentType = punishmentType;
        this.time = time;
    }

    @Override
    public void run() {
        this.time -= 1;
        if(this.time <= 0) CancelEvent();
    }

    public void CancelEvent(){
        PunishmentSystemAPI.AddPunishmentToTarget(admin, target, punishmentType, 0, punishmentType.tempTimeReason, true);
        cancel();
    }
}