package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBroadcast implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("tr.broadcast")) {
            if (args.length == 0) {

                Message.earg(sender, "Broadcast", "Too few arguments. /broadcast <message>");

            } else {

                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());

                API.broadcast(message);
            }
        } else {
            Message.e(sender, "Broadcast", Crit.P);
        }
        return true;
    }
}