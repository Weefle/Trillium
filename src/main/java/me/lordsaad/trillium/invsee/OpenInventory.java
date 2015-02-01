package me.lordsaad.trillium.invsee;

import me.lordsaad.trillium.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class OpenInventory implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player p = (Player) event.getPlayer();
            if (CommandInvsee.invusers.contains(p.getUniqueId())) {
                if (event.getInventory().getType() == InventoryType.PLAYER) {
                    Inventory inv = Bukkit.createInventory(event.getPlayer(), InventoryType.PLAYER, "Original Inventory");
                    inv.setContents(Utils.originalinventory(p));
                    p.openInventory(inv);
                }
            }
        }
    }
}
