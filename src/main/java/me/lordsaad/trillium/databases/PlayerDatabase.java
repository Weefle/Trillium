package me.lordsaad.trillium.databases;

import java.io.File;
import java.io.IOException;

import me.lordsaad.trillium.api.TrilliumAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerDatabase {

    public static File db(OfflinePlayer p) {
        File file = new File(TrilliumAPI.getInstance().getDataFolder() + "/PlayerDatabase/" + p.getUniqueId() + ".yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("Nickname", p.getName());
            db.set("Previous Location.world", "");
            db.set("Previous Location.x", 0);
            db.set("Previous Location.y", 0);
            db.set("Previous Location.z", 0);
            db.set("Muted", false);
            db.set("God Mode", false);
            db.set("Vanish Mode", false);
            db.set("Ban Reason", "");
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
