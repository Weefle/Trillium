package me.lordsaad.trillium.api;

import me.lordsaad.trillium.api.player.TrilliumPlayer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class TrilliumModule implements Listener {
    private ConfigurationSection config;
    
    protected ConfigurationSection getConfig() {
        return config;
    }
    
    protected TrilliumPlayer player(Player player) {
        return TrilliumAPI.getPlayer(player.getName());
    }
    
    protected void register() {
        
    }
}
