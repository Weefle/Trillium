package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.command.CommandException;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAfk {
    @Command(command = "afk", description = "Toggle your AFK status", usage = "/afk [player]")
    public static void onCommand(CommandSender sender, String[] args) throws CommandException {
        if (sender instanceof Player) {
            TrilliumPlayer player = TrilliumAPI.getPlayer(sender.getName());
            if (player.getProxy().hasPermission("trillium.afk")) {
                player.toggleAfk();
            } else {
                Message.e(sender, "AFK", Crit.P);
            }
        } else {
            Message.e(sender, "AFK", Crit.C);
        }
    }
}
