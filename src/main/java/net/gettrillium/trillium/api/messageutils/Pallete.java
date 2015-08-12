package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public enum Pallete {

    MAJOR(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.PALLETE_MAJOR))),
    MINOR(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.PALLETE_MINOR))),
    HIGHLIGHT(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.PALLETE_HIGHLIGHT)));

    private String color;

    Pallete(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
