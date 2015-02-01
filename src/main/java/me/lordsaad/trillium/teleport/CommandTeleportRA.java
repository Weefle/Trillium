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

                        if (requester != null) {
                            String world = requester.getLocation().getWorld().getName();
                            int x = requester.getLocation().getBlockX();
                            int y = requester.getLocation().getBlockY();
                            int z = requester.getLocation().getBlockZ();


                            YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(requester));

                            yml.set("Previous Location.world", world);
                            yml.set("Previous Location.x", x);
                            yml.set("Previous Location.y", y);
                            yml.set("Previous Location.z", z);

                            requester.teleport(p);
                            p.sendMessage(ChatColor.GREEN + "You teleported " + requester.getName() + " to you.");
                            requester.sendMessage(ChatColor.GREEN + p.getName() + " accepted your teleport request.");
                        } else {
                            p.sendMessage(ChatColor.RED + "Something went wrong...");
                        }
                    } else if (CommandTeleportRH.tprh.containsKey(p.getUniqueId())) {
                        
                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        if (requester != null) {
                            String world = p.getLocation().getWorld().getName();
                            int x = requester.getLocation().getBlockX();
                            int y = requester.getLocation().getBlockY();
                            int z = requester.getLocation().getBlockZ();

                            YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));

                            yml.set("Previous Location.world", world);
                            yml.set("Previous Location.x", x);
                            yml.set("Previous Location.y", y);
                            yml.set("Previous Location.z", z);

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            p.teleport(requester);
                            p.sendMessage(ChatColor.GREEN + "You teleported to " + requester.getName());
                            requester.sendMessage(ChatColor.GREEN + p.getName() + " accepted to teleport to you.");
                        } else {
                            p.sendMessage(ChatColor.RED + "Something went wrong...");
                        }
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