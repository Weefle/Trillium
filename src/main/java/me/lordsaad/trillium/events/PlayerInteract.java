package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandCmdBinder;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.databases.CmdBinderDatabase;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            if (event.getClickedBlock().getLocation().getBlockX() == loc.getBlockX()
                    && event.getClickedBlock().getLocation().getBlockY() == loc.getBlockY()
                    && event.getClickedBlock().getLocation().getBlockZ() == loc.getBlockZ()) {

                if (CommandCmdBinder.touchconsole.containsKey(p.getLocation())) {
                    String cmd = CommandCmdBinder.touchconsole.get(p.getLocation());
                    cmd = cmd.replace("[p]", p.getName());
                    Bukkit.dispatchCommand(Main.plugin.getServer().getConsoleSender(), cmd);
                    event.setCancelled(true);

                } else if (CommandCmdBinder.touchplayer.containsKey(p.getLocation())) {
                    String cmd = CommandCmdBinder.touchplayer.get(p.getLocation());
                    cmd = cmd.replace("[p]", p.getName());
                    Bukkit.dispatchCommand(p, cmd);
                    event.setCancelled(true);
                }
            }
        }

        if (CommandCmdBinder.antilagcheckitem.contains(p.getUniqueId())) {
            if (CommandCmdBinder.itemconsole.containsKey(p.getUniqueId())) {

                Map<ItemStack, String> iands = CommandCmdBinder.itemconsole.get(p.getUniqueId());
                for (ItemStack item : iands.keySet()) {
                    String cmd = iands.get(item);
                    cmd = cmd.replace("[p]", p.getName());
                    if (p.getItemInHand().equals(item)) {
                        Bukkit.dispatchCommand(Main.plugin.getServer().getConsoleSender(), cmd);
                        event.setCancelled(true);
                    }
                }

            } else if (CommandCmdBinder.itemplayer.containsKey(p.getUniqueId())) {
                Map<ItemStack, String> iands = CommandCmdBinder.itemplayer.get(p.getUniqueId());
                for (ItemStack item : iands.keySet()) {
                    String cmd = iands.get(item);
                    cmd = cmd.replace("[p]", p.getName());
                    if (p.getItemInHand().equals(item)) {
                        Bukkit.dispatchCommand(p, cmd);
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (CommandCmdBinder.antilagcheckcmd.contains(p.getUniqueId())) {

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
                    Message.m(MType.G, p, "Cmd Binder", "Command successfully bound to block.");
                    Message.m(MType.G, p, "Cmd Binder", ChatColor.AQUA + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                    event.setCancelled(true);
                    CommandCmdBinder.tcmdbconsole.remove(p.getUniqueId());
                    try {
                        yml.save(CmdBinderDatabase.cbd());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                    Message.m(MType.G, p, "Cmd Binder", "Command successfully bound to block.");
                    Message.m(MType.G, p, "Cmd Binder", ChatColor.AQUA + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                    event.setCancelled(true);
                    CommandCmdBinder.tcmdbplayer.remove(p.getUniqueId());
                    try {
                        yml.save(CmdBinderDatabase.cbd());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                        Message.m(MType.G, p, "Cmd Binder", "Command successfully bound to air block.");
                        Message.m(MType.G, p, "Cmd Binder", ChatColor.AQUA + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                        event.setCancelled(true);
                        CommandCmdBinder.wcmdbconsole.remove(p.getUniqueId());
                        try {
                            yml.save(CmdBinderDatabase.cbd());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                        Message.m(MType.G, p, "Cmd Binder", "Command successfully bound to air block.");
                        Message.m(MType.G, p, "Cmd Binder", ChatColor.AQUA + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                        event.setCancelled(true);
                        CommandCmdBinder.wcmdbplayer.remove(p.getUniqueId());
                        try {
                            yml.save(CmdBinderDatabase.cbd());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}