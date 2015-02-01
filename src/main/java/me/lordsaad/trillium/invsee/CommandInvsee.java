package me.lordsaad.trillium.invsee;

import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class CommandInvsee implements CommandExecutor {

    static ArrayList<UUID> invusers = new ArrayList<UUID>();

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
                                invusers.add(p.getUniqueId());
                                Message.m(MType.G, p, "Invsee", "Your inventory is now " + args[1] + "'s. /invsee to restore your inventory.");
                            }

                        } else if (args[0].equalsIgnoreCase("enderchest")) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                p.openInventory(target.getInventory());
                                Message.m(MType.G, p, "Invsee", "Now viewing " + args[1] + "'s inventory.");
                            }
                        }
                    } else {
                        if (invusers.contains(p.getUniqueId())) {
                            invusers.remove(p.getUniqueId());
                            Utils.restoreinventory(p);
                            Message.m(MType.G, p, "Invsee", "Inventory restored.");
                        } else {
                            Message.b(MType.W, "Invsee", "You are not viewing anyones inventory to restore your original inventory.");
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