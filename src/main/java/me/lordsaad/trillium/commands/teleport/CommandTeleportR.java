package me.lordsaad.trillium.commands.teleport;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CommandTeleportR implements CommandExecutor {

    static HashMap<UUID, UUID> tprequest = new HashMap<UUID, UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequest")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest")) {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            Message.m(MType.R, p, "TPR", target.getName() + " is now pending. Please stand by.");
                            Message.m(MType.R, target, "TPR", p.getName() + " would like to teleport to you.");
                            Message.m(MType.R, target, "TPR", ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.");
                            Message.m(MType.R, target, "TPR", ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.");
                            tprequest.put(p.getUniqueId(), target.getUniqueId());

                        } else {
                            Message.eplayer(p, "TPR", args[0]);
                        }
                    } else {
                        Message.earg(p, "TPR", "/tpr <player>");
                    }
                } else {
                    Message.e(p, "TPR", Crit.P);
                }
            } else {
                Message.e(sender, "TPR", Crit.C);
            }
        }
        return true;
    }
}