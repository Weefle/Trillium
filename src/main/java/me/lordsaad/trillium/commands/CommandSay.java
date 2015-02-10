package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSay implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("say")) {
            if (!(sender instanceof Player)) {

                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString().trim();

                Message.b(MType.R, ChatColor.LIGHT_PURPLE + "Console", message);

            } else {
                Message.m(MType.W, sender, "Say", "Say is for the console. Not you.");
            }
        }
        return true;
    }
}