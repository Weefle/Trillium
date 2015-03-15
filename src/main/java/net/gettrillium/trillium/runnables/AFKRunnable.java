package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.messageutils.Type;

import java.util.ArrayList;
import java.util.List;

public class AFKRunnable implements Runnable {

    public void run() {
        List<TrilliumPlayer> toKick = new ArrayList<>();

        for (TrilliumPlayer player : TrilliumAPI.getOnlinePlayers()) {
            if (player.isAfk()) {
                continue;
            }

            if (player.isVanished()) {
                continue;
            }

            if (player.getLastActive() < System.currentTimeMillis()) {
                if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_KICK)) {
                    toKick.add(player);
                } else {
                    player.toggleAfk();
                }
            }
        }

        for (TrilliumPlayer player : toKick) {
            player.getProxy().kickPlayer("You idled for too long.");
            Message.broadcast(Type.WARNING, "AFK", player.getProxy().getName() + " got kicked for idling for too long.");

        }
    }
}