package net.gettillium.trillium.events;

import net.gettillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        String m1 = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString("leave.message"));
        m1 = m1.replace("[USERNAME]", p.getName());
        event.setQuitMessage(m1);
    }
}
