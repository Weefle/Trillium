package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration.PluginMessages;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public enum Error {
    INVALID_PLAYER(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.INVALID_PLAYER))),
    NO_PERMISSION(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.NO_PERMISSION))),
    CONSOLE_NOT_ALLOWED(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.CONSOLE_NOT_ALLOWED))),
    TOO_FEW_ARGUMENTS(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.TOO_FEW_ARGUMENTS))),
    WRONG_ARGUMENTS(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(PluginMessages.WRONG_ARGUMENTS)));

    private final String error;

    Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
