package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration.PluginMessages;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public enum Mood {
    BAD(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.BAD_COLOR))),
    GOOD(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.GOOD_COLOR))),
    NEUTRAL(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.NEUTRAL_COLOR)));

    private final String color;

    Mood(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
