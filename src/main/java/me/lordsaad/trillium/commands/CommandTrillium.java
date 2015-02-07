package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTrillium implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("trillium")) {
            if (sender.hasPermission("tr.admin")) {
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
                sender.sendMessage(ChatColor.GRAY + "              Plugin made with love");
                sender.sendMessage(ChatColor.GRAY + "       by LordSaad, VortexSeven, and TurboTailz");
                sender.sendMessage(ChatColor.DARK_RED + "                          ❤");
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
                sender.sendMessage(ChatColor.GRAY + "Vesion: " + Main.plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "Configuration Reloaded");
                Main.plugin.reloadConfig();
            } else {
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
                sender.sendMessage(ChatColor.GRAY + "              Plugin made with love");
                sender.sendMessage(ChatColor.GRAY + "       by LordSaad44 and VortexSeven");
                sender.sendMessage(ChatColor.DARK_RED + "                          ❤");
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
                sender.sendMessage(ChatColor.GRAY + "Vesion: " + Main.plugin.getDescription().getVersion());

            }
        }
        return true;
    }
}