package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;

public class CommandMotd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("motd")) {
            if (sender.hasPermission("pd.motd")) {
                if (args.length != 0) {
                    if (sender.hasPermission("pd.motd.edit")) {

                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            sb.append(arg).append(" ");
                        }
                        String msg = sb.toString().trim();

                        ArrayList<String> motd = new ArrayList<String>();

                        Collections.addAll(motd, msg.split("\n"));

                        Main.plugin.getConfig().set("Motd", motd);
                        Message.m(MType.G, sender, "Motd", "New motd set:");
w                        sender.sendMessage(String.valueOf(motd));

                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }
                } else {
                    ArrayList<String> motd = (ArrayList<String>) Main.plugin.getConfig().getStringList("Motd");
                    for (String s : motd) {
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        sender.sendMessage(s);
                    }
                }
            } else {
                Message.e(sender, "TPRH", Crit.P);
            }
        }
        return true;
    }
}