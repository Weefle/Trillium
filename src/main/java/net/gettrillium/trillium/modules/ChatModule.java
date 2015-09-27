package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.cooldown.Cooldown;
import net.gettrillium.trillium.api.cooldown.CooldownType;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatModule extends TrilliumModule {

    @Command(name = "Say",
            command = "say",
            description = "Talk from the console",
            usage = "/say")
    public void say(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {

            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            String message = sb.toString().trim();

            new Message(Mood.NEUTRAL, ChatColor.LIGHT_PURPLE + "Console", message).broadcast();

        } else {
            new Message(Mood.BAD, TrilliumAPI.getName("say"), "Say is for the console only. Not you.").to(cs);
        }
    }

    @Command(name = "Motd",
            command = "motd",
            description = "View the server's motd",
            usage = "/motd",
            permissions = {Permission.Chat.MOTD})
    public void motd(CommandSender cs, String[] args) {
        String cmd = "motd";
        if (cs.hasPermission(TrilliumAPI.getPermissions(cmd)[0])) {
            List<String> motd = getConfig().getStringList(Configuration.Chat.MOTD);
            for (String s : motd) {
                s = s.replace("%USERNAME%", cs.getName());
                s = s.replace("%SLOTS%", "" + Bukkit.getMaxPlayers());
                s = s.replace("%ONLINE%", "" + Bukkit.getOnlinePlayers().size());
                s = ChatColor.translateAlternateColorCodes('&', s);
                cs.sendMessage(s);
            }
        } else {
            new Message(TrilliumAPI.getName("motd"), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Clear Chat",
            command = "clearchat",
            description = "Clear global chat or a single player's chat",
            usage = "/clearchat",
            permissions = {Permission.Chat.CLEARCHAT})
    public void clearchat(CommandSender cs, String[] args) {
        String cmd = "clearchat";
        if (cs instanceof Player) {
            if (cs.hasPermission(Permission.Chat.CLEARCHAT)) {
                if (args.length != 0) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {
                        Utils.clearChat(target);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + "'s chat has been cleared!").to(cs);
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                    }

                } else if (args[0].equalsIgnoreCase("global")) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        Utils.clearChat(pl);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Info",
            command = "information",
            description = "View information about a certain player.",
            usage = "/info",
            aliases = "info",
            permissions = {Permission.Chat.INFO})
    public void information(CommandSender cs, String[] args) {
        String cmd = "info";
        if (cs.hasPermission(Permission.Chat.INFO)) {
            if (args.length == 0) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            } else {
                TrilliumPlayer p = player(args[0]);
                if (p != null) {
                    p.getProxy().sendMessage(" ");
                    new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "Displaying Information On: " + ChatColor.AQUA + p.getName()).to(cs);
                    new Message(Mood.NEUTRAL, "Nickname", "" + ChatColor.AQUA + p.getDisplayName()).to(cs);
                    new Message(Mood.NEUTRAL, "Online", "" + ChatColor.AQUA + p.isVanished()).to(cs);
                    new Message(Mood.NEUTRAL, "Gamemode", "" + ChatColor.AQUA + p.getProxy().getGameMode()).to(cs);
                    new Message(Mood.NEUTRAL, "Banned", "" + ChatColor.AQUA + p.getProxy().isBanned()).to(cs);
                    if (p.getProxy().isBanned()) {
                        new Message(Mood.NEUTRAL, "Ban Reason", ChatColor.RED + "You are the weakest link. Goodbye.").to(cs);
                    }
                    new Message(Mood.NEUTRAL, "Muted", "" + ChatColor.AQUA + p.isMuted()).to(cs);
                    new Message(Mood.NEUTRAL, "Flying", "" + ChatColor.AQUA + p.isFlying()).to(cs);
                    new Message(Mood.NEUTRAL, "Location", "" + ChatColor.AQUA + Utils.locationToString(p.getProxy().getLocation())).to(cs);

                    if (p.isVanished() || !p.getProxy().isOnline()) {
                        new Message(Mood.NEUTRAL, "Last Found At", "" + ChatColor.AQUA + p.getLastLocation().getBlockX() + "," + p.getLastLocation().getBlockY() + ", " + p.getLastLocation().getBlockZ()).to(cs);
                    }
                    new Message(Mood.NEUTRAL, "Food Level", "" + ChatColor.AQUA + p.getProxy().getFoodLevel()).to(cs);
                    new Message(Mood.NEUTRAL, "Health Level", "" + ChatColor.AQUA + p.getProxy().getHealthScale()).to(cs);
                    new Message(Mood.NEUTRAL, "Time Played in Hours", "" + ChatColor.AQUA + (p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60).to(cs);
                    new Message(Mood.NEUTRAL, "Time Played in Days", "" + ChatColor.AQUA + ((p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60) / 24).to(cs);
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]);
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Me",
            command = "me",
            description = "Share your feelings/thoughts to everyone in the third person.",
            usage = "/me",
            permissions = {Permission.Chat.ME})
    public void me(CommandSender cs, String[] args) {
        String cmd = "me";
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Chat.ME)) {

                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString().trim();

                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "** " + ChatColor.GRAY + p.getProxy().getName() + " " + message + ChatColor.DARK_GRAY + " **");

            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Message",
            command = "message",
            description = "Send a private message to a player.",
            usage = "/msg <player> <msg>",
            aliases = {"msg", "m"},
            permissions = {Permission.Chat.MESSAGE})
    public void message(CommandSender cs, String[] args) {
        String cmd = "message";
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Chat.MESSAGE)) {
                if (args.length < 2) {
                    new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);

                } else {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        String msg = sb.toString().trim();

                        // TODO: rewrite

                        String format1 = ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PluginMessages.TO_FROM_FROM_MESSAGE));
                        String format2 = ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PluginMessages.FROM_TO_TO_MESSAGE));

                        format1 = format1.replace("%TO%", p.getName());
                        format1 = format1.replace("%FROM%", target.getName());
                        format1 = format1.replace("%MESSAGE%", msg);

                        format2 = format2.replace("%TO%", p.getName());
                        format2 = format2.replace("%FROM%", target.getName());
                        format2 = format2.replace("%MESSAGE%", msg);

                        p.getProxy().sendMessage(format1);
                        target.getProxy().sendMessage(format2);

                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(p);
                    }
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    // TODO - Rewrite
    // TODO - Make next priority..
    @Command(name = "Nick",
            command = "nickname",
            description = "Change your nickname to anything you want.",
            usage = "/nick <nickname> [player]",
            aliases = {"nick"},
            permissions = {Permission.Chat.NICK, Permission.Chat.NICK_COLOR, Permission.Chat.NICK_OTHER, Permission.Chat.NICK_OTHER_COLOR})
    public void nickname(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (p.hasPermission(Permission.Chat.NICK)
                        && !p.getProxy().isOp()
                        && !p.hasPermission(Permission.Chat.NICK_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE).equals("") || !getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE).equals(" ")) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor + "&f"));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            new Message(Mood.BAD, "Nickname", "You don't have a nickname set.").to(p);
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)) {

                            new Message(Mood.GOOD, "Nickname", "New nickname set: " + args[0]).to(p);
                            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.NICKNAMES_PREFIX)) + args[0] + "&f");

                        } else {
                            new Message(Mood.BAD, "Nickname", "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)
                                    + " is the limit.").to(p);
                        }
                    }

                } else if (p.hasPermission(Permission.Chat.NICK_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor + "&f"));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            new Message(Mood.BAD, "Nickname", "You don't have a nickname set.").to(p);
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)) {

                            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                            new Message(Mood.GOOD, "Nickname", "New nickname set: " + nick).to(p);
                            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.NICKNAMES_PREFIX)) + nick + "&f");

                        } else {
                            new Message(Mood.GOOD, "Nickname", "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)
                                    + " is the limit.").to(p);
                        }
                    }

                } else {
                    new Message("Nickname", Error.NO_PERMISSION).to(p);
                }

            } else if (args.length > 1) {
                if (p.hasPermission(Permission.Chat.NICK_OTHER) && !p.getProxy().isOp()) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor + "&f"));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            new Message(Mood.BAD, "Nickname", "You don't have a nickname set.").to(p);
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                target.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.NICKNAMES_PREFIX)) + args[0] + "&f");
                                new Message(Mood.GOOD, "Nickname", ChatColor.AQUA + p.getName() + ChatColor.BLUE + " set your nickname to: " + args[0]).to(target);
                                new Message(Mood.GOOD, "Nickname", "You set " + ChatColor.AQUA + target.getName() + ChatColor.BLUE + " to: " + args[0]).to(p);

                            } else {
                                new Message("Nickname", Error.INVALID_PLAYER, args[0]);
                            }
                        } else {
                            new Message(Mood.GOOD, "Nickname", "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)
                                    + " is the limit.").to(p);
                        }
                    }

                } else if (p.hasPermission(Permission.Chat.NICK_OTHER_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.NICKNAMES_OPS_COLOR_CODE) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor + "&f"));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            new Message(Mood.BAD, "Nickname", "You don't have a nickname set.").to(p);
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                                target.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.NICKNAMES_PREFIX)) + nick + "&f");
                                new Message(Mood.GOOD, "Nickname", ChatColor.AQUA + p.getName() + ChatColor.BLUE + " set your nickname to: " + nick).to(target);
                                new Message(Mood.GOOD, "Nickname", "You set " + ChatColor.AQUA + target.getName() + ChatColor.BLUE + " to: " + nick).to(p);

                            } else {
                                new Message("Nickname", Error.INVALID_PLAYER, args[0]).to(p);
                            }
                        } else {
                            new Message(Mood.GOOD, "Nickname", "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT)
                                    + " is the limit.").to(p);
                        }
                    }
                } else {
                    new Message("Nickname", Error.NO_PERMISSION).to(p);
                }
            } else {
                new Message("Nickname", Error.TOO_FEW_ARGUMENTS, "/nick <nickname> [player]").to(p);
            }
        } else {
            new Message("Nickname", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Chat Channel",
            command = "chatchannel",
            description = "Talk to a group of people in private.",
            usage = "/cc <channel> <msg>",
            aliases = {"cc"},
            permissions = {Permission.Chat.CHATCHANNEL + "<input>"})
    public void chatchannel(CommandSender cs, String[] args) {
        String cmd = "chatchannel";
        if (getConfig().getBoolean(Configuration.Chat.CHAT_CHANNELS_ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);
                if (args.length >= 2) {
                    if (p.hasPermission(Permission.Chat.CHATCHANNEL + args[0])) {

                        for (TrilliumPlayer pl : TrilliumAPI.getOnlinePlayers()) {
                            if (pl.hasPermission(Permission.Chat.CHATCHANNEL + args[0])) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }

                                String msg = sb.toString().trim();

                                String f = getConfig().getString(Configuration.Chat.CHAT_CHANNELS_FORMAT);

                                f = f.replace("%CHANNELNAME%", args[0]);
                                f = f.replace("%NICKNAME%", p.getProxy().getName());
                                f = f.replace("%MESSAGE%", msg);
                                if (getConfig().getBoolean(Configuration.Chat.CHAT_CHANNELS_ALLOW_COLOR_CODES)) {
                                    f = ChatColor.translateAlternateColorCodes('&', f);
                                }

                                pl.getProxy().sendMessage(f);
                            }
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd));
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
            }
        } else {
            new Message(Mood.BAD, TrilliumAPI.getName(cmd), "This feature is disabled.");
        }
    }

    @Command(name = "Broadcast",
            command = "broadcast",
            description = "Broadcast a message to the world.",
            usage = "/broadcast <message>",
            aliases = {"bc"},
            permissions = {Permission.Chat.BROADCAST})
    public void broadcast(CommandSender cs, String[] args) {
        String cmd = "broadcast";
        if (!cs.hasPermission(Permission.Chat.BROADCAST)) {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
            return;
        }
        if (args.length == 0) {
            new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            return;
        }

        String defaultcolor = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString(Configuration.Broadcast.REGULAR_BROADCASTS_COLOR_TO_USE).trim());

        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String message = sb.toString().trim();

        if (getConfig().getBoolean(Configuration.Broadcast.REGULAR_BROADCASTS_CENTRALIZE_BROADCASTS)) {

            List<String> centered = Utils.centerText(message);
            List<String> format = getConfig().getStringList(Configuration.Broadcast.REGULAR_BROADCASTS_BROADCAST_FORMAT);
            for (String s : format) {
                s = ChatColor.translateAlternateColorCodes('&', s);
                if (s.contains("[msg]")) {
                    for (String slices : centered) {
                        s = s.replace("[msg]", "");
                        Bukkit.broadcastMessage(defaultcolor + slices);
                    }
                } else {
                    Bukkit.broadcastMessage(s);
                }
            }
        } else {

            List<String> format = getConfig().getStringList(Configuration.Broadcast.REGULAR_BROADCASTS_BROADCAST_FORMAT);
            for (String s : format) {
                s = s.replace("[msg]", ChatColor.translateAlternateColorCodes('&',
                        getConfig().getString(Configuration.Broadcast.REGULAR_BROADCASTS_COLOR_TO_USE) + message));
                s = ChatColor.translateAlternateColorCodes('&', s);
                Bukkit.broadcastMessage(s);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission(Permission.Chat.COLOR)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }

        if (Cooldown.hasCooldown(event.getPlayer(), CooldownType.CHAT)) {
            new Message(Mood.BAD, "Chat", "Your spamming! You can resume chatting in: " + ChatColor.AQUA + Cooldown.getTime(event.getPlayer(), CooldownType.CHAT)).to(event.getPlayer());
            event.setCancelled(true);
        } else {
            if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission(Permission.Chat.COOLDOWN_EXEMPT)) {
                Cooldown.setCooldown(event.getPlayer(), CooldownType.CHAT, false);
            }
        }

        if (getConfig().getBoolean(Configuration.Chat.CHAT_FORMAT_ENABLED)) {


            for (String perm : getConfig().getConfigurationSection(Configuration.Chat.CHAT_FORMAT_PERMISSION_SPECIFIC_FORMATS).getKeys(false)) {
                for (String group : getConfig().getConfigurationSection(Configuration.Chat.CHAT_FORMAT_GROUP_SPECIFIC_FORMATS).getKeys(false)) {

                    boolean global = true;
                    if (!event.getPlayer().isOp()) {
                        if (event.getPlayer().hasPermission(Permission.Chat.PERM_FORMAT + perm)) {
                            global = false;
                            String format = getConfig().getString(Configuration.Chat.CHAT_FORMAT_PERMISSION_SPECIFIC_FORMATS + "." + perm);
                            format = format.replace("%USERNAME%", event.getPlayer().getName());
                            format = format.replace("%NICKNAME%", event.getPlayer().getDisplayName());
                            if (Trillium.chat != null) {
                                format = format.replace("%GROUPNAME%", Trillium.chat.getPrimaryGroup(event.getPlayer()));
                            }
                            format = format.replace("%MESSAGE%", event.getMessage());
                            format = ChatColor.translateAlternateColorCodes('&', format);
                            event.setFormat(format);

                        } else if (Trillium.chat != null) {
                            if (Trillium.chat.playerInGroup(event.getPlayer(), group)) {
                                global = false;
                                String format = getConfig().getString(Configuration.Chat.CHAT_FORMAT_GROUP_SPECIFIC_FORMATS + "." + group);
                                format = format.replace("%USERNAME%", event.getPlayer().getName());
                                format = format.replace("%NICKNAME%", event.getPlayer().getDisplayName());
                                format = format.replace("%GROUPNAME%", Trillium.chat.getPrimaryGroup(event.getPlayer()));
                                format = format.replace("%MESSAGE%", event.getMessage());
                                format = ChatColor.translateAlternateColorCodes('&', format);
                                event.setFormat(format);
                            }
                        }
                    } else {
                        global = true;
                    }

                    if (global) {
                        String format = getConfig().getString(Configuration.Chat.CHAT_FORMAT_GLOBAL_FORMAT);
                        format = format.replace("%USERNAME%", event.getPlayer().getName());
                        format = format.replace("%NICKNAME%", event.getPlayer().getDisplayName());
                        if (Trillium.chat != null) {
                            format = format.replace("%GROUPNAME%", Trillium.chat.getPrimaryGroup(event.getPlayer()));
                        }
                        format = format.replace("%MESSAGE%", event.getMessage());
                        format = ChatColor.translateAlternateColorCodes('&', format);
                        event.setFormat(format);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (Cooldown.hasCooldown(event.getPlayer(), CooldownType.CHAT)) {
            new Message(Mood.BAD, "Chat", "Your spamming! You can send messages in: " + ChatColor.AQUA + Cooldown.getTime(event.getPlayer(), CooldownType.CHAT)).to(event.getPlayer());
            event.setCancelled(true);
        } else {
            if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission(Permission.Chat.COOLDOWN_EXEMPT)) {
                Cooldown.setCooldown(event.getPlayer(), CooldownType.CHAT, false);
            }
        }
    }
}
