package net.gettrillium.trillium.api.messageutils;

import org.bukkit.ChatColor;

public enum Mood {
    ERROR(ChatColor.RED + ""),
    GOOD(ChatColor.GREEN + ""),
    GENERIC(ChatColor.BLUE + "");

    private String color;

    Mood(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}