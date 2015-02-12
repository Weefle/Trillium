package me.lordsaad.trillium.commands.teleport;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportH implements CommandExecutor {

    //TODO: save last location
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleporthere")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleporthere")) {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            target.teleport(p);
                            Message.m(MType.G, p, "TPH", "You teleported " + target.getName() + " to you.");
                            Message.m(MType.G, target, "TPH", p.getName() + " teleported you to them.");

                        } else {
                            Message.eplayer(p, "TPH", args[0]);
                        }
                    } else {
                        Message.earg(p, "TPH", "/tphere <player>");
                    }
                } else {
                    Message.e(p, "TPH", Crit.P);
                }
            } else {
                Message.e(sender, "TPH", Crit.C);
            }
        }
        return true;
    }
}