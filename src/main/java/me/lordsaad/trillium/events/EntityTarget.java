package me.lordsaad.trillium.events;

import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandVanish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player p = (Player) event.getTarget();
            if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                event.setCancelled(true);
            }
            if (CommandGodMode.godmodeusers.contains(p.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
