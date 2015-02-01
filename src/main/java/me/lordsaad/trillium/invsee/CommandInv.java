package me.lordsaad.trillium.invsee;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInv implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("inv")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.inv")) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            p.openInventory(target.getInventory());
                            Message.m(MType.G, p, "Invsee", "You are now viewing " + target.getName() + "'s inventory");
                        } else {
                            Message.e(p, "Invsee", args[1], Crit.T);
                        }

                    } else {
                        if (args[1].equalsIgnoreCase("enderchest")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            
                            if (target != null) {
                                p.openInventory(target.getEnderChest());
                                Message.m(MType.G, p, "Invsee", "Now viewing " + args[1] + "'s inventory.");
                            } else {
                                Message.e(p, "Invsee", args[1], Crit.T);
                            }
                        }
                    }
                } else {
                    Message.e(p, "Invsee", Crit.P);
                }
            } else {
                Message.e(sender, "Invsee", Crit.C);
            }
        }
        return true;
    }
}