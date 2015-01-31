package me.lordsaad.trillium;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerDatabase {


    public static File db(Player p) throws IOException {
        File file = new File(Main.plugin.getDataFolder() + "/PlayerDatabase/" + p.getUniqueId() + ".yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("Nickname", p.getName());
            db.set("Previous Location.world", p.getLocation().getWorld().getName());
            db.set("Previous Location.x", p.getLocation().getX());
            db.set("Previous Location.y", p.getLocation().getY());
            db.set("Previous Location.z", p.getLocation().getZ());
            db.set("Previous Location.pitch", p.getLocation().getPitch());
            db.set("Previous Location.yaw", p.getLocation().getYaw());
            db.set("Muted", false);
            db.set("God Mode", false);
            db.set("Vanish Mode", false);
            db.set("Inventory.items", null);
            db.set("Inventory.armor", null);

            db.save(file);
        }
        return file;
    }
}
