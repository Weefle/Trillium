package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishModule extends TrilliumModule {

    @Command(command = "mute", description = "Mute a player", usage = "/mute [player]")
    public static void mute(CommandSender sender, String[] args) {
        if (sender.hasPermission("tr.mute")) {
            if (args.length == 0) {
                Message.earg(sender, "Mute", "/mute <player>");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    TrilliumPlayer player = TrilliumAPI.getPlayer(args[0]);
                    if (!player.isMuted()) {
                        player.mute();
                        Message.m(MType.G, sender, "Mute", "You muted " + target.getName());
                        Message.m(MType.W, sender, "Mute", sender.getName() + " muted you.");
                    } else {
                        player.unmute();
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        TrilliumPlayer player = TrilliumAPI.getPlayer(e.getPlayer().getName());
        if (player.isMuted()) {
            e.setCancelled(true);
            Message.m(MType.W, player.getProxy(), "Mute", "Your voice has been silenced.");
        }
    }
}
