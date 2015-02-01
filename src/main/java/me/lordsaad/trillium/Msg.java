package me.lordsaad.trillium;

import me.lordsaad.trillium.messageutils.Crit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Msg {

    public static void b(Type type, String tag, String msg) {
        Bukkit.broadcastMessage(type.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + msg);
    }

    public static void m(Type type, CommandSender cs, String tag, String msg) {
        cs.sendMessage(type.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + msg);
    }

    public static void e(Type type, CommandSender cs, String tag, Crit criteria) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + criteria);
    }

    public static void e(Type type, CommandSender cs, String tag, String preextra, Crit criteria) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + preextra + criteria);
    }

    public static void e(Type type, CommandSender cs, String tag, Crit criteria, String forextra) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + criteria + forextra);
    }
}