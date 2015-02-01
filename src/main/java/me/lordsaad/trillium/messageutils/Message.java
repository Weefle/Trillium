package me.lordsaad.trillium.messageutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    public static void b(MType MType, String tag, String msg) {
        Bukkit.broadcastMessage(MType.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + msg);
    }

    public static void m(MType MType, CommandSender cs, String tag, String msg) {
        cs.sendMessage(MType.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + msg);
    }

    public static void e(CommandSender cs, String tag, Crit criteria) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + criteria);
    }

    public static void e(CommandSender cs, String tag, String preextra, Crit criteria) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + preextra + criteria);
    }

    public static void e(CommandSender cs, String tag, Crit criteria, String forextra) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " » " + ChatColor.GRAY + criteria + forextra);
    }
}