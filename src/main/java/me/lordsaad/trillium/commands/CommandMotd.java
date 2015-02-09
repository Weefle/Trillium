package me.lordsaad.trillium.commands;

import java.util.ArrayList;
import java.util.Collections;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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

                        TrilliumAPI.getInstance().getConfig().set("Motd", motd);
                        Message.m(MType.G, sender, "Motd", "New motd set:");
                        sender.sendMessage(String.valueOf(motd));

                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }
                } else {
                    ArrayList<String> motd = (ArrayList<String>) TrilliumAPI.getInstance().getConfig().getStringList("Motd");
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