package net.gettrillium.trillium.api.warp;

import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WarpDatabase {

    public static File wd() {
        File file = new File(TrilliumAPI.getInstance().getDataFolder(), "WarpDrive.yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("warps", "");
            try {
                db.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
