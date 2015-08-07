package net.gettrillium.trillium.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHomeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private String name;
    private Player player;
    private Location from;
    private Location to;
    private boolean cancelled = false;

    public PlayerHomeEvent(String homeName, Player player, Location from, Location to) {
        name = homeName;
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public String getHomeName() {
        return this.name;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
