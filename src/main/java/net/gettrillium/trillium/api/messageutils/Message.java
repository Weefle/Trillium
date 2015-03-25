package net.gettrillium.trillium.api.messageutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Message {

    WARNING(ChatColor.RED + ""),
    GOOD(ChatColor.GREEN + ""),
    GENERIC(ChatColor.BLUE + "");

    private Message type;
    private String prefix;

    private Message(Message type) {
        type = type;
        prefix = type.prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void broadcast(String tag, String msg) {
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + type.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + msg);
    }

    public void message(CommandSender cs, String tag, boolean dontFlip, String msg) {
        if (dontFlip) {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + type.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + msg);
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + type.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " << " + ChatColor.GRAY + msg);
        }
    }

    public void error(String tag, CommandSender cs, String player) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + Type.WARNING.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + player + " is either offline or does not exist.");
    }

    public void error(String tag, CommandSender cs) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Message.WARNING.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "You can't do that you silly console.");
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Type.WARNING.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "You don't have permission to do that.");
        }
    }

    public void error(CommandSender cs, String tag, boolean fewArgs, String usage) {
        if (fewArgs) {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Message.WARNING.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "Too few arguments. " + usage);
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Message.WARNING.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "Wrong arguments. " + usage);

        }
    }
}