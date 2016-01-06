package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration.PluginMessages;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Message {

    private static final Pattern PREFIX = Pattern.compile("%PREFIX%", Pattern.LITERAL);
    private static final Pattern MESSAGE = Pattern.compile("%MESSAGE%", Pattern.LITERAL);
    private static final Pattern MOOD_COLOR = Pattern.compile("%MOOD_COLOR%", Pattern.LITERAL);
    private static final Pattern MAJOR = Pattern.compile("%MAJOR%", Pattern.LITERAL);
    private static final Pattern MINOR = Pattern.compile("%MINOR%", Pattern.LITERAL);
    private static final Pattern HIGHLIGHT = Pattern.compile("%HIGHLIGHT%", Pattern.LITERAL);
    private static final Pattern EXTRA = Pattern.compile("%EXTRA%", Pattern.LITERAL);
    private static final Pattern USERNAME = Pattern.compile("%USERNAME%", Pattern.LITERAL);
    private String format;

    public Message(Mood mood, String prefix, String message) {
        format = TrilliumAPI.getInstance().getConfig().getString(PluginMessages.FORMAT);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = PREFIX.matcher(format).replaceAll(prefix);
        format = MESSAGE.matcher(format).replaceAll(message);
        format = MOOD_COLOR.matcher(format).replaceAll(mood.getColor());
        format = MAJOR.matcher(format).replaceAll(Pallete.MAJOR.getColor());
        format = MINOR.matcher(format).replaceAll(Pallete.MINOR.getColor());
        format = HIGHLIGHT.matcher(format).replaceAll(Pallete.HIGHLIGHT.getColor());
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String prefix, Error error) {
        this(Mood.BAD, prefix, error.getError());
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String prefix, Error error, String extra) {
        this(Mood.BAD, prefix, error.getError());
        format = EXTRA.matcher(format).replaceAll(extra);
        format = USERNAME.matcher(format).replaceAll(extra);
        format = ChatColor.translateAlternateColorCodes('&', format);
    }

    public Message(String colorize) {
        if (TrilliumAPI.getInstance().getConfig().getBoolean(PluginMessages.AUTO_HIGHLIGHT_PLAYER_NAMES)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (colorize.contains(p.getName())) {
                    String inject = "%HIGHLIGHT%" + p.getName() + "%MAJOR%";
                    colorize = colorize.replace(p.getName(), inject);
                }
            }
        }
        colorize = ChatColor.translateAlternateColorCodes('&', colorize);
        colorize = MAJOR.matcher(colorize).replaceAll(Pallete.MAJOR.getColor());
        colorize = MINOR.matcher(colorize).replaceAll(Pallete.MINOR.getColor());
        colorize = HIGHLIGHT.matcher(colorize).replaceAll(Pallete.HIGHLIGHT.getColor());
        colorize = ChatColor.translateAlternateColorCodes('&', colorize);
        format = colorize;
    }

    public void broadcast() {
        Bukkit.broadcastMessage(format);
    }

    public void broadcast(String perm) {
        Bukkit.broadcast(format, perm);
    }

    public void to(CommandSender to) {
        to.sendMessage(format);
    }

    public void to(TrilliumPlayer to) {
        to.getPlayer().sendMessage(format);
    }

    public String asString() {
        return format;
    }
}
