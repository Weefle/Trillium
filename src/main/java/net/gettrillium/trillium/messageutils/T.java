package net.gettrillium.trillium.messageutils;

import org.bukkit.ChatColor;

public enum T {
    W(ChatColor.RED + ""),
    G(ChatColor.GREEN + ""),
    R(ChatColor.BLUE + "");

    private String prefix;

    private T(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
