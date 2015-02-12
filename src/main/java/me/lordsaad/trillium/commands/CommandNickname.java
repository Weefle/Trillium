package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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
                    if (p.hasPermission("tr.nickname") && !p.isOp()) {

                        Message.m(MType.G, p, "Nickname", "New nickname set: " + args[0]);
                        p.setDisplayName(args[0]);

                    } else if (p.hasPermission("tr.nickname.color")) {

                        String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                        Message.m(MType.G, p, "Nickname", "New nickname set: " + nick);
                        p.setDisplayName(args[0]);

                    } else {
                        Message.e(p, "Nickname", Crit.P);
                    }
                } else if (args.length > 1) {
                    if (p.hasPermission("tr.nickname.other") && !p.isOp()) {

                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            target.setDisplayName(args[0]);
                            Message.m(MType.G, target, "Nickname", ChatColor.AQUA + p.getName() + ChatColor.BLUE + " set your nickname to: " + args[0]);
                            Message.m(MType.G, p, "Nickname", "You set " + ChatColor.AQUA + p.getName() + ChatColor.BLUE + " to: " + args[0]);

                        } else {
                            Message.eplayer(p, "Nickname", args[0]);
                        }
                    } else if (p.hasPermission("tr.nickname.other.color")) {

                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                            target.setDisplayName(nick);
                            Message.m(MType.G, target, "Nickname", ChatColor.AQUA + p.getName() + ChatColor.BLUE + " set your nickname to: " + nick);
                            Message.m(MType.G, p, "Nickname", "You set " + ChatColor.AQUA + p.getName() + ChatColor.BLUE + " to: " + nick);

                        } else {
                            Message.eplayer(p, "Nickname", args[0]);
                        }
                    } else {
                        Message.e(p, "Nickname", Crit.P);
                    }
                } else {
                    Message.earg(p, "Nickname", "/nick <nickname> [player]");
                }
            } else {
                Message.e(sender, "Nickname", Crit.C);
            }
        }
        return true;
    }
}