package net.gettrillium.trillium.api.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Cooldown {

    private static Table<UUID, CooldownType, Long> cooldown = HashBasedTable.create();

    public static void setCooldown(Player p, CooldownType type) {
        cooldown.put(p.getUniqueId(), type, System.currentTimeMillis());
    }

    public static boolean hasCooldown(Player p, CooldownType type) {
        if (cooldown.contains(p.getUniqueId(), type)) {
            Bukkit.broadcastMessage("GET: " + cooldown.get(p.getUniqueId(), type));
            Bukkit.broadcastMessage("CURRENT MILLIS: " + System.currentTimeMillis());
            Bukkit.broadcastMessage("SUBTRACTED: " + (System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)));
            Bukkit.broadcastMessage("IN SECONDS: " + (System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) / 1000.0);
            Bukkit.broadcastMessage("> WITH: " + (type.getTimeInTicks() / 20));
            Bukkit.broadcastMessage("HAS COOLDOWN: " + (((System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) / 1000.0) > (type.getTimeInTicks() / 20)));
            if (((System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) / 1000.0) > (type.getTimeInTicks() / 20)) {
                return true;
            } else {
                cooldown.remove(p.getUniqueId(), type);
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getTime(Player p, CooldownType type) {
        if (hasCooldown(p, type)) {
            return Utils.timeToString((int) ((System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) / 1000.0));
        } else {
            return null;
        }
    }
}