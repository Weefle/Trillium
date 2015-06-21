package net.gettrillium.trillium.api.report;

import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReportDatabase {

    public static File report() {
        File file = new File(TrilliumAPI.getInstance().getDataFolder(), "report database.yml");
        if (!file.exists()) {
            YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
            db.set("rows", "");
            try {
                db.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
