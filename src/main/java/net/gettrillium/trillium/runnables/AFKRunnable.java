package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AFKRunnable extends BukkitRunnable {

    @Override
    public void run() {
        List<TrilliumPlayer> toKick = new ArrayList<>();

        if (!TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_KICK)) {
            return;
        }

        for (TrilliumPlayer player : TrilliumAPI.getOnlinePlayers()) {
            if (!player.isAfk()) {
                continue;
            }

            if (player.getProxy().isOp()) {
                continue;
            }

            if (player.isVanished()) {
                continue;
            }

            if (player.getLastActive() >= System.currentTimeMillis()) {
                continue;
            }

            toKick.add(player);
        }

        for (TrilliumPlayer player : toKick) {
            player.getProxy().kickPlayer("You idled for too long.");
            new Message(Mood.BAD, "AFK", player.getName() + " got kicked for idling for too long.").broadcast();
        }
    }
}
