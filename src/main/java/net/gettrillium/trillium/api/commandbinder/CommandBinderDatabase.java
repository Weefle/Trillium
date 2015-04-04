package net.gettrillium.trillium.api.commandbinder;

import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandBinderDatabase {

    public static File cbd() {
        File file = new File(TrilliumAPI.getInstance().getDataFolder(), "CommandBinderDatabase.yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            List<String> filler = new ArrayList<>();
            db.set("walk-console", filler);
            db.set("walk-player", filler);
            db.set("touch-console", filler);
            db.set("touch-player", filler);
            try {
                db.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
