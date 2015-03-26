package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

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
