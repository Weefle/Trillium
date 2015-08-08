package net.gettrillium.trillium.api.messageutils;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;

public class Pallete {

    public static ChatColor getMinor() {
        return ChatColor.getByChar(TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.PALLETE_MINOR));
    }

    public static ChatColor getMajor() {
        return ChatColor.getByChar(TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.PALLETE_MAJOR));
    }

    public static ChatColor getHighlight() {
        return ChatColor.getByChar(TrilliumAPI.getInstance().getConfig().getString(Configuration.PluginMessages.PALLETE_HIGHLIGHT));
    }
}
