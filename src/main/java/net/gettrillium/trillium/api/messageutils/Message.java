package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration.PluginMessages;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    private String format;

    public Message(Mood mood, String prefix, String message) {
        format = TrilliumAPI.getInstance().getConfig().getString(PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%PREFIX%", prefix);
        format = format.replace("%MESSAGE%", message);
        format = format.replace("%MOOD_COLOR%", mood.getColor());
        format = format.replace("%MAJOR%", Pallete.getMajor() + "");
        format = format.replace("%MINOR%", Pallete.getMinor() + "");
        format = format.replace("%HIGHLIGHT%", Pallete.getHighlight() + "");
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String prefix, Error error) {
        this(Mood.BAD, prefix, error.getError());
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String prefix, Error error, String extra) {
        this(Mood.BAD, prefix, error.getError());
        format = format.replace("%EXTRA%", extra);
        format = format.replace("%USERNAME%", extra);
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
