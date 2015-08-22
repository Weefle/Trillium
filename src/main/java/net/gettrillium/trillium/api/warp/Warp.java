package net.gettrillium.trillium.api.warp;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Warp {

    private static Map<String, Location> warps = new HashMap<>();
    private static final List<Message> cachedWarpList = new ArrayList<>();

    private Warp() {
    }

    public static void setWarp(String name, Location loc) {
        warps.put(name, loc);
        save();

        cachedWarpList.clear();
    }

    public static void delWarp(String name) {
        warps.remove(name);
        save();

        cachedWarpList.clear();
    }

    public static Location getLocation(String name) {
        return warps.get(name);
    }

    public static boolean isNotNull(String name) {
        return warps.containsKey(name);
    }

    public static List<Message> getWarpList() {
        if (cachedWarpList.isEmpty()) {
            List<Message> format = new ArrayList<>(warps.size());

            for (Entry<String, Location> row : warps.entrySet()) {
                format.add(new Message(Mood.NEUTRAL, row.getKey(), Utils.locationToString(row.getValue())));
            }

            cachedWarpList.clear();
            cachedWarpList.addAll(format);
        }

        return new ArrayList<>(cachedWarpList);
    }

    private static void save() {
        if (Trillium.connection != null) {
            List<String> serialized = new ArrayList<>(warps.size());
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());

            for (Entry<String, Location> row : warps.entrySet()) {
                serialized.add(row.getKey() + ';' + Utils.locationToString(row.getValue()));
            }

            yml.set("rows", serialized);

            try {
                yml.save(WarpDatabase.wd());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (Entry<String, Location> row : warps.entrySet()) {
                try {
                    Statement statement = Trillium.connection.createStatement();
                    statement.executeUpdate("DELETE FROM warps");
                    statement.closeOnCompletion();

                    PreparedStatement ps = Trillium.connection.prepareStatement("INSERT INTO warps " +
                            "(name, loc-x, loc-y, loc-z, loc-world)" +
                            " VALUES (?, ?, ?, ?, ?);");
                    ps.setString(1, row.getKey());
                    ps.setInt(2, row.getValue().getBlockX());
                    ps.setInt(3, row.getValue().getBlockY());
                    ps.setInt(4, row.getValue().getBlockZ());
                    ps.setString(5, row.getValue().getWorld().getName());
                    ps.executeUpdate();
                    ps.closeOnCompletion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadWarps() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> serialized = yml.getStringList("rows");
        for (String deserialize : serialized) {
            warps.put(deserialize.split(";")[0], Utils.locationFromString(deserialize.split(";")[1]));
        }

        cachedWarpList.clear();
    }
}
