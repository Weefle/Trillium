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
    private SortedSet<Integer> set = new TreeSet<>();

    public void run() {
        for (String i : TrilliumAPI.getInstance().getConfig().getConfigurationSection(Configuration.Broadcast.AUTO_BROADCASTS).getKeys(false)) {
            if (StringUtils.isNumeric(i)) {

                set.add(Integer.parseInt(i));

                if (Integer.parseInt(i) == this.queue) {
                    if (set.last() == this.queue) {
                        this.queue = 1;
                    } else {
                        this.queue = Integer.parseInt(i) + 1;
                    }
                    List<String> list = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Broadcast.AUTO_BROADCASTS + "." + i);
                    for (String s : list) {
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        Bukkit.broadcastMessage(s);
                    }
                }
            }
        }
    }
}
