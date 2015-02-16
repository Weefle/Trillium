package net.gettillium.trillium.events;

import net.gettillium.trillium.api.TrilliumAPI;
import net.gettillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        TrilliumPlayer p = TrilliumAPI.getPlayer(event.getPlayer().getName());

        if (p.hasPermission("tr.chatcolor")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }
}
