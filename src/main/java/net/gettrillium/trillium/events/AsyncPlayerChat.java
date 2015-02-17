package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
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
