package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.messageutils.Type;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class ChatModule extends TrilliumModule {

    public ChatModule() {
        super("chat");
    }

    @Command(command = "say", description = "Talk from the console", usage = "/say")
    public void say(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {

            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            String message = sb.toString().trim();

            new Message(Mood.GENERIC, ChatColor.LIGHT_PURPLE + "Console", message).broadcast();

        } else {
            new Message(Mood.BAD, "Say", "Say is for the console only. Not you.").to(cs);
        }
    }

    @Command(command = "motd", description = "View the server's motd", usage = "/motd")
    public void motd(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.MOTD)) {
            List<String> motd = getConfig().getStringList(Configuration.Chat.INGAME_MOTD);
            for (String s : motd) {
                s = s.replace("[USERNAME]", cs.getName());
                s = s.replace("[SLOTS]", "" + Bukkit.getMaxPlayers());
                s = s.replace("[ONLINE]", "" + Bukkit.getOnlinePlayers().size());
                s = ChatColor.translateAlternateColorCodes('&', s);
                cs.sendMessage(s);
            }
        } else {
            new Message("Motd", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(command = "information", description = "View information about a certain player.", usage = "/info", aliases = "info")
    public void information(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.INFO)) {
            if (args.length == 0) {
                new Message("Info", Error.TOO_FEW_ARGUMENTS, "/info <player>").to(cs);
            } else {
                TrilliumPlayer p = player(args[0]);
                if (p != null) {
                    p.getProxy().sendMessage(" ");
                    new Message(Mood.GENERIC, "Info", "Displaying Information On: " + ChatColor.AQUA + p.getName()).to(cs);
                    new Message(Mood.GENERIC, "Nickname", "" + ChatColor.AQUA + p.getDisplayName()).to(cs);
                    new Message(Mood.GENERIC, "Online", "" + ChatColor.AQUA + p.isVanished()).to(cs);
                    new Message(Mood.GENERIC, "Gamemode", "" + ChatColor.AQUA + p.getProxy().getGameMode()).to(cs);
                    new Message(Mood.GENERIC, "Banned", "" + ChatColor.AQUA + p.getProxy().isBanned()).to(cs);
                    if (p.getProxy().isBanned()) {
                        new Message(Mood.GENERIC, "Ban Reason", ChatColor.RED + "You are the weakest link. Goodbye.").to(cs);
                    }
                    new Message(Mood.GENERIC, "Muted", "" + ChatColor.AQUA + p.isMuted()).to(cs);
                    new Message(Mood.GENERIC, "Flying", "" + ChatColor.AQUA + p.isFlying()).to(cs);
                    new Message(Mood.GENERIC, "Ping", "" + ChatColor.AQUA + Utils.getPing(p.getProxy())).to(cs);
                    new Message(Mood.GENERIC, "Lag Rate", "" + ChatColor.AQUA + Utils.getPingBar(p.getProxy())).to(cs);
                    new Message(Mood.GENERIC, "Location", "" + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ", " + p.getProxy().getLocation().getBlockY() + ", " + p.getProxy().getLocation().getBlockZ()).to(cs);

                    if (p.isVanished()) {
                        new Message(Mood.GENERIC, "Last Found At", "" + ChatColor.AQUA + p.getLastLocation().getBlockX() + "," + p.getLastLocation().getBlockY() + ", " + p.getLastLocation().getBlockZ()).to(cs);
                    }
                    new Message(Mood.GENERIC, "Food Level", "" + ChatColor.AQUA + p.getProxy().getFoodLevel()).to(cs);
                    new Message(Mood.GENERIC, "Health Level", "" + ChatColor.AQUA + p.getProxy().getHealthScale()).to(cs);
                    new Message(Mood.GENERIC, "Time Played in Hours", "" + ChatColor.AQUA + (p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60).to(cs);
                    new Message(Mood.GENERIC, "Time Played in Days", "" + ChatColor.AQUA + ((p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60) / 24).to(cs);
                } else {
                    new Message("Info", Error.INVALID_PLAYER, args[0]);
                }
            }
        } else {
            new Message("Info", Error.NO_PERMISSION).to(cs);
        }
    }

    @Command(command = "me", description = "Share your feelings/thoughts to everyone in the third person.", usage = "/me")
    public void me(CommandSender cs, String[] args) {
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
                new Message("Me", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Me", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "trillium", description = "The main command of the plugin.", usage = "/tr", aliases = "tr")
    public void trillium(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.TRILLIUM)) {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "            Plugin made with love by:");
            cs.sendMessage(ChatColor.GRAY + "       LordSaad, VortexSeven, Turbotailz,");
            cs.sendMessage(ChatColor.GRAY + "                samczsun, and hintss");
            cs.sendMessage(ChatColor.DARK_RED + "                          ❤");
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
            cs.sendMessage(ChatColor.GRAY + "Version: " + TrilliumAPI.getInstance().getDescription().getVersion());
            cs.sendMessage(ChatColor.GRAY + "Configuration Reloaded");
            cs.sendMessage(ChatColor.GRAY + "Support email: support@gettrillium.net");
            cs.sendMessage(ChatColor.GRAY + "Website: http://www.gettrillium.net/");
            cs.sendMessage(ChatColor.GRAY + "Resource page: http://www.spigotmc.org/resources/trillium.3882/");
            Utils.reload();
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "            Plugin made with love by:");
            cs.sendMessage(ChatColor.GRAY + "       LordSaad, VortexSeven, Turbotailz,");
            cs.sendMessage(ChatColor.GRAY + "                samczsun, and hintss");
            cs.sendMessage(ChatColor.DARK_RED + "                          ❤");
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
            cs.sendMessage(ChatColor.GRAY + "Version: " + TrilliumAPI.getInstance().getDescription().getVersion());
            cs.sendMessage(ChatColor.GRAY + "Support email: support@gettrillium.net");
            cs.sendMessage(ChatColor.GRAY + "Website: http://www.gettrillium.net/");
            cs.sendMessage(ChatColor.GRAY + "Resource page: http://www.spigotmc.org/resources/trillium.3882/");
        }
    }

    @Command(command = "message", description = "Send a private message to a player.", usage = "/msg <player> <msg>", aliases = "msg, m")
    public void message(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Chat.MESSAGE)) {
                if (args.length < 2) {
                    new Message("Message", Error.TOO_FEW_ARGUMENTS, "/msg <sender> <message>").to(p);

                } else {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        String msg = sb.toString().trim();

                        Message.message(Type.GENERIC, p.getProxy(), target.getProxy().getName(), false, msg);
                        target.getProxy().sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PluginMessages.)));
                        Message.message(Type.GENERIC, target.getProxy(), p.getProxy().getName(), true, msg);

                    } else {
                        Message.error("MSG", cs, args[0]);
                    }
                }
            } else {
                Message.error("MSG", cs);
            }
        } else {
            Message.error("MSG", cs);
        }
    }

    @Command(command = "nickname", description = "Change your nickname to anything you want.", usage = "/nick <nickname> [player]", aliases = "nick")
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
                                if (!getConfig().getString(Configuration.PlayerSettings.OPCOLOR).equals("") || !getConfig().getString(Configuration.PlayerSettings.OPCOLOR).equals(" ")) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.OPCOLOR) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "You don't have a nickname to remove.");
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            Message.message(Type.GOOD, p.getProxy(), "Nickname", true, "New nickname set: " + args[0]);
                            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + args[0]);

                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }

                } else if (p.hasPermission(Permission.Chat.NICK_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.OPCOLOR).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.OPCOLOR) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "You don't have a nickname set.");
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                            Message.message(Type.GOOD, p.getProxy(), "Nickname", true, "New nickname set: " + nick);
                            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + nick);

                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }

                } else {
                    Message.error("Nickname", cs);
                }

            } else if (args.length > 1) {
                if (p.hasPermission(Permission.Chat.NICK_OTHER) && !p.getProxy().isOp()) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.OPCOLOR).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.OPCOLOR) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "You don't have a nickname set.");
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                target.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + args[0]);
                                Message.message(Type.GOOD, target.getProxy(), "Nickname", true, ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " set your nickname to: " + args[0]);
                                Message.message(Type.GOOD, p.getProxy(), "Nickname", true, "You set " + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " to: " + args[0]);

                            } else {
                                Message.error("Nickname", cs, args[0]);
                            }
                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }

                } else if (p.hasPermission(Permission.Chat.NICK_OTHER_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.OPCOLOR).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.OPCOLOR) + p.getProxy().getName();
                                    p.setDisplayName(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setDisplayName(p.getProxy().getName());
                                }
                            } else {
                                p.setDisplayName(p.getProxy().getName());
                            }
                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "You don't have a nickname set.");
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                                target.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + nick);
                                Message.message(Type.GOOD, target.getProxy(), "Nickname", true, ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " set your nickname to: " + nick);
                                Message.message(Type.GOOD, p.getProxy(), "Nickname", true, "You set " + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " to: " + nick);

                            } else {
                                Message.error("Nickname", cs, args[0]);
                            }
                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Nickname", true, "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }
                } else {
                    Message.error("Nickname", cs);
                }
            } else {
                Message.error(p.getProxy(), "Nickname", true, "/nick <nickname> [player]");
            }
        } else {
            Message.error("Nickname", cs);
        }
    }

    @Command(command = "chatchannel", description = "Talk to a group of people in private.", usage = "/cc <channel> <msg>", aliases = "cc")
    public void chatchannel(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.Chat.CCENABLED)) {
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

                                String f = getConfig().getString(Configuration.Chat.CCFORMAT);

                                f = f.replace("[CHANNELNAME]", args[0]);
                                f = f.replace("[USERNAME]", p.getProxy().getName());
                                f = f.replace("[MESSAGE]", msg);
                                if (getConfig().getBoolean(Configuration.Chat.CCCOLOR)) {
                                    f = ChatColor.translateAlternateColorCodes('&', f);
                                }

                                pl.getProxy().sendMessage(f);
                            }
                        }
                    } else {
                        Message.error("Chat Channel", cs);
                    }
                } else {
                    Message.error(p.getProxy(), "Chat Channel", true, "/cc <channel> <msg>");
                }
            } else {
                Message.error("Chat Channel", cs);
            }
        } else {
            Message.message(Type.WARNING, cs, "Chat Channel", true, "This feature has been disabled.");
        }
    }

    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast <message>", aliases = "bc")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.BROADCAST)) {
            if (args.length == 0) {
                Message.error(cs, "Broadcast", true, "Too few arguments. /broadcast <message>");
            } else {
                String perm = null;
                int argsToStartWith = 0;
                if (args[0].startsWith("-p")) {
                    if (args.length <= 2) {
                        Message.error(cs, "Broadcast", true, "Too few arguments. /broadcast <message>");
                    } else {
                        perm = args[1];
                        argsToStartWith = 2;
                    }
                }

                String defaultcolor = ChatColor.translateAlternateColorCodes(
                        '&', TrilliumAPI.getInstance().getConfig().getString(
                                Configuration.Broadcast.COLOR_TO_USE).trim());

                StringBuilder sb = new StringBuilder();
                for (int i = argsToStartWith; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String message = sb.toString().trim();

                if (getConfig().getBoolean(Configuration.Broadcast.CENTRALIZE)) {

                    List<String> centered = Utils.centerText(message);
                    List<String> format = getConfig().getStringList(Configuration.Broadcast.FORMAT);
                    for (String s : format) {
                        if (s.contains("[msg]")) {
                            for (String slices : centered) {
                                s = s.replace("[msg]", "");
                                s = ChatColor.translateAlternateColorCodes('&', s);
                                if (perm != null) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        if (p.hasPermission(perm)) {
                                            p.sendMessage(defaultcolor + slices);
                                        }
                                    }
                                } else {
                                    Bukkit.broadcastMessage(defaultcolor + slices);
                                }
                            }
                        } else {
                            s = ChatColor.translateAlternateColorCodes('&', s);
                            if (perm != null) {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (p.hasPermission(perm)) {
                                        p.sendMessage(defaultcolor + s);
                                    }
                                }
                            } else {
                                Bukkit.broadcastMessage(s);
                            }
                        }
                    }
                } else {

                    List<String> format = getConfig().getStringList(Configuration.Broadcast.FORMAT);
                    for (String s : format) {
                        s = s.replace("[msg]", ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.Broadcast.COLOR_TO_USE) + message));
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        if (perm != null) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission(perm)) {
                                    p.sendMessage(defaultcolor + s);
                                }
                            }
                        } else {
                            Bukkit.broadcastMessage(s);
                        }
                    }
                }
            }
        } else {
            Message.error("Broadcast", cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission(Permission.Chat.COLOR)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }
}