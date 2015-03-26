package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
        format = error.getError();
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COMMAND%", command);

    }

    Message(String command, Error error, String extra) {
        format = error.getError();
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COMMAND", command);
        format = format.replace("%USAGE%", extra);
        format = format.replace("%PLAYER%", extra);

    }

    public void broadcast() {
        Bukkit.broadcastMessage(format);
    }

    public void message(CommandSender to) {
        to.sendMessage(format);
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
        WRONG_ARGUMENTS(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.WRONG_ARGUMENTS)));

        private String error;

        Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}