package me.lordsaad.trillium.messageutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    public static void b(MType MType, String tag, String msg) {
        Bukkit.broadcastMessage(MType.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + msg);
    }

    public static void m(MType MType, CommandSender cs, String tag, String msg) {
        cs.sendMessage(MType.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + msg);
    }

    public static void minvert(MType MType, CommandSender cs, String tag, String msg) {
        cs.sendMessage(MType.getPrefix() + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " << " + ChatColor.GRAY + msg);
    }

    public static void e(CommandSender cs, String tag, Crit criteria) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + criteria);
    }

    public static void eplayer(CommandSender cs, String tag, String p) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + p + " is either not online or does not exist.");
    }

    public static void earg(CommandSender cs, String tag, String forextra) {
        cs.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + tag + ChatColor.DARK_GRAY + "]" + ChatColor.BLUE + " >> " + ChatColor.GRAY + "Too few arguments. " + forextra);
    }
}