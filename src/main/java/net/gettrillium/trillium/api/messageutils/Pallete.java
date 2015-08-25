package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public enum Pallete {

    MAJOR(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.COLOR_PALLETE_MAJOR_COLOR))),
    MINOR(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.COLOR_PALLETE_MINOR_COLOR))),
    HIGHLIGHT(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.COLOR_PALLETE_HIGHLIGHT_COLOR)));

    private String color;

    Pallete(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
