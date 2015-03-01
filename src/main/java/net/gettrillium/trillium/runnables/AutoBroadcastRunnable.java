package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.List;

public class AutoBroadcastRunnable implements Runnable {

    private int queue = 0;

    public void run() {
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Broadcast.AUTO_ENABLED)) {
            for (int i : TrilliumAPI.getInstance().getConfig().getIntegerList(Configuration.Broadcast.AUTO_BROADCASTS)) {
                if (i == this.queue) {
                    this.queue = i + 1;
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