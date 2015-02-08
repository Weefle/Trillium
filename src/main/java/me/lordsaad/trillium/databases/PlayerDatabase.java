package me.lordsaad.trillium.databases;

import me.lordsaad.trillium.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerDatabase {

    public static File db(Player p) {
        File file = new File(Main.plugin.getDataFolder() + "/PlayerDatabase/" + p.getUniqueId() + ".yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("Nickname", p.getName());
            db.set("Previous Location.world", p.getLocation().getWorld().getName());
            db.set("Previous Location.x", p.getLocation().getBlockX());
            db.set("Previous Location.y", p.getLocation().getBlockY());
            db.set("Previous Location.z", p.getLocation().getBlockZ());
            db.set("Muted", false);
            db.set("God Mode", false);
            db.set("Vanish Mode", false);
            db.set("Inventory.items", null);
            db.set("Inventory.armor", null);

            try {
                db.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}