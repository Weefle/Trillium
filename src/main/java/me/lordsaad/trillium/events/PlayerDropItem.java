package me.lordsaad.trillium.events;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandVanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (TrilliumAPI.getInstance().getConfig().getBoolean("Vanish.prevent item pickup/drop")) {
            if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
