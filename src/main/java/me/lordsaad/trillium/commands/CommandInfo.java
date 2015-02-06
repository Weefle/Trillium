package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
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
                    Message.m(MType.R, sender, "Info", "Displaying Information on: " + p.getName());
                    Message.m(MType.R, sender, "Info", "Nickname: " + API.getnickname(p));
                    Message.m(MType.R, sender, "Info", "Online: " + API.isonline(p));
                    Message.m(MType.R, sender, "Info", "Gamemode: " + API.getgamemode(p));
                    Message.m(MType.R, sender, "Info", "Banned: " + p.isBanned());
                    if (p.isBanned()) {
                        Message.m(MType.R, sender, "Info", "Ban Reason: 'You are the weakest link. Goodbye.'");
                    }
                    Message.m(MType.R, sender, "Info", "Muted: " + API.ismuted(p));
                    Message.m(MType.R, sender, "Info", "Flying: " + API.isflying(p));
                    Message.m(MType.R, sender, "Info", "Location: " + API.locationstring(p));
                    if (!API.isonline(p)) {
                        Message.m(MType.R, sender, "Info", "Last found at: " + API.lastlocationstring(p));
                    }
                    Message.m(MType.R, sender, "Info", "Food level: " + API.getfoodlevel(p));
                    Message.m(MType.R, sender, "Info", "Health level: " + API.gethealthlevel(p));
                    Message.m(MType.R, sender, "Info", "Time Played: hours: " + (API.gettimeplayed(p) / 60) / 60);
                    Message.m(MType.R, sender, "Info", "Time Played: days: " + ((API.gettimeplayed(p) / 60) / 60) / 24);
                }
            } else {
                Message.e(sender, "Info", Crit.P);
            }
        }
        return true;
    }
}