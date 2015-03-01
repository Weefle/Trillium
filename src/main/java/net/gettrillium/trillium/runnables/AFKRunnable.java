package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;

import java.util.ArrayList;
import java.util.List;

public class AFKRunnable implements Runnable {

    public void run() {
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            List<TrilliumPlayer> toKick = new ArrayList<>();

            for (TrilliumPlayer player : TrilliumAPI.getOnlinePlayers()) {
                if (player.isAfk()) {
                    continue;
                }
                if (player.isVanished()) {
                    continue;
                }

                if (player.getInactiveTime() >= Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Afk.AUTO_AFK_TIME))) {
                    if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_KICK)) {
                        toKick.add(player);
                    } else {
                        player.toggleAfk();
                        player.active();
                    }
                }
            }

            for (TrilliumPlayer player : toKick) {
                player.getProxy().kickPlayer("You idled for too long.");
                Message.b(MType.W, "AFK", player.getProxy().getName() + " got kicked for idling for too long.");
            }
        }
    }
}