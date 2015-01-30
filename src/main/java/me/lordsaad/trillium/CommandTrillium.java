package me.lordsaad.trillium;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by saad on 30-Jan-15.
 */
public class CommandTrillium implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("trillium")) {
            if (sender.hasPermission("tr.admin")) {
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<-{[|O|]}->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<-{[|O|]}->>>");
                sender.sendMessage(ChatColor.GRAY + "Plugin made with love");
                sender.sendMessage(ChatColor.GRAY + "   by LordSaad44");
                sender.sendMessage(ChatColor.DARK_RED + "        ❤");
                sender.sendMessage(ChatColor.GRAY + "Vesion: " + Main.plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "Configuration Reloaded");
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<--------------------------------------->>>");
                Main.plugin.reloadConfig();
            } else {
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<-{[|O|]}->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<-{[|O|]}->>>");
                sender.sendMessage(ChatColor.GRAY + "Plugin made with love");
                sender.sendMessage(ChatColor.GRAY + "   by LordSaad44");
                sender.sendMessage(ChatColor.DARK_RED + "        ❤");
                sender.sendMessage(ChatColor.GRAY + "Vesion: " + Main.plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.DARK_GRAY + "<<<--------------------------------------->>>");
            }
        }
        return true;
    }
}