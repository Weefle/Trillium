package me.lordsaad.trillium.commands.teleport;

import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
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

                    Message.m(MType.G, p, "Back", "You have been sent back to your last location.");
                    p.teleport(loc);

                } else {
                    Message.e(p, "Back", Crit.P);
                }
            } else {
                Message.e(sender, "Back", Crit.C);
            }
        }
        return true;
    }
}