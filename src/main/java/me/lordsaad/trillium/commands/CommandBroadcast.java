package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
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

                Boolean space = Main.plugin.getConfig().getBoolean("Broadcast.line clearing");
                Boolean btwn = Main.plugin.getConfig().getBoolean("Broadcast.line clearing between header and footer");
                Boolean headerboolean = Main.plugin.getConfig().getBoolean("Broadcast.header.enabled");
                String header = Main.plugin.getConfig().getString("Broadcast.header.set");
                Boolean footerboolean = Main.plugin.getConfig().getBoolean("Broadcast.footer.enabled");
                String footer = Main.plugin.getConfig().getString("Broadcast.footer.set");
                Boolean prefixboolean = Main.plugin.getConfig().getBoolean("Broadcast.prefix.enabled");
                String prefix = Main.plugin.getConfig().getString("Broadcast.prefix.set");

                if (space) {
                    Bukkit.broadcastMessage(" ");
                }
                if (headerboolean) {
                    Bukkit.broadcastMessage(header);
                }
                if (btwn) {
                    Bukkit.broadcastMessage(" ");
                }

                if (prefixboolean) {
                    Bukkit.broadcastMessage(prefix + " " + message);
                } else {
                    Bukkit.broadcastMessage(message);
                }

                if (btwn) {
                    Bukkit.broadcastMessage(" ");
                }
                if (footerboolean) {
                    Bukkit.broadcastMessage(footer);
                }
                if (space) {
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage(" ");
                }
            }
        } else {
            Message.e(sender, "Broadcast", Crit.P);
        }
        return true;
    }
}