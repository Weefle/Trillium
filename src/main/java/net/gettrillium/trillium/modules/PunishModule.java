package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
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

    private boolean chatIsSilenced;

    @Command(name = "Mute",
            command = "mute",
            description = "Silence/unsilence someone's voice.",
            usage = "/mute <player/all> [player]",
            permissions = {Permission.Punish.MUTE})
    public void mute(CommandSender cs, String[] args) {
        String cmd = "mute";
        if (cs.hasPermission(Permission.Punish.MUTE)) {
            if (args.length < 1) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("p")) {
                    if (args.length < 2) {
                        new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
                        return;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        TrilliumPlayer player = player(target);
                        if (!player.isMuted()) {
                            player.setMuted(true);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You muted " + target.getName()).to(cs);
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), cs.getName() + " muted you.").to(cs);
                        } else {
                            player.setMuted(false);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You unmuted " + target.getName()).to(cs);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), cs.getName() + " unmuted you.").to(cs);
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                    }
                } else if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("a")) {

                    if (chatIsSilenced) {
                        chatIsSilenced = false;
                        new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), cs.getName() + " has unsilenced the chat!").broadcast();
                    } else {
                        new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), cs.getName() + " has silenced the chat!").broadcast();
                        chatIsSilenced = true;
                    }
                } else {
                    new Message("Mute", Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Shadow Mute",
            command = "shadowmute",
            description = "Silently Silence/unsilence someone's voice.",
            usage = "/smute <player>",
            permissions = {Permission.Punish.SHADOW_MUTE})
    public void shadowmute(CommandSender cs, String[] args) {
        String cmd = "shadowmute";
        if (cs.hasPermission(Permission.Punish.MUTE)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {
                    if (target.isShadowMuted()) {
                        target.setShadowMuted(false);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " has been silently unmuted.").to(cs);
                    } else {
                        target.setShadowBanned(true);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " has been silently muted.").to(cs);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Kick",
            command = "kick",
            description = "Kick a player from the server.",
            usage = "/kick <player> [reason]",
            permissions = {Permission.Punish.KICK})
    public void kick(CommandSender cs, String[] args) {
        String cmd = "kick";
        if (cs.hasPermission(Permission.Punish.KICK)) {
            if (args.length < 2) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String reason = sb.toString().trim();
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), target.getName() + " got kicked for: ").broadcast();
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                    target.kickPlayer(reason);
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Ban",
            command = "ban",
            description = "Ban a player from the server.",
            usage = "/ban <player> [reason]",
            permissions = {Permission.Punish.BAN})
    public void ban(CommandSender cs, String[] args) {
        String cmd = "ban";
        if (cs.hasPermission(Permission.Punish.BAN)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
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
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Shadow Ban",
            command = "shadowban",
            description = "Silently ban a player by making them 100% invisible to other players.",
            usage = "/sban <player>",
            aliases = {"sban"},
            permissions = {Permission.Punish.SHADOW_BAN})
    public void shadowban(CommandSender cs, String[] args) {
        String cmd = "shadowban";
        if (cs.hasPermission(Permission.Punish.SHADOW_BAN)) {
            if (args.length != 0) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {

                    target.setShadowBanned(true);
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " has been silently banned.").to(cs);

                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Shadow ban",
            command = "shadowunban",
            description = "unshadow ban a shadow banned player.",
            usage = "/sunban <player>",
            aliases = {"sunban"},
            permissions = {Permission.Punish.SHADOW_BAN})
    public void shadowunban(CommandSender cs, String[] args) {
        String cmd = "shadowunban";
        if (cs.hasPermission(Permission.Punish.SHADOW_BAN)) {
            if (args.length != 0) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {

                    target.setShadowBanned(false);
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " has been silently unbanned.").to(cs);

                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Unban",
            command = "unban",
            description = "Unban a player.",
            usage = "/unban <player>",
            aliases = "pardon",
            permissions = {Permission.Punish.UNBAN})
    public void unban(CommandSender cs, String[] args) {
        String cmd = "unban";
        if (cs.hasPermission(Permission.Punish.UNBAN)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), args[0] + " got unbanned.").broadcast();
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Ban IP",
            command = "banip",
            description = "Ban the ip of a player from the server.",
            usage = "/banip <player> [reason]",
            permissions = {Permission.Punish.BANIP})
    public void banip(CommandSender cs, String[] args) {
        String cmd = "banip";
        if (cs.hasPermission(Permission.Punish.BANIP)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
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
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), target.getName() + " got banned with reason:").broadcast();
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, cs.getName());
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[0] + " got banned with reason:").broadcast();
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'").broadcast();
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Unban IP",
            command = "unbanip",
            description = "Unban the IP of a player.",
            usage = "/unbanip <IP>",
            aliases = "pardonip",
            permissions = {Permission.Punish.UNBANIP})
    public void unbanip(CommandSender cs, String[] args) {
        String cmd = "unbanip";
        if (cs.hasPermission(Permission.Punish.UNBANIP)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                Bukkit.getBanList(BanList.Type.IP).pardon(args[0]);
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), args[0] + " got unbanned.").to(cs);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    // TODO: make

    @Command(name = "Freeze",
            command = "freeze",
            description = "Disable/Enable the ability to move for anyone online or a certain player.",
            usage = "/freeze <player/all> [player]",
            permissions = {Permission.Punish.FREEZE})
    public void freeze(CommandSender cs, String[] args) {
        String cmd = "freeze";
        if (cs.hasPermission(Permission.Punish.FREEZE)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                TrilliumPlayer p = player(args[0]);
                if (p == null) {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                    return;
                }

                if (p.isFrozen()) {
                    p.setFrozen(false);
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You unfroze " + p.getName() + ".").to(cs);
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), cs.getName() + " unfroze you.").to(p);
                } else {
                    p.setFrozen(true);
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You froze " + p.getName() + ".").to(cs);
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), cs.getName() + " froze you.").to(p);
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        TrilliumPlayer p = player(e.getPlayer());

        if (p.isMuted() && !p.isShadowBanned()) {
            new Message(Mood.BAD, "Mute", "Your voice has been silenced.").to(p);
            e.setCancelled(true);
        }

        if (p.isShadowBanned() && !p.isMuted()) {
            p.getProxy().sendMessage("Unknown command. Try /help for a list of commands.");
            e.setCancelled(true);
        }
    }
}
