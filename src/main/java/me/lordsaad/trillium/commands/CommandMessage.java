package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMessage implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.message")) {
                    if (args.length < 2) {
                        Message.earg(p, "MSG", "/msg <sender> <message>");

                    } else {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String msg = sb.toString().trim();

                            Message.minvert(MType.R, p, target.getName(), msg);
                            Message.m(MType.R, target, p.getName(), msg);
                        } else {
                            Message.eplayer(p, "MSG", args[0]);
                        }
                    }
                } else {
                    Message.e(p, "MSG", Crit.P);
                }
            } else {
                Message.e(sender, "MSG", Crit.C);
            }
        }
        return true;
    }
}