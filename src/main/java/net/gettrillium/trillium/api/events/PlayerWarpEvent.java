package net.gettrillium.trillium.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWarpEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private String name;
    private Player player;
    private Location from;
    private Location to;
    private boolean cancelled;

    public PlayerWarpEvent(String warpName, Player player, Location from, Location to) {
        name = warpName;
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public String getWarpName() {
        return name;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
