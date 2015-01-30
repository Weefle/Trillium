package me.lordsaad.trillium;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by saad on 30-Jan-15.
 */
public class PlayerDatabase {


    public static File db(Player p) throws IOException {
        File file = new File(Main.plugin.getDataFolder() + "/PlayerDatabase/" + p.getUniqueId() + ".yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("Nickname", p.getName());
            db.set("Previous Location.wolrd", p.getLocation().getWorld().getName());
            db.set("Previous Location.x", p.getLocation().getX());
            db.set("Previous Location.y", p.getLocation().getY());
            db.set("Previous Location.z", p.getLocation().getZ());
            db.set("Previous Location.pitch", p.getLocation().getPitch());
            db.set("Previous Location.yaw", p.getLocation().getYaw());
            db.set("Muted", false);
            db.set("God Mode", false);

            db.save(file);
        }
        return file;
    }
}
