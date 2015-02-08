package me.lordsaad.trillium.events;

import me.lordsaad.trillium.CmdBinderDatabase;
import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandCmdBinder;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (Main.plugin.getConfig().getBoolean("AFK.auto unafk")) {
            if (CommandAfk.afklist.contains(p.getUniqueId())) {
                if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                    CommandAfk.afklist.remove(p.getUniqueId());
                    CommandAfk.afktimer.put(p.getUniqueId(), 0);
                    Message.b(MType.G, "AFK", p.getName() + " is no longer AFK.");
                }
            }
        }

        for (Location loc : CommandCmdBinder.antilagcheckloc) {
            if (p.getLocation().equals(loc)) {
                if (CommandCmdBinder.touchconsole.containsKey(p.getLocation())) {
                    String cmd = CommandCmdBinder.touchconsole.get(p.getLocation());
                    cmd = cmd.replace("[p]", p.getName());
                    Bukkit.dispatchCommand(Main.plugin.getServer().getConsoleSender(), cmd);
                } else if (CommandCmdBinder.touchplayer.containsKey(p.getLocation())) {
                    String cmd = CommandCmdBinder.touchplayer.get(p.getLocation());
                    cmd = cmd.replace("[p]", p.getName());
                    Bukkit.dispatchCommand(Main.plugin.getServer().getConsoleSender(), cmd);
                }
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(CmdBinderDatabase.cbd());

        if (CommandCmdBinder.tcmdbconsole.containsKey(p.getUniqueId())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                int x = event.getClickedBlock().getLocation().getBlockX();
                int y = event.getClickedBlock().getLocation().getBlockZ();
                int z = event.getClickedBlock().getLocation().getBlockZ();

                List<String> l = new ArrayList<String>();
                l.add(yml.getString("touchconsole"));
                l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                yml.set("touchconsole", l);
                CommandCmdBinder.tcmdbconsole.remove(p.getUniqueId());
            }
        } else if (CommandCmdBinder.tcmdbplayer.containsKey(p.getUniqueId())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                int x = event.getClickedBlock().getLocation().getBlockX();
                int y = event.getClickedBlock().getLocation().getBlockZ();
                int z = event.getClickedBlock().getLocation().getBlockZ();

                List<String> l = new ArrayList<String>();
                l.add(yml.getString("touchplayer"));
                l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + CommandCmdBinder.tcmdbplayer.get(p.getUniqueId()));
                yml.set("touchplayer", l);
                CommandCmdBinder.tcmdbplayer.remove(p.getUniqueId());
            }
        } else if (CommandCmdBinder.wcmdbconsole.containsKey(p.getUniqueId())) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getRelative(event.getBlockFace()).getType() == null
                        || event.getClickedBlock().getRelative(event.getBlockFace()).getType() == Material.AIR) {
                    int x = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockX();
                    int y = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockY();
                    int z = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockZ();

                    List<String> l = new ArrayList<String>();
                    l.add(yml.getString("walkconsole"));
                    l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + CommandCmdBinder.wcmdbconsole.get(p.getUniqueId()));
                    yml.set("walkconsole", l);
                    CommandCmdBinder.wcmdbconsole.remove(p.getUniqueId());
                }
            }
        } else if (CommandCmdBinder.wcmdbplayer.containsKey(p.getUniqueId())) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getRelative(event.getBlockFace()).getType() == null
                        || event.getClickedBlock().getRelative(event.getBlockFace()).getType() == Material.AIR) {
                    int x = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockX();
                    int y = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockY();
                    int z = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockZ();

                    List<String> l = new ArrayList<String>();
                    l.add(yml.getString("walkplayer"));
                    l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + CommandCmdBinder.wcmdbplayer.get(p.getUniqueId()));
                    yml.set("walkplayer", l);
                    CommandCmdBinder.wcmdbplayer.remove(p.getUniqueId());
                }
            }
        }
    }
}