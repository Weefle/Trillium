package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInventory implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("inventory")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("crafting") || args[0].equalsIgnoreCase("cv")) {
                        if (p.hasPermission("tr.inventory.crafting")) {
                            p.openWorkbench(p.getLocation(), true);
                            Message.m(MType.G, p, "Inventory", "Now viewing a crafting table.");
                        } else {
                            Message.e(p, "Inventory", Crit.P);
                        }
                    } else {
                        if (p.hasPermission("tr.inventory.player")) {
                            Player target = Bukkit.getPlayer(args[0]);

                            if (target != null) {
                                p.openInventory(target.getInventory());
                                Message.m(MType.G, p, "Inventory", "You are now viewing " + target.getName() + "'s inventory");
                            } else {
                                Message.eplayer(p, "Inventory", args[0]);
                            }
                        } else {
                            Message.e(p, "Inventory", Crit.P);
                        }
                    }
                } else if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("enderchest") || args[1].equalsIgnoreCase("ec")) {
                        if (p.hasPermission("tr.inventory.enderchest")) {
                            Player target = Bukkit.getPlayer(args[0]);

                            if (target != null) {
                                p.openInventory(target.getEnderChest());
                                Message.m(MType.G, p, "Inventory", "Now viewing " + args[0] + "'s ender chest.");

                            } else {
                                Message.eplayer(p, "Inventory", args[0]);
                            }
                        } else {
                            Message.e(p, "Inventory", Crit.P);
                        }
                    } else {
                        Message.m(MType.W, p, "Inventory", "What is that? /inventory <player [enderchest]/crafting>");
                    }
                } else {
                    Message.earg(p, "Inventory", " /inventory <player [enderchest]/crafting>");
                }
            } else {
                Message.e(sender, "Invsee", Crit.C);
            }
        }
        return true;
    }
}