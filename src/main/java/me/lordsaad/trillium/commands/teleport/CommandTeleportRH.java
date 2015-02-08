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

public class CommandTeleportRH implements CommandExecutor {

    static HashMap<UUID, UUID> tprh = new HashMap<UUID, UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequesthere")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequesthere")) {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            Message.m(MType.R, p, "TPR", "Teleport request for " + target.getName() + " to here is now pending. Please stand by.");
                            Message.m(MType.R, target, "TPR", p.getName() + ChatColor.BLUE + " would like you to teleport to him");
                            Message.m(MType.R, target, "TPR", ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.");
                            Message.m(MType.R, target, "TPR", ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.");
                            tprh.put(p.getUniqueId(), target.getUniqueId());

                        } else {
                            Message.eplayer(p, "TPR", args[0]);
                        }
                    } else {
                        Message.earg(p, "TPRH", "/tprh <player>");
                    }
                } else {
                    Message.e(p, "TPRH", Crit.P);
                }
            } else {
                Message.e(sender, "TPRH", Crit.C);
            }
        }
        return true;
    }
}