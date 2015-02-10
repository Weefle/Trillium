package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInfo implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("information")) {
            if (sender.hasPermission("tr.info")) {
                if (args.length == 0) {
                    Message.earg(sender, "Info", "/info <player>");
                } else {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        p.sendMessage(" ");
                        Message.m(MType.R, sender, "Info", "Displaying Information on: " + ChatColor.AQUA + p.getName());
                        Message.m(MType.R, sender, "Info", "Nickname: " + ChatColor.AQUA + p.getDisplayName());
                        Message.m(MType.R, sender, "Info", "Online: " + ChatColor.AQUA + API.isOnline(p));
                        Message.m(MType.R, sender, "Info", "Gamemode: " + ChatColor.AQUA + API.getGamemode(p));
                        Message.m(MType.R, sender, "Info", "Banned: " + ChatColor.AQUA + p.isBanned());
                        if (p.isBanned()) {
                            Message.m(MType.R, sender, "Info", "Ban Reason: 'You are the weakest link. Goodbye.'");
                        }
                        Message.m(MType.R, sender, "Info", "Muted: " + ChatColor.AQUA + API.isMuted(p));
                        Message.m(MType.R, sender, "Info", "Flying: " + ChatColor.AQUA + API.isFlying(p));
                        Message.m(MType.R, sender, "Info", "Ping: " + ChatColor.AQUA + API.getPing(p));
                        Message.m(MType.R, sender, "Info", "Ping: " + ChatColor.AQUA + API.getPingBar(p));
                        Message.m(MType.R, sender, "Info", "Location: " + ChatColor.AQUA + API.locationString(p));
                        if (!API.isOnline(p)) {
                            Message.m(MType.R, sender, "Info", "Last found at: " + ChatColor.AQUA + API.lastLocationString(p));
                        }
                        Message.m(MType.R, sender, "Info", "Food level: " + ChatColor.AQUA + API.getFoodLevel(p));
                        Message.m(MType.R, sender, "Info", "Health level: " + ChatColor.AQUA + API.getHealthLevel(p));
                        Message.m(MType.R, sender, "Info", "Time Played: hours: " + ChatColor.AQUA + (API.getTimePlayed(p) / 60) / 60);
                        Message.m(MType.R, sender, "Info", "Time Played: days: " + ChatColor.AQUA + ((API.getTimePlayed(p) / 60) / 60) / 24);
                    } else {
                        Message.eplayer(sender, "Info", args[0]);
                    }
                }
            } else {
                Message.e(sender, "Info", Crit.P);
            }
        }
        return true;
    }

}