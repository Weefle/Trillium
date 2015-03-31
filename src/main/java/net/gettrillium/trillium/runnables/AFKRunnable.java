package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;

import java.util.ArrayList;
import java.util.List;

public class AFKRunnable implements Runnable {

    public void run() {
        List<TrilliumPlayer> toKick = new ArrayList<>();
        List<TrilliumPlayer> toAfk = new ArrayList<>();

        for (TrilliumPlayer player : TrilliumAPI.getOnlinePlayers()) {
            if (!player.isAfk()) {
                continue;
            }

            if (!player.isVanished()) {
                continue;
            }

            if (player.getLastActive() < System.currentTimeMillis()) {
                if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_KICK)) {
                    toKick.add(player);
                } else {
                    toAfk.add(player);
                }
            }
        }

        for (TrilliumPlayer player : toKick) {
            player.getProxy().kickPlayer("You idled for too long.");
            new Message(Mood.BAD, "AFK", player.getName() + " got kicked for idling for too long.").broadcast();
        }

        for (TrilliumPlayer player : toAfk) {
            player.toggleAfk();
        }
    }
}