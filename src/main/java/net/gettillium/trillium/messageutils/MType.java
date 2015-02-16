package net.gettillium.trillium.messageutils;

import org.bukkit.ChatColor;

public enum MType {
    W(ChatColor.DARK_GRAY + "[" + ChatColor.RED),
    G(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN),
    R(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE);

    private String prefix;

    private MType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
