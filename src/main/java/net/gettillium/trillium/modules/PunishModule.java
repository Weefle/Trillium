package net.gettillium.trillium.modules;

import net.gettillium.trillium.api.Permission;
import net.gettillium.trillium.api.TrilliumModule;
import net.gettillium.trillium.api.command.Command;
import net.gettillium.trillium.api.player.TrilliumPlayer;
import net.gettillium.trillium.messageutils.Crit;
import net.gettillium.trillium.messageutils.MType;
import net.gettillium.trillium.messageutils.Message;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishModule extends TrilliumModule {

    public PunishModule() {
        super("punish");
    }

    @Command(command = "mute", description = "Silence/unsilence someone's voice.", usage = "/mute <player>")
    public void mute(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.MUTE)) {
            if (args.length == 0) {
                Message.earg(sender, "Mute", "/mute <player>");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    TrilliumPlayer player = player(target);
                    if (!player.isMuted()) {
                        player.setMuted(true);
                        Message.m(MType.G, sender, "Mute", "You muted " + target.getName());
                        Message.m(MType.W, sender, "Mute", sender.getName() + " muted you.");
                    } else {
                        player.setMuted(false);
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

    @Command(command = "kick", description = "Kick a player from the server.", usage = "/kick <player> [reason]")
    public void kick(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.KICK)) {
            if (args.length < 2) {
                Message.earg(sender, "Kick", "/kick <player> [reason]");
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
                    target.kickPlayer(reason);
                } else {
                    Message.eplayer(sender, "Kick", args[0]);
                }
            }
        } else {
            Message.e(sender, "Kick", Crit.P);
        }
    }

    @Command(command = "ban", description = "Ban a player from the server.", usage = "/ban <player> [reason]")
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

    @Command(command = "unban", description = "Unban a player.", usage = "/unban <player>", aliases = "pardon")
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

    @Command(command = "banip", description = "Ban the ip of a player from the server.", usage = "/banip <player> [reason]")
    public void banip(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.BANIP)) {
            if (args.length == 0) {
                Message.earg(sender, "BanIP", "/banip <player> [reason]");
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
                    Bukkit.getBanList(BanList.Type.IP).addBan(String.valueOf(target.getAddress()), reason, null, sender.getName());
                    target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                    Message.b(MType.W, "BanIP", target.getName() + " got banned with reason:");
                    Message.b(MType.W, "BanIP", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, sender.getName());
                    Message.b(MType.W, "BanIP", args[0] + " got banned with reason:");
                    Message.b(MType.W, "BanIP", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                }
            }
        } else {
            Message.e(sender, "BanIP", Crit.P);
        }
    }

    @Command(command = "unbanip", description = "Unban the IP of a player.", usage = "/unbanip <IP>", aliases = "pardonip")
    public void unbanip(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Punish.UNBANIP)) {
            if (args.length == 0) {
                Message.earg(sender, "Unban", "/unbanip <IP>");
            } else {
                Bukkit.getBanList(BanList.Type.IP).pardon(args[0]);
                Message.b(MType.G, "Unban", args[0] + " got unbanned.");
            }
        } else {
            Message.e(sender, "Unban", Crit.P);
        }
    }
}
