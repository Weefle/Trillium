package me.lordsaad.trillium.tp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportHere implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleporthere")) {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                                target.teleport(p);
                                p.sendMessage(ChatColor.GREEN + "You teleported " + target.getName() + " to you.");
                                target.sendMessage(ChatColor.BLUE + p.getName() + " teleported you to them.");
                            
                        } else {
                            p.sendMessage(ChatColor.RED + args[0] + " is either offline or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too few arguments. /tphere <player>");
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