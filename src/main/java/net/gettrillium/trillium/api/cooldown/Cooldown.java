package net.gettrillium.trillium.api.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import net.gettrillium.trillium.api.Utils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Cooldown {

    private static Table<UUID, CooldownType, Long> cooldown = HashBasedTable.create();

    public static void setCooldown(Player p, CooldownType type, boolean toConfig) {
        if (!toConfig) {
            cooldown.put(p.getUniqueId(), type, System.currentTimeMillis());
        } else {
            TrilliumPlayer player = TrilliumAPI.getPlayer(p);
            player.setCooldown(type.name());
        }
    }

    public static boolean hasCooldown(Player p, CooldownType type) {
        long cooldownSecs = type.getTimeInTicks() / 20;

        if (!isSavedToConfig(p, type)) {
            if (cooldown.contains(p.getUniqueId(), type)) {
                long startMillis = cooldown.get(p.getUniqueId(), type);
                double elapsedSecs = (System.currentTimeMillis() - startMillis) / 1000.0;
                if (elapsedSecs < cooldownSecs) {
                    return true;
                } else {
                    cooldown.remove(p.getUniqueId(), type);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            TrilliumPlayer player = TrilliumAPI.getPlayer(p);
            if (player.hasCooldown(type.name())) {
                long startMillis = player.getCooldown(type.name());
                double elapsedSecs = (System.currentTimeMillis() - startMillis) / 1000.0;
                if (elapsedSecs < cooldownSecs) {
                    return true;
                } else {
                    player.removeCooldown(type.name());
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean isSavedToConfig(Player p, CooldownType type) {
        if (cooldown.contains(p.getUniqueId(), type)) {
            return false;
        } else {
            TrilliumPlayer player = TrilliumAPI.getPlayer(p);
            return player.hasCooldown(type.name());
        }
    }

    public static String getTime(Player p, CooldownType type) {
        if (hasCooldown(p, type)) {
            TrilliumPlayer player = TrilliumAPI.getPlayer(p);
            Double d;
            if (player.hasCooldown(type.name())) {
                d = (type.getTimeInTicks() / 20) - ((System.currentTimeMillis() - player.getCooldown(type.name())) / 1000.0);
            } else {
                d = (type.getTimeInTicks() / 20) - ((System.currentTimeMillis() - cooldown.get(p.getUniqueId(), type)) / 1000.0);
            }
            return Utils.timeToString(d.intValue() * 20);
        } else {
            return null;
        }
    }
}