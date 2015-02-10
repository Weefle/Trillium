package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishModule extends TrilliumModule {

    @Command(command = "mute", description = "Mute a player", usage = "/mute [player]")
    public void mute(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.MUTE)) {
            if (args.length == 0) {
                Message.earg(sender, "Mute", "/mute <player>");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    TrilliumPlayer player = player(target);
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

    @Command(command = "kick", description = "Kick a player", usage = "/kick [player]")
    public void kick(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (sender.hasPermission(Permission.Punish.KICK)) {
                if (args.length < 2) {
                    Message.earg(p, "Kick", "/kick <player>");
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        String reason = sb.toString().trim();
                        Message.b(MType.W, "Kick", target.getName() + " got kicked for:");
                        Message.b(MType.W, "Kick", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                        p.kickPlayer(reason);
                    } else {
                        Message.eplayer(p, "Kick", args[0]);
                    }
                }
            } else {
                Message.e(p, "Kick", Crit.P);
            }
        } else {
            Message.e(sender, "Kick", Crit.C);
        }
    }

    @Command(command = "ban", description = "Ban a player", usage = "/ban [player]")
    public void ban(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.BAN)) {
            if (args.length == 0) {
                Message.earg(sender, "Ban", "/ban <player> [reason]");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                String reason;
                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    reason = sb.toString().trim();
                } else {
                    reason = "You are the weakest link. Good bye.";
                }

                if (target != null) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, null, sender.getName());
                    target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                    Message.b(MType.W, "Ban", target.getName() + " got banned with reason:");
                    Message.b(MType.W, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, sender.getName());
                    Message.b(MType.W, "Ban", args[0] + " got banned with reason:");
                    Message.b(MType.W, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                }
            }
        } else {
            Message.e(sender, "Ban", Crit.P);
        }
    }

    @Command(command = "unban", description = "Unban a player", usage = "/unban [player]")
    public void unban(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.UNBAN)) {
            if (args.length == 0) {
                Message.earg(sender, "Unban", "/unban <player>");
            } else {
                Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                Message.b(MType.G, "Unban", args[0] + " got unbanned.");
            }
        } else {
            Message.e(sender, "Unban", Crit.P);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        TrilliumPlayer player = player(e.getPlayer());
        if (player.isMuted()) {
            e.setCancelled(true);
            Message.m(MType.W, player.getProxy(), "Mute", "Your voice has been silenced.");
        }
    }
}
