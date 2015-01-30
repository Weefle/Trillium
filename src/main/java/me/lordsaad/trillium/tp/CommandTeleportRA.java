package me.lordsaad.trillium.tp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportRA implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequestaccept")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest.respond")) {
                    if (CommandTeleportR.tprequest.containsValue(p.getUniqueId())) {

                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        requester.teleport(p);
                        p.sendMessage(ChatColor.GREEN + "You teleported " + requester.getName() + " to you.");
                        requester.sendMessage(ChatColor.GREEN + p.getName() + " accepted your teleport request.");

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