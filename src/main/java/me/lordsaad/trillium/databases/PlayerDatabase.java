package me.lordsaad.trillium.databases;

import me.lordsaad.trillium.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerDatabase {

    public static File db(OfflinePlayer p) {
        File file = new File(Main.plugin.getDataFolder() + "/PlayerDatabase/" + p.getUniqueId() + ".yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("Nickname", p.getName());
            db.set("Previous_Location.world", "");
            db.set("Previous_Location.x", 0);
            db.set("Previous_Location.y", 0);
            db.set("Previous_Location.z", 0);
            db.set("Muted", false);
            db.set("God_Mode", false);
            db.set("Vanish_Mode", false);
            db.set("Ban_Reason", "");
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
