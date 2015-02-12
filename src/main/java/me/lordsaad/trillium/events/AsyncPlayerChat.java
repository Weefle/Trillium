package me.lordsaad.trillium.events;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat extends TrilliumModule implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (getConfig().getBoolean(Configuration.Ability.AUTO_UNAFK)) {
            if (p.isAfk()) {
                if (!p.isVanished()) {
                    CommandAfk.afklist.remove(p.getProxy().getUniqueId());
                    CommandAfk.afktimer.put(p.getProxy().getUniqueId(), 0);
                    Message.b(MType.G, "AFK", p.getProxy().getName() + " is no longer AFK.");
                }
            }
        }

        if (p.hasPermission("tr.chatcolor")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }
}
