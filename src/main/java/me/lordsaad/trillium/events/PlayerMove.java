package me.lordsaad.trillium.events;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandCmdBinder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockY() != event.getTo().getBlockY()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

            for (Location loc : CommandCmdBinder.antilagcheckloc) {
                if (p.getLocation().getBlockX() == loc.getBlockX()
                        && p.getLocation().getBlockY() == loc.getBlockY()
                        && p.getLocation().getBlockZ() == loc.getBlockZ()) {
                    if (CommandCmdBinder.walkconsole.containsKey(p.getLocation())) {
                        String cmd = CommandCmdBinder.walkconsole.get(p.getLocation());
                        cmd = cmd.replace("[p]", p.getName());
                        Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), cmd);

                    } else if (CommandCmdBinder.walkplayer.containsKey(p.getLocation())) {
                        String cmd = CommandCmdBinder.walkplayer.get(p.getLocation());
                        cmd = cmd.replace("[p]", p.getName());
                        Bukkit.dispatchCommand(p, cmd);
                    }
                }
            }
        }
    }
}
