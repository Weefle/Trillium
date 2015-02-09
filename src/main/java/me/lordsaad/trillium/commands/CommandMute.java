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

import java.util.ArrayList;
import java.util.UUID;

public class CommandMute implements CommandExecutor {

    public static ArrayList<UUID> muted = new ArrayList<UUID>();
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mute")) {
            if (sender.hasPermission("tr.mute")) {
                if (args.length == 0) {
                    Message.earg(sender, "Mute", "/mute <player>");
                } else {

                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        
                        if (!API.ismuted(target)) {
                            API.setmuted(true, target);
                            Message.m(MType.G, sender, "Mute", "You muted " + target.getName());
                            Message.m(MType.W, sender, "Mute", sender.getName() + " muted you.");
                        } else {
                            API.setmuted(false, target);
                            Message.m(MType.G, sender, "Mute", "You unmuted " + target.getName());
                            Message.m(MType.G, sender, "Mute", sender.getName() + " unmuted you.");
                        }
                    } else {
                        Message.eplayer(sender, "Mute", args[0]);
                    }
                }

            } else {
                Message.e(sender, "Mute", Crit.P);
            }
        }
        return true;
    }
}