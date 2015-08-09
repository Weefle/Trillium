package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission.Afk;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.TrilliumPlayer;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AFKModule extends TrilliumModule {

    @Command(name = "AFK",
            command = "afk",
            description = "Indicate that you are away from your keyboard.",
            usage = "/afk",
            permissions = {Afk.USE})
    public void afk(CommandSender cs, String[] args) {
        String cmd = "afk";
        if (cs instanceof Player) {
            TrilliumPlayer player = player((Player) cs);
            if (player.hasPermission(Afk.USE)) {
                player.toggleAfk();
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (TrilliumAPI.isLoaded(event.getPlayer())) {
            TrilliumPlayer player = player(event.getPlayer());
            player.active();
        } else {
            TrilliumAPI.loadPlayers();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (TrilliumAPI.isLoaded(event.getPlayer())) {
            TrilliumPlayer player = player(event.getPlayer());
            player.active();
        } else {
            TrilliumAPI.loadPlayers();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (TrilliumAPI.isLoaded(event.getPlayer())) {
            TrilliumPlayer player = player(event.getPlayer());
            player.active();
        } else {
            TrilliumAPI.loadPlayers();
        }
    }
}
