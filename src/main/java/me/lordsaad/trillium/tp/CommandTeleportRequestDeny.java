package me.lordsaad.trillium.tp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportRequestDeny implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequestdeny")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest.respond")) {
                    if (CommandTeleportRequest.tprequest.containsValue(p.getUniqueId())) {

                        Player requester = Bukkit.getPlayer(CommandTeleportRequest.tprequest.get(p.getUniqueId()));

                        p.sendMessage(ChatColor.GREEN + "You denied " + ChatColor.AQUA + requester.getName() + "'s teleport request.");
                        requester.sendMessage(ChatColor.RED + p.getName() + " denied your teleport request.");
                        CommandTeleportRequest.tprequest.remove(p.getUniqueId());

                    } else {
                        p.sendMessage(ChatColor.RED + "No pending teleport requests to deny.");
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