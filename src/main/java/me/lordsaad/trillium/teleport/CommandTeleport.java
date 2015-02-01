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

public class CommandTeleport implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    if (p.hasPermission("tr.teleport")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            String world = p.getLocation().getWorld().getName();
                            int x = p.getLocation().getBlockX();
                            int y = p.getLocation().getBlockY();
                            int z = p.getLocation().getBlockZ();

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

                            p.teleport(target);
                            p.sendMessage(ChatColor.GREEN + "You teleported to " + target.getName());

                        } else {
                            p.sendMessage(ChatColor.RED + args[0] + " is either offline or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }
                } else if (args.length >= 2) {
                    if (p.hasPermission("tr.teleport.other")) {
                        Player target1 = Bukkit.getPlayer(args[0]);
                        Player target2 = Bukkit.getPlayer(args[1]);
                        if (target1 != null) {
                            if (target2 != null) {

                                String world = target1.getLocation().getWorld().getName();
                                int x = target1.getLocation().getBlockX();
                                int y = target1.getLocation().getBlockY();
                                int z = target1.getLocation().getBlockZ();

                                YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(target1));

                                yml.set("Previous Location.world", world);
                                yml.set("Previous Location.x", x);
                                yml.set("Previous Location.y", y);
                                yml.set("Previous Location.z", z);

                                target1.teleport(target2);
                                p.sendMessage(ChatColor.GREEN + "You teleported " + target1.getName() + " to " + target2.getName());
                                target1.sendMessage(ChatColor.GREEN + p.getName() + " teleported you to " + target2.getName());

                            } else {
                                p.sendMessage(ChatColor.RED + args[1] + " is either offline or does not exist.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + args[0] + " is either offline or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too few arguments. /tp <player> [player]");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You can't do that.");
            }
        }
        return true;
    }
}