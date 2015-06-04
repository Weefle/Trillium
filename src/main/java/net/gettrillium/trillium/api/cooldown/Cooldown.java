package net.gettrillium.trillium.api.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cooldown {

    private static Table<UUID, CooldownType, Long> cooldown = HashBasedTable.create();

    public static void setCooldown(Player p, CooldownType type) {
        cooldown.put(p.getUniqueId(), type, System.currentTimeMillis());
    }

    public static boolean hasCooldown(Player p, CooldownType type) {
        if (cooldown.contains(p.getUniqueId(), type)) {
            Bukkit.broadcastMessage("COMPARE: " + (System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)));
            Bukkit.broadcastMessage("> WITH: " + (type.getTimeInTicks() / 20));
            Bukkit.broadcastMessage("HAS COOLDOWN: " + (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) > (type.getTimeInTicks() / 20)));
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) > (type.getTimeInTicks() / 20)) {
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
            return Utils.timeToString((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)));
        } else {
            return null;
        }
    }
}