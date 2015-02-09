package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandMotd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("motd")) {
            ArrayList<String> motd = (ArrayList<String>) Main.plugin.getConfig().getStringList("Motd");
            for (String s : motd) {
                s = ChatColor.translateAlternateColorCodes('&', s);
                sender.sendMessage(s);
            }
        }
        return true;
    }
}