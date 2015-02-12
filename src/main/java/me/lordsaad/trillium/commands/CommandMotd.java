package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandMotd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("motd")) {
            if (sender.hasPermission("tr.motd")) {
                    ArrayList<String> motd = (ArrayList<String>) TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Server.INGAME_MOTD);
                    for (String s : motd) {
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        sender.sendMessage(s);
                    }
            } else {
                Message.e(sender, "Motd", Crit.P);
            }
        }
        return true;
    }
}