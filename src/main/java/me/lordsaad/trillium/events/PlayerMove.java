package me.lordsaad.trillium.events;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandCmdBinder;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
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
            if (Main.plugin.getConfig().getBoolean("AFK.auto unafk")) {
                if (API.isafk(p)) {
                    if (!API.isvanished(p)) {
                        CommandAfk.afktimer.put(p.getUniqueId(), 0);
                        CommandAfk.afklist.remove(p.getUniqueId());
                        Message.b(MType.G, "AFK", p.getName() + " is no longer AFK.");
                    }
                }
            }

            for (Location loc : CommandCmdBinder.antilagcheckloc) {
                if (p.getLocation().equals(loc)) {
                    if (CommandCmdBinder.walkconsole.containsKey(p.getLocation())) {
                        String cmd = CommandCmdBinder.walkconsole.get(p.getLocation());
                        cmd = cmd.replace("[p]", p.getName());
                        Bukkit.dispatchCommand(Main.plugin.getServer().getConsoleSender(), cmd);
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
