package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.commands.CommandVanish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem implements Listener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
            if (Main.plugin.getConfig().getBoolean("Vanish.prevent item pickup/drop")) {
                event.setCancelled(true);
            }
        }
    }
}
