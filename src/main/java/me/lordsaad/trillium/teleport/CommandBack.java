package me.lordsaad.trillium.teleport;

import me.lordsaad.trillium.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBack implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("back")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.back")) {

                    String world = Main.plugin.getConfig().getString("Previous Location.world");
                    double x = Main.plugin.getConfig().getDouble("Previous Location.x");
                    double y = Main.plugin.getConfig().getDouble("Previous Location.y");
                    double z = Main.plugin.getConfig().getDouble("Previous Location.z");
                    float pitch = (float) Main.plugin.getConfig().getDouble("Previous Location.pitch");
                    float yaw = (float) Main.plugin.getConfig().getDouble("Previous Location.yaw");
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