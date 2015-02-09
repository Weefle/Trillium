package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKick implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kick")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.kick")) {
                    if (args.length < 2) {
                        Message.earg(p, "Kick", "/kick <player>");

                    } else {

                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String reason = sb.toString().trim();

                            Message.b(MType.W, "Kick", target.getName() + " got kicked for:");
                            Message.b(MType.W, "Kick", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                            p.kickPlayer(reason);

                        } else {
                            Message.eplayer(p, "Kick", args[0]);
                        }
                    }
                } else {
                    Message.e(p, "Kick", Crit.P);
                }
            } else {
                Message.e(sender, "Kick", Crit.C);
            }
        }
        return true;
    }
}