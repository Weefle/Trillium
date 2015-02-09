package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (!event.getMessage().equalsIgnoreCase("v") || !event.getMessage().equalsIgnoreCase("vanish")) {
            if (Main.plugin.getConfig().getBoolean("AFK.auto unafk")) {
                if (CommandAfk.afklist.contains(p.getUniqueId())) {
                    if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                        CommandAfk.afklist.remove(p.getUniqueId());
                        CommandAfk.afktimer.put(p.getUniqueId(), 0);
                        Message.b(MType.G, "AFK", p.getName() + " is no longer AFK.");
                    }
                }
            }
        }
    }
}
