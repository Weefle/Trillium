package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPing implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String motd = TrilliumAPI.getInstance().getConfig().getString(Configuration.Chat.SERVER_LIST_MOTD);
        motd = ChatColor.translateAlternateColorCodes('&', motd);
        motd.replace("[onlineplayers]", String.valueOf(event.getNumPlayers()));
        motd.replace("[maxplayers]", String.valueOf(event.getMaxPlayers()));
        event.setMotd(motd);
    }
}
