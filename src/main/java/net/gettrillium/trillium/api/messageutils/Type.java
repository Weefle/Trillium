package net.gettrillium.trillium.api.messageutils;

import org.bukkit.ChatColor;

public enum Type {
    WARNING(ChatColor.RED + ""),
    GOOD(ChatColor.GREEN + ""),
    GENERIC(ChatColor.BLUE + "");

    private String prefix;

    Type(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
