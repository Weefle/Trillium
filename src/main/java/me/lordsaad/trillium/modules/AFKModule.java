package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.command.CommandException;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AFKModule extends TrilliumModule {

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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        TrilliumPlayer player = TrilliumAPI.getPlayer(event.getPlayer().getName());
        if (player.isAfk()) {
            player.toggleAfk();
        }
    }
}
