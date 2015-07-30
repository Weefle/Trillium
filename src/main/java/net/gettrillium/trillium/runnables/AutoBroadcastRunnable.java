package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AutoBroadcastRunnable implements Runnable {

    private int queue = 1;

    public void run() {
        SortedSet<Integer> set = new TreeSet<>();

        for (String i : TrilliumAPI.getInstance().getConfig().getConfigurationSection(Configuration.Broadcast.AUTO_BROADCASTS).getKeys(false)) {
            if (StringUtils.isNumeric(i)) {
                set.add(Integer.parseInt(i));

                if (Integer.parseInt(i) == this.queue) {
                    if (set.last() == this.queue) {
                        this.queue = set.first();
                    } else {
                        this.queue++;
                    }
                }
            }
        }

        List<String> list = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Broadcast.AUTO_BROADCASTS + "." + this.queue);
        for (String s : list) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            Bukkit.broadcastMessage(s);
        }
    }
}
