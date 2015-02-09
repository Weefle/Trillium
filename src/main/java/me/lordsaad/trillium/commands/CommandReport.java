package me.lordsaad.trillium.commands;

import java.util.ArrayList;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReport implements CommandExecutor {

    public static ArrayList<String> reportlist = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("report")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.report") || p.hasPermission("tr.reportreceiver")) {
                    if (args.length != 0) {

                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            sb.append(arg).append(" ");
                        }
                        String msg = sb.toString().trim();

                        String big = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Reports" + ChatColor.DARK_GRAY + "]"
                                + ChatColor.BLUE + " {"
                                + ChatColor.AQUA + p.getName() + ChatColor.BLUE + ", "
                                + ChatColor.AQUA + p.getWorld().getName() + ChatColor.BLUE + ", "
                                + ChatColor.AQUA + p.getLocation().getBlockX() + ChatColor.BLUE + ", "
                                + ChatColor.AQUA + p.getLocation().getBlockY() + ChatColor.BLUE + ", "
                                + ChatColor.AQUA + p.getLocation().getBlockZ() + ChatColor.BLUE + "} >> "
                                + ChatColor.GRAY + msg;

                        reportlist.add(big);
                        Message.m(MType.G, p, "Report", "Your report was submitted successfully.");
                        p.sendMessage(ChatColor.YELLOW + "'" + ChatColor.GRAY + msg + ChatColor.YELLOW + "'");
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            if (pl.hasPermission("tr.reportreceiver")) {
                                Message.m(MType.W, p, "Report", "A new report was submitted by: " + p.getName());
                                pl.sendMessage(big);
                                Message.m(MType.R, p, "Report", "/reports for a list of all reports.");
                            }
                        }
                    } else {
                        Message.m(MType.W, p, "Report", "What's your report? /report <msg>");
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
