package me.lordsaad.trillium.commands;

import java.util.ArrayList;
import java.util.UUID;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGodMode implements CommandExecutor {

    public static ArrayList<UUID> godmodeusers = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("god")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    if (p.hasPermission("tr.god")) {

                        if (API.isGodMode(p)) {
                            API.setGodMode(false, p);
                            Message.m(MType.G, p, "God Mode", "You are no longer in god mode.");

                        } else {
                            API.setGodMode(true, p);
                            Message.m(MType.G, p, "God Mode", "You are now in god mode.");
                        }

                    } else {
                        Message.e(p, "God Mode", Crit.P);
                    }

                } else {
                    if (p.hasPermission("tr.god.other")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        if (pl != null) {

                            if (API.isGodMode(pl)) {
                                API.setGodMode(false, pl);
                                Message.m(MType.G, pl, "God Mode", p.getName() + " removed you from god mode.");
                                Message.m(MType.G, p, "God Mode", pl.getName() + " is no longer in god mode.");

                            } else {
                                API.setGodMode(true, pl);
                                Message.m(MType.G, pl, "God Mode", p.getName() + " put you in god mode.");
                                Message.m(MType.G, p, "God Mode", pl.getName() + " is now in god mode.");
                            }

                        } else {
                            Message.eplayer(p, "God Mode", args[0]);
                        }
                    } else {
                        Message.earg(p, "God Mode", "/god [player]");
                    }
                }
            } else {
                Message.e(sender, "God Mode", Crit.C);
            }
        }
        return true;
    }
}