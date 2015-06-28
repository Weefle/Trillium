package net.gettrillium.trillium.api.events;

import net.gettrillium.trillium.api.Utils;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignInteractEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Sign sign;
    private String command;
    private boolean cancelled;
    private CommandSender sender;

    public SignInteractEvent(Player player, Sign sign, String command, CommandSender sender) {
        this.player = player;
        this.sign = sign;
        this.command = command;
        this.sender = sender;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getBaseCommand() {
        return this.command;
    }

    public String getCommand() {
        return this.command + " " + Utils.commandBlockify(sign.getLine(1), player);
    }

    public Sign getSign() {
        return this.sign;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}