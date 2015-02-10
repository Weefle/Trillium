package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSmite implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("smite")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.thor")) {
                    if (args.length == 0) {
                        Location loc = p.getTargetBlock(null, 100).getLocation();
                        p.getWorld().strikeLightning(loc);
                    } else {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            target.getWorld().strikeLightning(target.getLocation());
                            Message.m(MType.R, target, "Smite", p.getName() + " stuck lightning upon you!");
                            Message.m(MType.R, p, "Smite", "You struck lightning upon " + target.getName());

                        } else {
                            Message.eplayer(p, "Smite", args[0]);
                        }
                    }
                } else {
                    Message.e(p, "Smite", Crit.P);
                }
            } else {
                Message.e(sender, "Smite", Crit.C);

            }
        }
        return true;
    }
}