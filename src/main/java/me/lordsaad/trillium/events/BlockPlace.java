package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if (Main.plugin.getConfig().getBoolean("AFK.auto unafk")) {
            if (CommandAfk.afklist.contains(p.getUniqueId())) {
                if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                    CommandAfk.afklist.remove(p.getUniqueId());
                    Utils.starttimer(p);
                    Message.b(MType.G, "AFK", p.getName() + " is no longer AFK.");
                }
            }
        }
    }
}
