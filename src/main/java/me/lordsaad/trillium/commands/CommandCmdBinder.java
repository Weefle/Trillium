package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCmdBinder implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("commandbinder")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.commandbinder")) {
                    if (args.length < 3) {
                        Message.earg(p, "Cmd Binder", "/cb <touch/walk> <console/player> <command>");
                        Message.m(MType.W, p, "Cmd Binder", "or /cb <t/w> <c/p> <command>");
                        Message.m(MType.W, p, "Cmd Binder", "Example: /cb t c tp [p] 110 45 247");
                    } else {

                        Message.m(MType.W, p, "Cmd Binder", "This feature is coming soon....");
                        Message.m(MType.W, p, "Cmd Binder", "It's a really big feature, so you have to be patient.");
                        Message.m(MType.W, p, "Cmd Binder", "Sorry for the inconvenience. :(");
                    }
                } else {
                    Message.e(p, "Cmd Binder", Crit.P);
                }
            } else {
                Message.e(sender, "Cmd Binder", Crit.C);
            }
        }
        return true;
    }
}