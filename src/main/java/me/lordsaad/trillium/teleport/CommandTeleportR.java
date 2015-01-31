package me.lordsaad.trillium.teleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CommandTeleportR implements CommandExecutor {

    static HashMap<UUID, UUID> tprequest = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequest")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest")) {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            p.sendMessage(ChatColor.BLUE + "Teleport request to " + target.getName() + " is now pending. Please stand by.");
                            target.sendMessage(ChatColor.AQUA + p.getName() + ChatColor.BLUE + " would like to teleport to you.");
                            target.sendMessage(ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.");
                            target.sendMessage(ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.");
                            tprequest.put(p.getUniqueId(), target.getUniqueId());

                        } else {
                            p.sendMessage(ChatColor.RED + args[0] + " is either offline or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too few arguments. /tpa <player>");
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