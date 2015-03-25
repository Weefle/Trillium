package net.gettrillium.trillium.api.messageutils;

import org.bukkit.ChatColor;

public enum Type {
    WARNING(ChatColor.RED + ""),
    GOOD(ChatColor.GREEN + ""),
    GENERIC(ChatColor.BLUE + "");

    private String prefix;

    private Type(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
