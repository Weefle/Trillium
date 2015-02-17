package net.gettillium.trillium.events;

import net.gettillium.trillium.api.Configuration;
import net.gettillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPing implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String s = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SERVER_LIST_MOTD);
        s = ChatColor.translateAlternateColorCodes('&', s);
        event.setMotd(s);
    }
}
