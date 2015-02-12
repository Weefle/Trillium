package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.api.TrilliumAPI;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTrillium implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("trillium")) {
            if (args.length == 0) {
                if (sender.hasPermission("tr.admin")) {
                    sender.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
                    sender.sendMessage(ChatColor.GRAY + "              Plugin made with love");
                    sender.sendMessage(ChatColor.GRAY + "       by LordSaad, VortexSeven, Turbotailz");
                    sender.sendMessage(ChatColor.GRAY + "               and Samczsun");
                    sender.sendMessage(ChatColor.DARK_RED + "                     ❤");
                    sender.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
                    sender.sendMessage(ChatColor.GRAY + "Vesion: " + TrilliumAPI.getInstance().getDescription().getVersion());
                    sender.sendMessage(ChatColor.GRAY + "Configuration Reloaded");
                    sender.sendMessage(ChatColor.GRAY + "Please report any bugs you find to: support@gettrillium.net");
                    TrilliumAPI.getInstance().reloadConfig();
                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
                    sender.sendMessage(ChatColor.GRAY + "              Plugin made with love");
                    sender.sendMessage(ChatColor.GRAY + "       by LordSaad, VortexSeven, Turbotailz");
                    sender.sendMessage(ChatColor.GRAY + "               and Samczsun");
                    sender.sendMessage(ChatColor.DARK_RED + "                     ❤");
                    sender.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
                    sender.sendMessage(ChatColor.GRAY + "Vesion: " + TrilliumAPI.getInstance().getDescription().getVersion());
                    sender.sendMessage(ChatColor.GRAY + "Please report any bugs you find to: support@gettrillium.net");
                }
            }
        }
        return true;
    }
}