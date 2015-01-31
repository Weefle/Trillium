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
                    double x = yml.getDouble("Previous Location.x");
                    double y = yml.getDouble("Previous Location.y");
                    double z = yml.getDouble("Previous Location.z");
                    float pitch = (float) yml.getDouble("Previous Location.pitch");
                    float yaw = (float) yml.getDouble("Previous Location.yaw");
                    Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

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