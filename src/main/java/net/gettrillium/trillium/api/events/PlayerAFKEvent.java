package net.gettrillium.trillium.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAFKEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private boolean isAFK;
    private boolean cancelled;

    public PlayerAFKEvent(Player player, boolean isAFK) {
        this.player = player;
        this.isAFK = isAFK;
    }

    public boolean isAFK() {
        return this.isAFK;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
