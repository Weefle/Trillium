package me.lordsaad.trillium.teleport;

import me.lordsaad.trillium.PlayerDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandBack implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("back")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.back")) {

                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));

                    String world = yml.getString("Previous Location.world");
                    int x = yml.getInt("Previous Location.x");
                    int y = yml.getInt("Previous Location.y");
                    int z = yml.getInt("Previous Location.z");

                    Location loc = new Location(Bukkit.getWorld(world), x, y, z);

                    p.sendMessage(ChatColor.BLUE + "You have been sent back to your last location.");
                    p.teleport(loc);

                } else {
                    p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You can't do that.");
            }
        }
        return true;
    }
}