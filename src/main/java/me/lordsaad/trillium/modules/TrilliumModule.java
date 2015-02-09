package me.lordsaad.trillium.modules;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public abstract class TrilliumModule implements Listener {
    private ConfigurationSection config;
    
    protected ConfigurationSection getConfig() {
        return config;
    }
}
