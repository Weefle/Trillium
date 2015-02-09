package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.commands.CommandVanish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (Main.plugin.getConfig().getBoolean("Vanish.prevent_item_pickup/drop")) {
            if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
