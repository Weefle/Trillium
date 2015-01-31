package me.lordsaad.trillium.teleport;

import me.lordsaad.trillium.PlayerDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandTeleportRA implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequestaccept")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest.respond")) {
                    if (CommandTeleportR.tprequest.containsValue(p.getUniqueId())) {
                        
                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        String world = requester.getLocation().getWorld().getName();
                        double x = requester.getLocation().getX();
                        double y = requester.getLocation().getY();
                        double z = requester.getLocation().getZ();
                        float yaw = requester.getLocation().getYaw();
                        float pitch = requester.getLocation().getPitch();

                        YamlConfiguration yml = null;
                        try {
                            yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(requester));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (yml != null) {
                            yml.set("Previous Location.world", world);
                            yml.set("Previous Location.x", x);
                            yml.set("Previous Location.y", y);
                            yml.set("Previous Location.z", z);
                            yml.set("Previous Location.pitch", pitch);
                            yml.set("Previous Location.yaw", yaw);
                        }
                        
                        requester.teleport(p);
                        p.sendMessage(ChatColor.GREEN + "You teleported " + requester.getName() + " to you.");
                        requester.sendMessage(ChatColor.GREEN + p.getName() + " accepted your teleport request.");

                    } else if (CommandTeleportRH.tprh.containsKey(p.getUniqueId())) {
                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        String world = p.getLocation().getWorld().getName();
                        double x = p.getLocation().getX();
                        double y = p.getLocation().getY();
                        double z = p.getLocation().getZ();
                        float yaw = p.getLocation().getYaw();
                        float pitch = p.getLocation().getPitch();

                        YamlConfiguration yml = null;
                        try {
                            yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (yml != null) {
                            yml.set("Previous Location.world", world);
                            yml.set("Previous Location.x", x);
                            yml.set("Previous Location.y", y);
                            yml.set("Previous Location.z", z);
                            yml.set("Previous Location.pitch", pitch);
                            yml.set("Previous Location.yaw", yaw);
                        }

                        try {
                            if (yml != null) {
                                yml.save(PlayerDatabase.db(p));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                        p.teleport(requester);
                        p.sendMessage(ChatColor.GREEN + "You teleported to " + requester.getName());
                        requester.sendMessage(ChatColor.GREEN + p.getName() + " accepted to teleport to you.");
                        
                    } else {
                        p.sendMessage(ChatColor.RED + "No pending teleport requests to accept.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You can't do that.");
            }
        }
        return true;
    }
}