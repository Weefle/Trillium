package net.gettrillium.trillium.api.events;

import net.gettrillium.trillium.api.SignType;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignInteractEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Sign sign;
    private SignType type;

    public SignInteractEvent(Player player, Sign sign, SignType type) {
        this.player = player;
        this.sign = sign;
        this.type = type;
    }

    public Sign getSign() {
        return this.sign;
    }

    public SignType getSignType() {
        return this.type;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}