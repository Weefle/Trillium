package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    private String format;

    public Message(Mood mood, String command, String message) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COLOR%", mood.getColor());
        format = format.replace("%COMMAND", command);
        format = format.replace("%MESSAGE", ChatColor.translateAlternateColorCodes('&', message));
    }

    public Message(String command, Error error) {
        format = error.getError();
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COMMAND%", command);

    }

    public Message(String command, Error error, String extra) {
        format = error.getError();
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%COMMAND", command);
        format = format.replace("%USAGE%", extra);
        format = format.replace("%PLAYER%", extra);

    }

    public void broadcast() {
        Bukkit.broadcastMessage(format);
    }

    public void to(CommandSender to) {
        to.sendMessage(format);
    }
}