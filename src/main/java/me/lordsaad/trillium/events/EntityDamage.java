package me.lordsaad.trillium.events;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

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
            if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                if (TrilliumAPI.getInstance().getConfig().getBoolean("Vanish.god mode")) {
                    event.setCancelled(true);
                }
            }
            if (CommandAfk.afklist.contains(p.getUniqueId())) {
                if (TrilliumAPI.getInstance().getConfig().getBoolean("AFK.god mode")) {
                    event.setCancelled(true);
                } else {
                    if (TrilliumAPI.getInstance().getConfig().getBoolean("AFK.auto unafk")) {
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
}