package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.TrilliumPlayer;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishModule extends TrilliumModule {

    @Command(name = "Mute",
            command = "mute",
            description = "Silence/unsilence someone's voice.",
            usage = "/mute <player>",
            permissions = {Permission.Punish.MUTE})
    public void mute(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.MUTE)) {
            if (args.length == 0) {
                new Message("Mute", Error.TOO_FEW_ARGUMENTS, "/mute <player>").to(cs);
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    TrilliumPlayer player = player(target);
                    if (!player.isMuted()) {
                        player.setMuted(true);
                        new Message(Mood.GOOD, "Mute", "You muted " + target.getName()).to(cs);
                        new Message(Mood.BAD, "Mute", cs.getName() + " muted you.").to(cs);
                    } else {
                        player.setMuted(false);
                        new Message(Mood.GOOD, "Mute", "You unmuted " + target.getName()).to(cs);
                        new Message(Mood.GOOD, "Mute", cs.getName() + " unmuted you.").to(cs);
                    }
                } else {
                    new Message("Mute", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            }
        } else {
            new Message("Mute", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Shadow Mute",
            command = "shadowmute",
            description = "Silently Silence/unsilence someone's voice.",
            usage = "/smute <player>",
            permissions = {Permission.Punish.SHADOW_MUTE})
    public void shadowmute(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.MUTE)) {
            if (args.length == 0) {
                new Message("Shadow Mute", Error.TOO_FEW_ARGUMENTS, "/smute <player>").to(cs);
            } else {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {
                    if (target.isShadowMuted()) {
                        target.setShadowMuted(false);
                        new Message(Mood.GOOD, "Shadow Mute", target.getName() + " has been silently unmuted.").to(cs);
                    } else {
                        target.setShadowBanned(true);
                        new Message(Mood.GOOD, "Shadow Mute", target.getName() + " has been silently muted.").to(cs);
                    }
                } else {
                    new Message("Shadow Mute", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            }
        } else {
            new Message("Shadow Mute", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Kick",
            command = "kick",
            description = "Kick a player from the server.",
            usage = "/kick <player> [reason]",
            permissions = {Permission.Punish.KICK})
    public void kick(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.KICK)) {
            if (args.length < 2) {
                new Message("Kick", Error.TOO_FEW_ARGUMENTS, "/kick <player> [reason]").to(cs);
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String reason = sb.toString().trim();
                    new Message(Mood.BAD, "Kick", target.getName() + " got kicked for: ").broadcast();
                    new Message(Mood.BAD, "Kick", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                    target.kickPlayer(reason);
                } else {
                    new Message("Kick", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            }
        } else {
            new Message("Kick", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Ban",
            command = "ban",
            description = "Ban a player from the server.",
            usage = "/ban <player> [reason]",
            permissions = {Permission.Punish.BAN})
    public void ban(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.BAN)) {
            if (args.length == 0) {
                new Message("Ban", Error.TOO_FEW_ARGUMENTS, "/ban <player> [reason]").to(cs);
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
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, null, cs.getName());
                    target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                    new Message(Mood.BAD, "Ban", target.getName() + " got banned with reason:").broadcast();
                    new Message(Mood.BAD, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, cs.getName());
                    new Message(Mood.BAD, "Ban", args[0] + " got banned with reason:").broadcast();
                    new Message(Mood.BAD, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                }
            }
        } else {
            new Message("Ban", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Shadow Ban",
            command = "shadowban",
            description = "Silently ban a player by making them 100% invisible to other players.",
            usage = "/sban <player>", aliases = {"sban"}, permissions = {Permission.Punish.SHADOW_BAN})
    public void shadowban(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.SHADOW_BAN)) {
            if (args.length != 0) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {

                    target.setShadowBanned(true);
                    new Message(Mood.GOOD, "Shadow Ban", target.getName() + " has been silently banned.").to(cs);

                } else {
                    new Message("Shadow Ban", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message("Shadow Ban", Error.TOO_FEW_ARGUMENTS, "/sban <player>").to(cs);
            }
        } else {
            new Message("Shadow Ban", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Shadow ban",
            command = "shadowunban",
            description = "unshadow ban a shadow banned player.",
            usage = "/sunban <player>", aliases = {"sunban"}, permissions = {Permission.Punish.SHADOW_BAN})
    public void shadowunban(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.SHADOW_BAN)) {
            if (args.length != 0) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {

                    target.setShadowBanned(false);
                    new Message(Mood.GOOD, "Shadow Ban", target.getName() + " has been silently unbanned.").to(cs);

                } else {
                    new Message("Shadow Ban", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message("Shadow Ban", Error.TOO_FEW_ARGUMENTS, "/sunban <player>").to(cs);
            }
        } else {
            new Message("Shadow Ban", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Unban",
            command = "unban",
            description = "Unban a player.",
            usage = "/unban <player>",
            aliases = "pardon",
            permissions = {Permission.Punish.UNBAN})
    public void unban(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.UNBAN)) {
            if (args.length == 0) {
                new Message("Unban", Error.TOO_FEW_ARGUMENTS, "/unban <player>").to(cs);
            } else {
                Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                new Message(Mood.GOOD, "Unban", args[0] + " got unbanned.").broadcast();
            }
        } else {
            new Message("Unban", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Ban IP",
            command = "banip",
            description = "Ban the ip of a player from the server.",
            usage = "/banip <player> [reason]",
            permissions = {Permission.Punish.BANIP})
    public void banip(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.BANIP)) {
            if (args.length == 0) {
                new Message("Ban IP", Error.TOO_FEW_ARGUMENTS, "/banip <player> [reason]").to(cs);
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
                    Bukkit.getBanList(BanList.Type.IP).addBan(String.valueOf(target.getAddress()), reason, null, cs.getName());
                    target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                    new Message(Mood.BAD, "Ban IP", target.getName() + " got banned with reason:").broadcast();
                    new Message(Mood.BAD, "Ban IP", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, cs.getName());
                    new Message(Mood.BAD, "Ban IP", args[0] + " got banned with reason:").broadcast();
                    new Message(Mood.BAD, "Ban IP", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                }
            }
        } else {
            new Message("Ban IP", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(name = "Unban IP",
            command = "unbanip",
            description = "Unban the IP of a player.",
            usage = "/unbanip <IP>",
            aliases = "pardonip",
            permissions = {Permission.Punish.UNBANIP})
    public void unbanip(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.UNBANIP)) {
            if (args.length == 0) {
                new Message("Unban IP", Error.TOO_FEW_ARGUMENTS, "/unbanip <IP>").to(cs);
            } else {
                Bukkit.getBanList(BanList.Type.IP).pardon(args[0]);
                new Message(Mood.GOOD, "Unban IP", args[0] + " got unbanned.").to(cs);
            }
        } else {
            new Message("Unban IP", Error.NO_PERMISSION).to(cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        TrilliumPlayer p = player(e.getPlayer());

        if (p.isMuted() || p.isShadowBanned()) {
            new Message(Mood.BAD, "Mute", "Your voice has been silenced.").to(p);
            e.setCancelled(true);
        }
    }
}
