package com.pandapulsestudios.punishmentsystem.Events;

import com.pandapulsestudios.pulsecore.BossBar.PandaBossBar;
import com.pandapulsestudios.punishmentsystem.Configs.ServerReport;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerReportCreation extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final ServerReport serverReport;
    private String cancelReason;

    public ServerReportCreation(ServerReport serverReport){
        this.serverReport = serverReport;
        Bukkit.getServer().getPluginManager().callEvent(this);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    public HandlerList getHandlers() {
        return handlers;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }


    public ServerReport getServerReport(){ return serverReport; }
    public String getCancelReason(){return cancelReason; }
    public void setCancelReason(String cancelReason){this.cancelReason = cancelReason;}
}