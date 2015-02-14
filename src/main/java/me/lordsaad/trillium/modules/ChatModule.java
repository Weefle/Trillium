package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast <message>")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.BROADCAST)) {
            if (args.length == 0) {
                Message.earg(cs, "Broadcast", "Too few arguments. /broadcast <message>");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString().trim();

                List<String> format = getConfig().getStringList("broadcast");

                for (String s : format) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    s = s.replace("[msg]", message);
                    Bukkit.broadcastMessage(s);
                }
            }
        } else {
            Message.e(cs, "Broadcast", Crit.P);
        }
    }

    @Command(command = "motd", description = "View the server's motd", usage = "/motd")
    public void motd(CommandSender cs) {
        if (cs.hasPermission(Permission.Chat.MOTD)) {
            ArrayList<String> motd = (ArrayList<String>) TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Server.INGAME_MOTD);
            for (String s : motd) {
                s = ChatColor.translateAlternateColorCodes('&', s);
                s = ChatColor.translateAlternateColorCodes('&', s);
                s = s.replace("[USERNAME]", cs.getName());
                s = s.replace("[SLOTS]", "" + Bukkit.getMaxPlayers());
                s = s.replace("[ONLINE]", "" + Bukkit.getOnlinePlayers().size());
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
    public void trillium(CommandSender cs) {
        if (cs.hasPermission(Permission.Admin.TRILLIUM)) {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "              Plugin made with love");
            cs.sendMessage(ChatColor.GRAY + "       by LordSaad, VortexSeven, Turbotailz");
            cs.sendMessage(ChatColor.GRAY + "               and Samczsun");
            cs.sendMessage(ChatColor.DARK_RED + "                     ❤");
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
            cs.sendMessage(ChatColor.GRAY + "Vesion: " + TrilliumAPI.getInstance().getDescription().getVersion());
            cs.sendMessage(ChatColor.GRAY + "Configuration Reloaded");
            cs.sendMessage(ChatColor.GRAY + "Support email: support@gettrillium.net");
            cs.sendMessage(ChatColor.GRAY + "Website: http://www.gettrillium.net/");
            cs.sendMessage(ChatColor.GRAY + "Resource page: http://www.spigotmc.org/resources/trillium.3882/");
            TrilliumAPI.getInstance().reloadConfig();
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "              Plugin made with love");
            cs.sendMessage(ChatColor.GRAY + "       by LordSaad, VortexSeven, Turbotailz");
            cs.sendMessage(ChatColor.GRAY + "               and Samczsun");
            cs.sendMessage(ChatColor.DARK_RED + "                     ❤");
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
                if (p.hasPermission(Permission.Chat.NICK) && !p.getProxy().isOp()) {

                    if (args[0].equalsIgnoreCase("remove")) {
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
                            return;

                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    }

                    if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                        Message.m(MType.G, p.getProxy(), "Nickname", "New nickname set: " + args[0]);
                        p.setNickname(getConfig().getInt(Configuration.PlayerSettings.PREF) + args[0]);

                    } else {
                        Message.m(MType.W, p.getProxy(), "Nickname", "Too many characters. "
                                + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                + " is the limit.");
                    }

                } else if (p.hasPermission(Permission.Chat.NICK_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove")) {
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
                            return;

                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    }


                    if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                        String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                        Message.m(MType.G, p.getProxy(), "Nickname", "New nickname set: " + nick);
                        p.getProxy().setDisplayName(getConfig().getInt(Configuration.PlayerSettings.PREF) + nick);

                    } else {
                        Message.m(MType.W, p.getProxy(), "Nickname", "Too many characters. "
                                + getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)
                                + " is the limit.");
                    }
                    
                } else {
                    Message.e(p.getProxy(), "Nickname", Crit.P);
                }

            } else if (args.length > 1) {
                if (p.hasPermission(Permission.Chat.NICK_OTHER) && !p.getProxy().isOp()) {

                    if (args[0].equalsIgnoreCase("remove")) {
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
                            return;

                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    }


                    if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                        TrilliumPlayer target = player(args[1]);
                        if (target != null) {
                            target.getProxy().setDisplayName(getConfig().getInt(Configuration.PlayerSettings.PREF) + args[0]);
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

                } else if (p.hasPermission(Permission.Chat.NICK_OTHER_COLOR)) {

                    if (args[0].equalsIgnoreCase("remove")) {
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
                            return;

                        } else {
                            Message.m(MType.W, p.getProxy(), "Nickname", "You don't have a nickname set.");
                        }
                    }


                    if (args[0].length() <= getConfig().getInt(Configuration.PlayerSettings.CHARLIMIT)) {

                        TrilliumPlayer target = player(args[1]);
                        if (target != null) {
                            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
                            target.getProxy().setDisplayName(getConfig().getInt(Configuration.PlayerSettings.PREF) + nick);
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
}