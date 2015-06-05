package net.gettrillium.trillium.api.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.Utils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Cooldown {

    // TODO: save cooldowns to player configs (kits and such)

    private static Table<UUID, CooldownType, Long> cooldown = HashBasedTable.create();

    public static void setCooldown(Player p, CooldownType type) {
        cooldown.put(p.getUniqueId(), type, System.currentTimeMillis());
    }

    public static boolean hasCooldown(Player p, CooldownType type) {
        if (cooldown.contains(p.getUniqueId(), type)) {
            long startMillis = cooldown.get(p.getUniqueId(), type);
            double elapsedSecs = (System.currentTimeMillis() - startMillis) / 1000.0;
            long cooldownSecs = type.getTimeInTicks() / 20;
            if (elapsedSecs < cooldownSecs) {
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
            Double d = (type.getTimeInTicks() / 20) - ((System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) / 1000.0);
            return Utils.timeToString(d.intValue() * 20);
        } else {
            return null;
        }
    }
}