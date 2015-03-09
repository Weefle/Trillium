package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.M;
import net.gettrillium.trillium.messageutils.T;
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

            M.b(T.R, ChatColor.LIGHT_PURPLE + "Console", message);

        } else {
            M.m(T.W, cs, "Say", true, "Say is for the console. Not you.");
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
            M.e("Motd", cs);
        }
    }

    @Command(command = "information", description = "View information about a certain player.", usage = "/info", aliases = "info")
    public void information(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.INFO)) {
            if (args.length == 0) {
                M.e(cs, "Info", true, "/info <player>");
            } else {
                TrilliumPlayer p = player(args[0]);
                if (p != null) {
                    p.getProxy().sendMessage(" ");
                    M.m(T.R, cs, "Info", true, "Displaying Information On: " + ChatColor.AQUA + p.getProxy().getName());
                    M.m(T.R, cs, "Info", true, "Nickname: " + ChatColor.AQUA + p.getDisplayName());
                    M.m(T.R, cs, "Info", true, "Online: " + ChatColor.AQUA + p.isVanished());
                    M.m(T.R, cs, "Info", true, "Gamemode: " + ChatColor.AQUA + p.getProxy().getGameMode());
                    M.m(T.R, cs, "Info", true, "Banned: " + ChatColor.AQUA + p.getProxy().isBanned());
                    if (p.getProxy().isBanned()) {
                        M.m(T.R, cs, "Info", true, "Ban Reason: 'You are the weakest link. Goodbye.'");
                    }
                    M.m(T.R, cs, "Info", true, "Muted: " + ChatColor.AQUA + p.isMuted());
                    M.m(T.R, cs, "Info", true, "Flying: " + ChatColor.AQUA + p.isFlying());
                    M.m(T.R, cs, "Info", true, "Ping: " + ChatColor.AQUA + Utils.getPing(p.getProxy()));
                    M.m(T.R, cs, "Info", true, "Lag Rate: " + ChatColor.AQUA + Utils.getPingBar(p.getProxy()));
                    M.m(T.R, cs, "Info", true, "Location: " + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ", " + p.getProxy().getLocation().getBlockY() + ", " + p.getProxy().getLocation().getBlockZ());
                    if (p.isVanished()) {
                        M.m(T.R, cs, "Info", true, "Last found at: " + ChatColor.AQUA + p.getLastLocation().getBlockX() + "," + p.getLastLocation().getBlockY() + ", " + p.getLastLocation().getBlockZ());
                    }
                    M.m(T.R, cs, "Info", true, "Food level: " + ChatColor.AQUA + p.getProxy().getFoodLevel());
                    M.m(T.R, cs, "Info", true, "Health level: " + ChatColor.AQUA + p.getProxy().getHealthScale());
                    M.m(T.R, cs, "Info", true, "Time Played: hours: " + ChatColor.AQUA + (p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60);
                    M.m(T.R, cs, "Info", true, "Time Played: days: " + ChatColor.AQUA + ((p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60) / 24);
                } else {
                    M.e("Info", cs, args[0]);
                }
            }
        } else {
            M.e("Info", cs);
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
                M.e("Me", cs);
            }
        } else {
            M.e("Me", cs);
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
            cs.sendMessage(ChatColor.GRAY + "Vesion: " + TrilliumAPI.getInstance().getDescription().getVersion());
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
            cs.sendMessage(ChatColor.GRAY + "Vesion: " + TrilliumAPI.getInstance().getDescription().getVersion());
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
                    M.e(p.getProxy(), "MSG", true, "/msg <sender> <message>");

                } else {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        String msg = sb.toString().trim();

                        M.m(T.R, p.getProxy(), target.getProxy().getName(), false, msg);
                        M.m(T.R, target.getProxy(), p.getProxy().getName(), true, msg);

                    } else {
                        M.e("MSG", cs, args[0]);
                    }
                }
            } else {
                M.e("MSG", cs);
            }
        } else {
            M.e("MSG", cs);
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
                            M.m(T.W, p.getProxy(), "Nickname", true, "You don't have a nickname to remove.");
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            M.m(T.G, p.getProxy(), "Nickname", true, "New nickname set: " + args[0]);
                            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + args[0]);

                        } else {
                            M.m(T.W, p.getProxy(), "Nickname", true, "Too many characters. "
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
                            M.m(T.W, p.getProxy(), "Nickname", true, "You don't have a nickname set.");
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                            M.m(T.G, p.getProxy(), "Nickname", true, "New nickname set: " + nick);
                            p.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + nick);

                        } else {
                            M.m(T.W, p.getProxy(), "Nickname", true, "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }

                } else {
                    M.e("Nickname", cs);
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
                            M.m(T.W, p.getProxy(), "Nickname", true, "You don't have a nickname set.");
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                target.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + args[0]);
                                M.m(T.G, target.getProxy(), "Nickname", true, ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " set your nickname to: " + args[0]);
                                M.m(T.G, p.getProxy(), "Nickname", true, "You set " + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " to: " + args[0]);

                            } else {
                                M.e("Nickname", cs, args[0]);
                            }
                        } else {
                            M.m(T.W, p.getProxy(), "Nickname", true, "Too many characters. "
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
                            M.m(T.W, p.getProxy(), "Nickname", true, "You don't have a nickname set.");
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                                target.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + nick);
                                M.m(T.G, target.getProxy(), "Nickname", true, ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " set your nickname to: " + nick);
                                M.m(T.G, p.getProxy(), "Nickname", true, "You set " + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " to: " + nick);

                            } else {
                                M.e("Nickname", cs, args[0]);
                            }
                        } else {
                            M.m(T.W, p.getProxy(), "Nickname", true, "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }
                } else {
                    M.e("Nickname", cs);
                }
            } else {
                M.e(p.getProxy(), "Nickname", true, "/nick <nickname> [player]");
            }
        } else {
            M.e("Nickname", cs);
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
                        M.e("Chat Channel", cs);
                    }
                } else {
                    M.e(p.getProxy(), "Chat Channel", true, "/cc <channel> <msg>");
                }
            } else {
                M.e("Chat Channel", cs);
            }
        } else {
            M.m(T.W, cs, "Chat Channel", true, "This feature has been disabled.");
        }
    }

    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast <message>", aliases = "bc")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.BROADCAST)) {
            if (args.length == 0) {
                M.e(cs, "Broadcast", true, "Too few arguments. /broadcast <message>");
            } else {
                String perm = null;
                int argsToStartWith = 0;
                if (args[0].startsWith("-p")) {
                    if (args.length <= 2) {
                        M.e(cs, "Broadcast", true, "Too few arguments. /broadcast <message>");
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
            M.e("Broadcast", cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission(Permission.Chat.COLOR)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }
}