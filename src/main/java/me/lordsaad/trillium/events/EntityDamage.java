package me.lordsaad.trillium.events;

import me.lordsaad.trillium.commands.CommandGodMode;
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