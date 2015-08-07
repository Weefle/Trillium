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
    private boolean cancelled = false;
    private CommandSender sender;

    public SignInteractEvent(Player player, Sign sign, String command, CommandSender sender) {
        this.player = player;
        this.sign = sign;
        this.command = command;
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getBaseCommand() {
        return command;
    }

    public String getCommand() {
        return command + ' ' + Utils.commandBlockify(sign.getLine(1), player);
    }

    public Sign getSign() {
        return sign;
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
