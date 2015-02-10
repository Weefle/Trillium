package me.lordsaad.trillium.commands;

import java.util.ArrayList;
import java.util.UUID;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {

    public static ArrayList<UUID> flyusers = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    if (p.hasPermission("tr.fly")) {

                        //long way for api and message. sorry, i know it can be compacted into 1 line of code...
                        if (flyusers.contains(p.getUniqueId())) {
                            flyusers.remove(p.getUniqueId());
                            Message.m(MType.G, p, "Fly", "You are no longer in fly mode.");
                            p.setAllowFlight(false);
                        } else {
                            flyusers.add(p.getUniqueId());
                            Message.m(MType.G, p, "Fly", "You are now in fly mode.");
                            p.setAllowFlight(true);
                        }
                    } else {
                        Message.e(p, "Fly", Crit.P);
                    }

                } else {
                    if (p.hasPermission("tr.fly.other")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        if (pl != null) {

                            if (flyusers.contains(pl.getUniqueId())) {
                                flyusers.remove(pl.getUniqueId());
                                Message.m(MType.G, pl, "Fly", p.getName() + " removed you from fly mode.");
                                Message.m(MType.G, p, "Fly", pl.getName() + " is no longer in fly mode.");
                                pl.setAllowFlight(false);
                            } else {
                                flyusers.add(pl.getUniqueId());
                                Message.m(MType.G, pl, "Fly", p.getName() + " put you in fly mode.");
                                Message.m(MType.G, p, "Fly", pl.getName() + " is now in fly mode.");
                                pl.setAllowFlight(true);
                            }
                        } else {
                            Message.eplayer(p, "Fly", args[0]);
                        }
                    } else {
                        Message.earg(p, "Fly", "/fly [player]");
                    }
                }
            } else {
                Message.e(sender, "Fly", Crit.C);
            }
        }

        return true;
    }
}
