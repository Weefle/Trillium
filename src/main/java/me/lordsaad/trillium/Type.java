package me.lordsaad.trillium;

import org.bukkit.ChatColor;

public enum Type {
    W(ChatColor.DARK_GRAY + "[" + ChatColor.RED),
    G(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN),
    R(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE);

    private String prefix;

    private Type(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
