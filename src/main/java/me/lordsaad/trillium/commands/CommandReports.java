package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReports implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("reports")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.reportreceiver")) {
                    if (args.length != 0) {

                        if (args[0].equalsIgnoreCase("clear")) {
                            CommandReport.reportlist.clear();
                            Message.m(MType.G, p, "Reports", "Cleared Report List.");

                        } else if (args[0].equalsIgnoreCase("remove")) {
                            if (args.length < 2) {
                                Message.earg(p, "Reports", "/reports remove <index number>");
                            } else {
                                if (API.isint(args[1])) {
                                    int nb = Integer.parseInt(args[1]);
                                    if (nb > 0 && nb <= CommandReport.reportlist.size() + 1) {
                                        Message.m(MType.G, p, "Reports", "Removed: " + nb);
                                        p.sendMessage(CommandReport.reportlist.get(nb - 1));
                                        CommandReport.reportlist.remove(nb - 1);

                                    } else {
                                        Message.m(MType.W, p, "Reports", args[1] + " is either larger than the list index or smaller than 0");
                                    }
                                } else {
                                    Message.m(MType.W, p, "Reports", args[1] + " is not a number.");
                                }
                            }
                        } else {
                            Message.earg(p, "Reports", "/reports [remove <index>/clear]");
                        }

                    } else {
                        p.sendMessage(ChatColor.BLUE + "Report List:");
                        int nb = 0;
                        for (String big : CommandReport.reportlist) {
                            nb++;
                            p.sendMessage(ChatColor.GRAY + "-" + ChatColor.AQUA + nb + ChatColor.GRAY + "- " + big);
                        }
                    }
                } else {
                    Message.e(p, "Report", Crit.P);
                }
            } else {
                Message.e(sender, "Report", Crit.C);
            }
        }
        return true;
    }
}
