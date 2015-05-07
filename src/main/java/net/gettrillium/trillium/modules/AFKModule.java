package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AFKModule extends TrilliumModule {

    @Command(command = "afk", description = "Indicate that you are away from your keyboard.", usage = "/afk")
    public void afk(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player((Player) cs);
            if (player.hasPermission(Permission.Afk.USE)) {
                player.toggleAfk();
            } else {
                new Message("AFK", Error.NO_PERMISSION).to(player);
            }
        } else {
            new Message("AFK", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        player.active();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            TrilliumPlayer player = player((Player) event.getEntity());
            player.active();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        player.active();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        player.active();
    }
}
