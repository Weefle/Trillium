package me.lordsaad.trillium.events;

import me.lordsaad.trillium.API;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealth implements Listener {
    
    @EventHandler
    public void onhealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (API.isGodMode(p)) {
                event.setCancelled(true);
            }
        }
    }
}
