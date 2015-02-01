package me.lordsaad.trillium.teleport;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportRD implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequestdeny")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest.respond")) {
                    if (CommandTeleportR.tprequest.containsValue(p.getUniqueId())) {

                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        Message.m(MType.G, p, "TPRD", "You denied " + ChatColor.AQUA + requester.getName() + "'s teleport request.");
                        Message.m(MType.G, requester, "TPRD", p.getName() + " denied your teleport request.");
                        CommandTeleportR.tprequest.remove(p.getUniqueId());

                    } else {
                        Message.m(MType.W, p, "TPRD", "No pending teleport requests to deny.");
                    }
                } else {
                    Message.e(p, "TPRD", Crit.P);
                }
            } else {
                Message.e(sender, "TPRD", Crit.C);
            }
        }
        return true;
    }
}