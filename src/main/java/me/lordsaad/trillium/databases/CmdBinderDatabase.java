package me.lordsaad.trillium.databases;

import me.lordsaad.trillium.api.TrilliumAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CmdBinderDatabase {

    public static File cbd() {
        File file = new File(TrilliumAPI.getInstance().getDataFolder() + "CommandBinderDatabase.yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("walkconsole", "");
            db.set("walkplayer", "");
            db.set("touchconsole", "");
            db.set("touchplayer", "");
            try {
                db.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
