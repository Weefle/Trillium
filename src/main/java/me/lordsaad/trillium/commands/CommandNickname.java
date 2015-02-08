package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNickname implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nickname")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    if (p.hasPermission("tr.nickname")) {

                        Message.m(MType.G, p, "Nickname", "New nickname set: " + args[0]);
                        p.setDisplayName(args[0]);

                    } else if (p.hasPermission("tr.nickname.color")) {

                        String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                        Message.m(MType.G, p, "Nickname", "New nickname set: " + nick);
                        p.setDisplayName(args[0]);

                    } else {
                        Message.e(p, "Nickname", Crit.P);
                    }
                }
            } else {
                Message.e(sender, "Nickname", Crit.C);
            }
        }
        return true;
    }
}