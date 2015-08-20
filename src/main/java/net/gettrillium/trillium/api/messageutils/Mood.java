package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public enum Mood {

    BAD(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.BAD_COLOR))),
    GOOD(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.GOOD_COLOR))),
    NEUTRAL(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.NEUTRAL_COLOR)));

    private String color;

    Mood(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}