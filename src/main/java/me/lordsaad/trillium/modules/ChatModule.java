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
    public void broadcast(CommandSender cs) {
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
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null) {
                    TrilliumPlayer player = TrilliumAPI.getPlayer(p.getName());
                    p.sendMessage(" ");
                    Message.m(MType.R, cs, "Info", "Displaying Information on: " + ChatColor.AQUA + p.getName());
                    Message.m(MType.R, cs, "Info", "Nickname: " + ChatColor.AQUA + p.getDisplayName());
                    Message.m(MType.R, cs, "Info", "Online: " + ChatColor.AQUA + player.isVanished());
                    Message.m(MType.R, cs, "Info", "Gamemode: " + ChatColor.AQUA + p.getGameMode());
                    Message.m(MType.R, cs, "Info", "Banned: " + ChatColor.AQUA + p.isBanned());
                    if (p.isBanned()) {
                        Message.m(MType.R, cs, "Info", "Ban Reason: 'You are the weakest link. Goodbye.'");
                    }
                    Message.m(MType.R, cs, "Info", "Muted: " + ChatColor.AQUA + player.isMuted());
                    Message.m(MType.R, cs, "Info", "Flying: " + ChatColor.AQUA + p.isFlying());
                    Message.m(MType.R, cs, "Info", "Ping: " + ChatColor.AQUA + Utils.getPing(p));
                    Message.m(MType.R, cs, "Info", "Ping: " + ChatColor.AQUA + Utils.getPingBar(p));
                    Message.m(MType.R, cs, "Info", "Location: " + ChatColor.AQUA + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ());
                    if (player.isVanished()) {
                        Message.m(MType.R, cs, "Info", "Last found at: " + ChatColor.AQUA + "[COMING SOON]");
                    }
                    Message.m(MType.R, cs, "Info", "Food level: " + ChatColor.AQUA + p.getFoodLevel());
                    Message.m(MType.R, cs, "Info", "Health level: " + ChatColor.AQUA + p.getHealthScale());
                    Message.m(MType.R, cs, "Info", "Time Played: hours: " + ChatColor.AQUA + (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60);
                    Message.m(MType.R, cs, "Info", "Time Played: days: " + ChatColor.AQUA + ((p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60) / 60) / 24);
                } else {
                    Message.eplayer(cs, "Info", args[0]);
                }
            }
        } else {
            Message.e(cs, "Info", Crit.P);
        }
    }


}
