package me.lordsaad.trillium;

import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Utils {

    public static boolean isdouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isint(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void starttimer(final Player p) {
        new BukkitRunnable() {
            public void run() {
                if (!CommandAfk.afklist.contains(p.getUniqueId())) {
                    if (CommandAfk.afktimer.containsKey(p.getUniqueId())) {
                        CommandAfk.afktimer.put(p.getUniqueId(), CommandAfk.afktimer.get(p.getUniqueId()) + 1);
                    } else {
                        CommandAfk.afktimer.put(p.getUniqueId(), 1);
                    }
                    if (CommandAfk.afktimer.get(p.getUniqueId()) >= Integer.parseInt(Main.plugin.getConfig().getString("AFK.time until idle"))) {

                        if (Main.plugin.getConfig().getBoolean("AFK.kick on afk")) {
                            p.kickPlayer("You idled for too long. Sorry.");
                            Message.b(MType.W, "AFK", p.getName() + " got kicked for idling for too long.");
                            CommandAfk.afktimer.remove(p.getUniqueId());
                            cancel();

                        } else {
                            Message.b(MType.G, "AFK", p.getName() + " is now AFK.");
                            CommandAfk.afklist.add(p.getUniqueId());
                            CommandAfk.afktimer.remove(p.getUniqueId());
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 20, 1);
    }
}
