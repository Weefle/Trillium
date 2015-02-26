package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
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

            Message.b(MType.R, ChatColor.LIGHT_PURPLE + "Console", message);

        } else {
            Message.m(MType.W, cs, "Say", "Say is for the console. Not you.");
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
            Message.e(cs, "Motd", Crit.P);
        }
    }

    @Command(command = "information", description = "View information about a certain player.", usage = "/info", aliases = "info")
    public void information(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.INFO)) {
            if (args.length == 0) {
                Message.earg(cs, "Info", "/info <player>");
            } else {
                TrilliumPlayer p = player(args[0]);
                if (p != null) {
                    p.getProxy().sendMessage(" ");
                    Message.m(MType.R, cs, "Info", "Displaying Information On: " + ChatColor.AQUA + p.getProxy().getName());
                    Message.m(MType.R, cs, "Info", "Nickname: " + ChatColor.AQUA + p.getNickname());
                    Message.m(MType.R, cs, "Info", "Online: " + ChatColor.AQUA + p.isVanished());
                    Message.m(MType.R, cs, "Info", "Gamemode: " + ChatColor.AQUA + p.getProxy().getGameMode());
                    Message.m(MType.R, cs, "Info", "Banned: " + ChatColor.AQUA + p.getProxy().isBanned());
                    if (p.getProxy().isBanned()) {
                        Message.m(MType.R, cs, "Info", "Ban Reason: 'You are the weakest link. Goodbye.'");
                    }
                    Message.m(MType.R, cs, "Info", "Muted: " + ChatColor.AQUA + p.isMuted());
                    Message.m(MType.R, cs, "Info", "Flying: " + ChatColor.AQUA + p.isFlying());
                    Message.m(MType.R, cs, "Info", "Ping: " + ChatColor.AQUA + Utils.getPing(p.getProxy()));
                    Message.m(MType.R, cs, "Info", "Lag Rate: " + ChatColor.AQUA + Utils.getPingBar(p.getProxy()));
                    Message.m(MType.R, cs, "Info", "Location: " + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ", " + p.getProxy().getLocation().getBlockY() + ", " + p.getProxy().getLocation().getBlockZ());
                    if (p.isVanished()) {
                        Message.m(MType.R, cs, "Info", "Last found at: " + ChatColor.AQUA + p.getLastLocation().getBlockX() + "," + p.getLastLocation().getBlockY() + ", " + p.getLastLocation().getBlockZ());
                    }
                    Message.m(MType.R, cs, "Info", "Food level: " + ChatColor.AQUA + p.getProxy().getFoodLevel());
                    Message.m(MType.R, cs, "Info", "Health level: " + ChatColor.AQUA + p.getProxy().getHealthScale());
                    Message.m(MType.R, cs, "Info", "Time Played: hours: " + ChatColor.AQUA + (p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60);
                    Message.m(MType.R, cs, "Info", "Time Played: days: " + ChatColor.AQUA + ((p.getProxy().getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60) / 24);
                } else {
                    Message.eplayer(cs, "Info", args[0]);
                }
            }
        } else {
            Message.e(cs, "Info", Crit.P);
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
                Message.e(p.getProxy(), "Me", Crit.P);
            }
        } else {
            Message.e(cs, "Me", Crit.C);
        }
    }

    @Command(command = "trillium", description = "The main command of the plugin.", usage = "/tr", aliases = "tr")
    public void trillium(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.TRILLIUM)) {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "            Plugin made with love by:");
            cs.sendMessage(ChatColor.GRAY + "         LordSaad, VortexSeven, Turbotailz,");
            cs.sendMessage(ChatColor.GRAY + "                 samczsun, and hintss");
            cs.sendMessage(ChatColor.DARK_RED + "                   ❤");
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
            cs.sendMessage(ChatColor.GRAY + "Vesion: " + TrilliumAPI.getInstance().getDescription().getVersion());
            cs.sendMessage(ChatColor.GRAY + "Configuration Reloaded");
            cs.sendMessage(ChatColor.GRAY + "Support email: support@gettrillium.net");
            cs.sendMessage(ChatColor.GRAY + "Website: http://www.gettrillium.net/");
            cs.sendMessage(ChatColor.GRAY + "Resource page: http://www.spigotmc.org/resources/trillium.3882/");
            TrilliumAPI.getInstance().reloadConfig();
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "            Plugin made with love by:");
            cs.sendMessage(ChatColor.GRAY + "         LordSaad, VortexSeven, Turbotailz,");
            cs.sendMessage(ChatColor.GRAY + "                 samczsun, and hintss");
            cs.sendMessage(ChatColor.DARK_RED + "                   ❤");
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
                    Message.earg(p.getProxy(), "MSG", "/msg <sender> <message>");

                } else {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        String msg = sb.toString().trim();

                        Message.minvert(MType.R, p.getProxy(), target.getProxy().getName(), msg);
                        Message.m(MType.R, target.getProxy(), p.getProxy().getName(), msg);

                    } else {
                        Message.eplayer(p.getProxy(), "MSG", args[0]);
                    }
                }
            } else {
                Message.e(p.getProxy(), "MSG", Crit.P);
            }
        } else {
            Message.e(cs, "MSG", Crit.C);
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
                                    p.setNickname(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setNickname(p.getProxy().getName());
                                }
                            } else {
                                p.setNickname(p.getProxy().getName());
                            }
                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname to remove.");
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            Message.m(MType.G, p.getProxy(), "Nickname", "New nickname set: " + args[0]);
                            p.setNickname(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + args[0]);

                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "Too many characters. "
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
                                    p.setNickname(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setNickname(p.getProxy().getName());
                                }
                            } else {
                                p.setNickname(p.getProxy().getName());
                            }
                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    } else {

                        if (ChatColor.stripColor(args[0]).length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                            Message.m(MType.G, p.getProxy(), "Nickname", "New nickname set: " + nick);
                            p.setNickname(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + nick);

                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }

                } else {
                    Message.e(p.getProxy(), "Nickname", Crit.P);
                }

            } else if (args.length > 1) {
                if (p.hasPermission(Permission.Chat.NICK_OTHER) && !p.getProxy().isOp()) {

                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
                        if (p.hasNickname()) {
                            if (p.getProxy().isOp()) {
                                if (!getConfig().getString(Configuration.PlayerSettings.OPCOLOR).isEmpty()) {
                                    String opcolor = "&" + getConfig().getString(Configuration.PlayerSettings.OPCOLOR) + p.getProxy().getName();
                                    p.setNickname(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setNickname(p.getProxy().getName());
                                }
                            } else {
                                p.setNickname(p.getProxy().getName());
                            }
                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                target.setNickname(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + args[0]);
                                Message.m(MType.G, target.getProxy(), "Nickname", ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " set your nickname to: " + args[0]);
                                Message.m(MType.G, p.getProxy(), "Nickname", "You set " + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " to: " + args[0]);

                            } else {
                                Message.eplayer(p.getProxy(), "Nickname", args[0]);
                            }
                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "Too many characters. "
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
                                    p.setNickname(ChatColor.translateAlternateColorCodes('&', opcolor));
                                } else {
                                    p.setNickname(p.getProxy().getName());
                                }
                            } else {
                                p.setNickname(p.getProxy().getName());
                            }
                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    } else {

                        if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                            TrilliumPlayer target = player(args[1]);
                            if (target != null) {
                                String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                                target.setNickname(ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.PREF)) + nick);
                                Message.m(MType.G, target.getProxy(), "Nickname", ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " set your nickname to: " + nick);
                                Message.m(MType.G, p.getProxy(), "Nickname", "You set " + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + " to: " + nick);

                            } else {
                                Message.eplayer(p.getProxy(), "Nickname", args[0]);
                            }
                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "Too many characters. "
                                    + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                    + " is the limit.");
                        }
                    }
                } else {
                    Message.e(p.getProxy(), "Nickname", Crit.P);
                }
            } else {
                Message.earg(p.getProxy(), "Nickname", "/nick <nickname> [player]");
            }
        } else {
            Message.e(cs, "Nickname", Crit.C);
        }
    }

    @Command(command = "chatchannel", description = "Talk to a group of people in private.", usage = "/cc <channel> <msg>", aliases = "cc")
    public void chatchannel(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.Server.CCENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);
                if (args.length >= 2) {
                    if (p.hasPermission(Permission.Chat.CHATCHANNEL + args[0])) {

                        for (TrilliumPlayer pl : TrilliumAPI.getOnlinePlayers()) {
                            if (pl.hasPermission(Permission.Chat.CHATCHANNEL + args[0])) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i <= args.length; i++) {
                                    sb.append(i).append(" ");
                                }

                                String msg = sb.toString().trim();
                                String f = getConfig().getString(Configuration.Server.CCFORMAT);

                                f = f.replace("[CHANNELNAME]", args[0]);
                                f = f.replace("[USERNAME]", p.getProxy().getName());
                                f = f.replace("[MESSAGE]", msg);
                                if (getConfig().getBoolean(Configuration.Server.CCCOLOR)) {
                                    f = ChatColor.translateAlternateColorCodes('&', msg);
                                }

                                pl.getProxy().sendMessage(f);
                            }
                        }
                    } else {
                        Message.e(p.getProxy(), "Chat Channel", Crit.P);
                    }
                } else {
                    Message.earg(p.getProxy(), "Chat Channel", "/cc <channel> <msg>");
                }
            } else {
                Message.e(cs, "Chat Channel", Crit.C);
            }
        } else {
            Message.m(MType.W, cs, "Chat Channel", "This feature has been disabled.");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission(Permission.Chat.COLOR)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }
}