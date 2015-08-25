package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.Configuration.Broadcast;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AutoBroadcastRunnable extends BukkitRunnable {
    private int queue = 1;
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    public AutoBroadcastRunnable() {
        for (String i : TrilliumAPI.getInstance().getConfig().getConfigurationSection(Broadcast.AUTO_BROADCAST_BROADCASTS).getKeys(false)) {
            if (!StringUtils.isNumeric(i)) {
                continue;
            }

            int num = Integer.parseInt(i);

            if (num > max) {
                max = num;
            }

            if (num < min) {
                min = num;
            }
        }

        queue = min;
    }

    @Override
    public void run() {
        List<String> list = TrilliumAPI.getInstance().getConfig().getStringList(Broadcast.AUTO_BROADCAST_BROADCASTS + '.' + queue);

        for (String s : list) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            Bukkit.broadcastMessage(s);
        }

        queue++;

        if (queue > max) {
            queue = min;
        }
    }
}
