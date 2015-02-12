package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.spawn")) {

                    p.teleport(p.getWorld().getSpawnLocation());
                    Message.m(MType.G, p, "Spawn", "Successfully teleported you to the spawn.");

                } else {
                    Message.e(p, "Spawn", Crit.P);
                }
            } else {
                Message.e(sender, "Spawn", Crit.C);
            }
        }
        return true;
    }
}