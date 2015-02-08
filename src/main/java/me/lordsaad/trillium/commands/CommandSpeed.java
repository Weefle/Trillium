package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpeed implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speed")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.speed")) {
                    if (args.length > 1) {
                        if (API.isint(args[1]) || API.isdouble(args[1])) {
                            if (args[0].equalsIgnoreCase("fly")) {
                                double i = Double.parseDouble(args[1]);
                                if (i <= 10 && i >= -10) {
                                    i = i * 0.1;
                                    p.setFlySpeed((float) i);
                                    Message.m(MType.G, p, "Speed", "Fly speed set to " + ChatColor.AQUA + args[1]);
                                } else {
                                    Message.earg2(p, "Speed", "/speed <fly/walk> <speed>");
                                }
                            } else if (args[0].equalsIgnoreCase("walk")) {
                                double i = Double.parseDouble(args[1]);
                                if (i <= 10 && i >= -10) {
                                    i = i * 0.1;
                                    p.setWalkSpeed((float) i);
                                    Message.m(MType.G, p, "Speed", "Walk speed set to " + ChatColor.AQUA + args[1]);
                                } else {
                                    Message.earg2(p, "Speed", "/speed <fly/walk> <speed>");
                                }
                            } else {
                                Message.earg2(p, "Speed", "/speed <fly/walk> <speed>");
                            }
                        } else {
                            Message.m(MType.W, p, "Speed", args[1] + " is not a number.");
                        }
                    } else {
                        Message.earg(p, "Speed", "/speed <fly/walk> <speed>");
                    }
                } else {
                    Message.e(p, "Speed", Crit.P);
                }
            } else {
                Message.e(sender, "Speed", Crit.C);
            }
        }
        return true;
    }
}