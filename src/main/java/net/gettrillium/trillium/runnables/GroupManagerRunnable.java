package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GroupManagerRunnable implements Runnable {

    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            new GroupManager(p).refreshPermissions();
        }
    }
}
