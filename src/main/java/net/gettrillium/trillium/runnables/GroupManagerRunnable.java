package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.GroupManager;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.player.TrilliumPlayer;

public class GroupManagerRunnable implements Runnable {

    public void run() {
        for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
            GroupManager manager = new GroupManager(p.getProxy());
            manager.refreshPermissions();
        }
    }
}
