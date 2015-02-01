package me.lordsaad.trillium.invsee;

import me.lordsaad.trillium.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInvsee implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("invsee")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.invsee")) {
                    if (args.length != 0) {
                        if (args[0].equalsIgnoreCase("inventory")) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                Utils.saveinventory(p, target.getInventory().getContents(), target.getInventory().getArmorContents());
                            }
                            
                        } else if (args[0].equalsIgnoreCase("enderchest")) {
                            Player target = Bukkit.getPlayer(args[1]);
                            
                            if (target != null) {
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too few arguments. /invsee <inventory/enderchest> <player>");
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