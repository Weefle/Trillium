package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration.PluginMessages;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public enum Mood {
    BAD(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.COLORS_BAD_MESSAGE))),
    GOOD(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.COLORS_GOOD_MESSAGE))),
    NEUTRAL(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.COLORS_NEUTRAL_MESSAGE)));

    private final String color;

    Mood(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
