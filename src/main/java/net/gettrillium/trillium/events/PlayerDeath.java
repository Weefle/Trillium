package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Ability.AUTO_RESPAWN)) {
            p.spigot().respawn();
        }

        if (!TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Chat.ENABLE_DEATH_MESSAGES)) {
            event.setDeathMessage(null);
        }
    }
}
