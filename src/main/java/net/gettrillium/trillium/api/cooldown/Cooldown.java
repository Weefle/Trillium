package net.gettrillium.trillium.api.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Cooldown {

    private static Table<UUID, CooldownType, Long> cooldown = HashBasedTable.create();

    public static void setCooldown(Player p, CooldownType type) {
        long time = 0;
        if (type.getCooldownType().equalsIgnoreCase("tp")) {
            time = System.currentTimeMillis() + (long) ((Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.COOLDOWN_TP)) / 20) * 1000);
        }
        cooldown.put(p.getUniqueId(), type, time);
    }

    public static boolean hasCooldown(Player p, CooldownType type) {
        if (cooldown.contains(p.getUniqueId(), type)) {
            if (cooldown.get(p.getUniqueId(), type) - System.currentTimeMillis() > 0) {
                return true;
            } else {
                cooldown.remove(p.getUniqueId(), type);
                return false;
            }
        } else {
            return false;
        }
    }

    public static long getTime(Player p, CooldownType type) {
        if (hasCooldown(p, type)) {
            return cooldown.get(p.getUniqueId(), type) - System.currentTimeMillis();
        } else {
            return 0;
        }
    }
}