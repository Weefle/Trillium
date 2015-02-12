package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMe implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("me")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.me")) {

                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    String message = sb.toString().trim();

                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "** " + ChatColor.GRAY + p.getName() + " " + message + ChatColor.DARK_GRAY + " **");

                } else {
                    Message.e(p, "Me", Crit.P);
                }
            } else {
                Message.e(sender, "Me", Crit.C);
            }
        }
        return true;
    }
}