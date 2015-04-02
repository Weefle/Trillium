package net.gettrillium.trillium.api.commandbinder;

import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CommandBinderDatabase {

    public static File cbd() {
        File file = new File(TrilliumAPI.getInstance().getDataFolder(), "CommandBinderDatabase.yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("walk-console", null);
            db.set("walk-player", null);
            db.set("touch-console", null);
            db.set("touch-player", null);
            try {
                db.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
