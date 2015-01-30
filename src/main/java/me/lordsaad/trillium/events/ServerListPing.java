package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPing implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String s = Main.plugin.getConfig().getString("Server List Motd");
        s = ChatColor.translateAlternateColorCodes('&', s);
        event.setMotd(s);
    }
}
