package me.lordsaad.trillium;

import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AfkRunnable implements Runnable {

    @Override
    public void run() {
        //if (Main.plugin.getConfig().getBoolean("AFK.auto afk.enabled")) {
            for (UUID uuid : CommandAfk.afktimer.keySet()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    if (!CommandAfk.afklist.contains(p.getUniqueId())) {
                        if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                            if (!p.hasPermission("tr.afk.exempt")) {
                                CommandAfk.afktimer.put(p.getUniqueId(), CommandAfk.afktimer.get(p.getUniqueId()) + 1);

                                if (CommandAfk.afktimer.get(p.getUniqueId()) >= Main.plugin.getConfig().getInt("AFK.time until idle")) {

                                    if (Main.plugin.getConfig().getBoolean("AFK.kick on afk")) {
                                        p.kickPlayer("You idled for too long. Sorry.");
                                        Message.b(MType.W, "AFK", p.getName() + " got kicked for idling for too long.");
                                        CommandAfk.afklist.remove(p.getUniqueId());
                                        CommandAfk.afktimer.remove(p.getUniqueId());

                                    } else {
                                        Message.b(MType.G, "AFK", p.getName() + " is now AFK.");
                                        CommandAfk.afklist.add(p.getUniqueId());
                                        CommandAfk.afktimer.remove(p.getUniqueId());
                                    }
                                }
                            }
                       // }
                    }
                } else {
                    CommandAfk.afktimer.remove(uuid);
                }
            }
        }
    }
}