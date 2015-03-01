package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AFKModule extends TrilliumModule {

    public AFKModule() {
        super("afk");
    }

    @Command(command = "afk", description = "Indicate that you are away from your keyboard.", usage = "/afk")
    public void afk(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            TrilliumPlayer player = player((Player) sender);
            if (player.hasPermission(Permission.Afk.USE)) {
                player.toggleAfk();
                player.active();
            } else {
                Message.e(sender, "AFK", Crit.P);
            }
        } else {
            Message.e(sender, "AFK", Crit.C);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            if (!player.isVanished()) {
                player.toggleAfk();
                player.active();
            }
        }
        player.active();
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            if (!player.isVanished()) {
                player.toggleAfk();
                player.active();
            }
        }
        player.active();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            TrilliumPlayer player = player((Player) event.getEntity());
            if (player.isAfk()) {
                if (!player.isVanished()) {
                    player.toggleAfk();
                    player.active();
                }
            }
            player.active();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            if (!player.isVanished()) {
                player.toggleAfk();
                player.active();
            }
        }
        player.active();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            if (!player.isVanished()) {
                player.toggleAfk();
            }
        }
        player.active();
    }
}
