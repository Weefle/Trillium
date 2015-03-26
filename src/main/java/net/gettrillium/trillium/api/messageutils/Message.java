package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    private String format;

    Message(Mood mood, String command, String message) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COLOR%", mood.getColor());
        format = format.replace("%COMMAND", command);
        format = format.replace("%MESSAGE", ChatColor.translateAlternateColorCodes('&', message));
    }

    Message(String command, Error error) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COMMAND", command);

    }

    Message(String command, Error error, String player) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COMMAND", command);

    }

    public void broadcast() {
        Bukkit.broadcastMessage(format);
    }

    public void messageSender(CommandSender cs, String prefix, boolean dontFlip, String msg) {
        if (dontFlip) {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + type.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + msg);
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + type.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " << " + ChatColor.GRAY + msg);
        }
    }

    public void error(String prefix, CommandSender cs, String player) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + Type.WARNING.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + player + " is either offline or does not exist.");
    }

    public void error(String Stringprefix, CommandSender cs) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Message.WARNING.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "You can't do that you silly console.");
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Type.WARNING.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "You don't have permission to do that.");
        }
    }

    public void error(CommandSender cs, String prefix, boolean fewArgs, String usage) {
        if (fewArgs) {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Message.WARNING.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "Too few arguments. " + usage);
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "[" + Message.WARNING.getPrefix() + prefix + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "Wrong arguments. " + usage);

        }
    }

    public enum Mood {
        ERROR(ChatColor.RED + ""), GOOD(ChatColor.GREEN + ""), GENERIC(ChatColor.BLUE + "");

        private String color;

        Mood(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    public enum Error {
        INVALID_PLAYER(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.INVALID_PLAYER))),
        NO_PERMISSION(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.NO_PERMISSION))),
        CONSOLE_NOT_ALLOWED(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.CONSOLE_NOT_ALLOWED))),
        TOO_FEW_ARGUMENTS(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.TOO_FEW_ARGUMENTS))),
        WRONG_ARGUMENTS(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.WRONG_ARGUMENTS))),

        private String error;

        Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}