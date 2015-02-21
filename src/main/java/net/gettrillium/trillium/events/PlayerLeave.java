package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        String quitMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.LEAVEMESSAGE));
        quitMessage = quitMessage.replace("[USERNAME]", p.getName());
        event.setQuitMessage(quitMessage);
    }
}
