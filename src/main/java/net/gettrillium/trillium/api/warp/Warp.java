package net.gettrillium.trillium.api.warp;

import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warp {

    private static HashMap<String, Location> warps = new HashMap<>();

    public static void setWarp(String name, Location loc) {
        warps.put(name, loc);
        save();
    }

    public static void delWarp(String name) {
        warps.remove(name);
        save();
    }

    public static Location getLocation(String name) {
        return warps.get(name);
    }

    public static boolean isNotNull(String name) {
        return warps.get(name) != null;
    }

    public static ArrayList<Message> getWarpList() {
        ArrayList<Message> format = new ArrayList<>();

        for (Map.Entry<String, Location> row : warps.entrySet()) {
            format.add(new Message(Mood.NEUTRAL, row.getKey(), Utils.locationToString(row.getValue())));
        }

        return format;
    }

    private static void save() {
        ArrayList<String> serialized = new ArrayList<>();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());

        for (Map.Entry<String, Location> row : warps.entrySet()) {
            serialized.add(row.getKey() + ";" + Utils.locationToString(row.getValue()));
        }

        yml.set("rows", serialized);

        try {
            yml.save(WarpDatabase.wd());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setWarps() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> serialized = yml.getStringList("rows");
        for (String deserialize : serialized) {
            warps.put(deserialize.split(";")[0], Utils.locationFromString(deserialize.split(";")[1]));
        }
    }
}