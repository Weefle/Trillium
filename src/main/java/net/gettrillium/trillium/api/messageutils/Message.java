package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    private String format;

    public Message(Mood mood, String prefix, String message) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%PREFIX%", prefix);
        format = format.replace("%MESSAGE%", message);
        format = format.replace("%COLOR%", mood.getColor());
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String prefix, Error error) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = format.replace("%MESSAGE%", error.getError());
        format = format.replace("%PREFIX%", prefix);
        format = format.replace("%COLOR%", Mood.BAD.getColor());
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String prefix, Error error, String extra) {
        format = TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.FORMAT);
        format = format.replace("%MESSAGE%", error.getError());
        format = format.replace("%PREFIX%", prefix);
        format = format.replace("%USAGE%", extra);
        format = format.replace("%PLAYER%", extra);
        format = format.replace("%COLOR%", Mood.BAD.getColor());
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public void broadcast() {
        Bukkit.broadcastMessage(format);
    }

    public void to(CommandSender to) {
        to.sendMessage(format);
    }

    public void to(TrilliumPlayer to) {
        to.getProxy().sendMessage(format);
    }

    public String asString() {
        return format;
    }
}