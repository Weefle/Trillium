package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class CommandAfk implements CommandExecutor {

    public static HashMap<UUID, Integer> afktimer = new HashMap<UUID, Integer>();
    public static ArrayList<UUID> afklist = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("afk")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.afk")) {
                    if (!afklist.contains(p.getUniqueId())) {
                        afklist.add(p.getUniqueId());
                        Message.b(MType.G, "AFK", p.getName() + " is now AFK.");
                    } else {
                        afklist.remove(p.getUniqueId());
                        Message.b(MType.G, "AFK", p.getName() + " is no longer AFK.");
                    }
                } else {
                    Message.e(p, "AFK", Crit.P);
                }
            } else {
                Message.e(sender, "AFK", Crit.C);
            }
        }
        return true;
    }
}
