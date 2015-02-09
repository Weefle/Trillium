package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
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

public class CommandVanish implements CommandExecutor {

    public static ArrayList<UUID> vanishedusers = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("vanish")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    if (p.hasPermission("tr.vanish")) {
                        
                        if (!API.isVanished(p)) {

                            API.setVanished(true, p);
                            Message.m(MType.G, p, "Vanish", "You are now in vanish mode.");

                        } else {
                            
                            API.setVanished(false, p);
                            Message.m(MType.G, p, "Vanish", "You are no longer in vanish mode.");
                        }
                    } else {
                        Message.e(p, "Vanish", Crit.P);
                    }

                } else {
                    if (p.hasPermission("tr.vanish.other")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        if (pl != null) {
                            
                            if (API.isVanished(p)) {

                                API.setVanished(false, p);
                                Message.m(MType.G, pl, "Vanish", p.getName() + " put you in vanish mode.");
                                Message.m(MType.G, p, "Vanish", pl.getName() + " is now in vanish mode.");

                            } else {

                                API.setVanished(false, p);
                                Message.m(MType.G, pl, "Vanish", p.getName() + " removed you from vanish mode.");
                                Message.m(MType.G, p, "Vanish", pl.getName() + " is no longer in vanish mode.");
                            }
                        } else {
                            Message.eplayer(p, "Vanish", args[0]);
                        }
                    } else {
                        Message.earg(p, "Vanish", "/vanish [player]");
                    }
                }
            } else {
                Message.e(sender, "Vanish", Crit.C);
            }
        }

        return true;
    }
}
