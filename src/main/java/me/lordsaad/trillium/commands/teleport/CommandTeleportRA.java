package me.lordsaad.trillium.commands.teleport;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleportRA implements CommandExecutor {

    //TODO: save last location
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleportrequestaccept")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleportrequest.respond")) {
                    if (CommandTeleportR.tprequest.containsValue(p.getUniqueId())) {

                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        requester.teleport(p);
                        Message.m(MType.G, p, "TPRA", "You teleported " + requester.getName() + " to you.");
                        Message.m(MType.G, requester, "TPRA", p.getName() + " accepted your teleport request.");

                    } else if (CommandTeleportRH.tprh.containsValue(p.getUniqueId())) {

                        Player requester = Bukkit.getPlayer(CommandTeleportR.tprequest.get(p.getUniqueId()));

                        p.teleport(requester);
                        Message.m(MType.G, p, "TPRA", "You teleported to " + requester.getName());
                        Message.m(MType.G, requester, "TPRA", p.getName() + " accepted to teleport to you.");

                    } else {
                        Message.m(MType.W, p, "TPRA", "No pending teleport requests to accept.");
                    }
                } else {
                    Message.e(p, "TPRA", Crit.P);
                }
            } else {
                Message.e(sender, "TPRA", Crit.C);
            }
        }
        return true;
    }
}