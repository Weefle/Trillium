package me.lordsaad.trillium.teleport;

import me.lordsaad.trillium.PlayerDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandTeleportH implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("tr.teleporthere")) {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            String world = target.getLocation().getWorld().getName();
                            double x = target.getLocation().getX();
                            double y = target.getLocation().getY();
                            double z = target.getLocation().getZ();
                            float yaw = target.getLocation().getYaw();
                            float pitch = target.getLocation().getPitch();

                            YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(target));

                            yml.set("Previous Location.world", world);
                            yml.set("Previous Location.x", x);
                            yml.set("Previous Location.y", y);
                            yml.set("Previous Location.z", z);
                            yml.set("Previous Location.pitch", pitch);
                            yml.set("Previous Location.yaw", yaw);

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            target.teleport(p);
                            p.sendMessage(ChatColor.GREEN + "You teleported " + target.getName() + " to you.");
                            target.sendMessage(ChatColor.BLUE + p.getName() + " teleported you to them.");

                        } else {
                            p.sendMessage(ChatColor.RED + args[0] + " is either offline or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too few arguments. /tphere <player>");
                    }
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