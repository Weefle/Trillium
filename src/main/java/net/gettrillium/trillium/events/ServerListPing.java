package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.regex.Pattern;

public class ServerListPing implements Listener {

    private static final Pattern ONLINE = Pattern.compile("%ONLINEPLAYERS%", Pattern.LITERAL);
    private static final Pattern MAX = Pattern.compile("%MAXPLAYERS%", Pattern.LITERAL);

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String motd = TrilliumAPI.getInstance().getConfig().getString(Configuration.Chat.SERVER_LIST_MOTD);
        motd = ChatColor.translateAlternateColorCodes('&', motd);
        motd = ONLINE.matcher(motd).replaceAll(String.valueOf(event.getNumPlayers()));
        motd = MAX.matcher(motd).replaceAll(String.valueOf(event.getMaxPlayers()));
        event.setMotd(motd);
    }
}
