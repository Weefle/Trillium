package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandLag implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("lag")) {
            if (sender.hasPermission("tr.lag")) {

                long time = System.currentTimeMillis();

                Message.m(MType.R, sender, "Lag", "Before GC:");
                Utils.printCurrentMemory(sender);
                sender.sendMessage(" ");

                System.gc();
                Message.m(MType.G, sender, "Lag", "GC complete.");

                sender.sendMessage(" ");
                Message.m(MType.R, sender, "Lag", "After GC:");
                Utils.printCurrentMemory(sender);
                sender.sendMessage(" ");

                long need = System.currentTimeMillis() - time;
                Message.m(MType.R, sender, "Lag", "GC took " + need / 1000L + " seconds.");

            } else {
                Message.e(sender, "Lag", Crit.P);
            }
        }
        return true;
    }
}