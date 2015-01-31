package me.lordsaad.trillium.gamemode;

import me.lordsaad.trillium.godmode.CommandGodMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (CommandGodMode.godmodeusers.contains(p.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}