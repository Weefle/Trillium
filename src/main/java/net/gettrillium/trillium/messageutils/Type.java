package net.gettrillium.trillium.messageutils;

import org.bukkit.ChatColor;

public enum Type {
    W(ChatColor.RED + ""),
    G(ChatColor.GREEN + ""),
    R(ChatColor.BLUE + "");

    private String prefix;

    private Type(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
