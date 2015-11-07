package net.gettrillium.trillium.api.warp;

import net.gettrillium.trillium.api.LocationHandler;
import net.gettrillium.trillium.api.SQL.SQL;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Warp {

    // TODO - serialize locs before adding them to lists

    private static Map<String, String> warps = new HashMap<>();
    private static final List<Message> cachedWarpList = new ArrayList<>();

    private Warp() {
    }

    public static void setWarp(String name, Location loc) {
        warps.put(name, LocationHandler.serialize(loc));
        save();

        cachedWarpList.clear();
    }

    public static void delWarp(String name) {
        warps.remove(name);
        save();

        cachedWarpList.clear();
    }

    public static Location getLocation(String name) {
        return LocationHandler.deserialize(warps.get(name));
    }

    public static boolean isNotNull(String name) {
        return warps.containsKey(name);
    }

    public static List<Message> getWarpList() {
        if (cachedWarpList.isEmpty()) {
            List<Message> format = new ArrayList<>(warps.size());

            for (Entry<String, String> row : warps.entrySet()) {
                format.add(new Message(Mood.NEUTRAL, row.getKey(), LocationHandler.toString(LocationHandler.deserialize(row.getValue()))));
            }

            cachedWarpList.clear();
            cachedWarpList.addAll(format);
        }

        return new ArrayList<>(cachedWarpList);
    }

    private static void save() {
        if (!SQL.sqlEnabled()) {
            List<String> serialized = new ArrayList<>(warps.size());
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());

            for (Entry<String, String> row : warps.entrySet()) {
                serialized.add(row.getKey() + ';' + row.getValue());
            }

            yml.set("rows", serialized);

            try {
                yml.save(WarpDatabase.wd());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            SQL.executeUpdate("DELETE * FROM warps");

            for (Entry<String, String> row : warps.entrySet()) {
                try {
                    PreparedStatement ps = SQL.prepareStatement("INSERT INTO warps " +
                            "(name, loc_x, loc_y, loc_z, loc_world)" +
                            " VALUES (?, ?, ?, ?, ?);");
                    if (ps != null) {
                        Location loc = LocationHandler.deserialize(row.getKey());
                        ps.setString(1, row.getKey());
                        ps.setInt(2, loc.getBlockX());
                        ps.setInt(3, loc.getBlockY());
                        ps.setInt(4, loc.getBlockZ());
                        ps.setString(5, loc.getWorld().getName());
                        ps.executeUpdate();
                        ps.closeOnCompletion();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadWarps() {
        if (!SQL.sqlEnabled()) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
            List<String> serialized = yml.getStringList("rows");
            for (String deserialize : serialized) {
                warps.put(deserialize.split(";")[0], deserialize.split(";")[1]);
            }
            cachedWarpList.clear();
        } else {
            try {
                ResultSet result = SQL.executeQuery("SELECT * FROM warps");
                if (result != null) {

                    Location loc = new Location(Bukkit.getWorld(result.getString("loc_world")),
                            result.getInt("loc_x"), result.getInt("loc_y"), result.getInt("loc_z"));
                    String name = result.getString("name");
                    warps.put(name, LocationHandler.serialize(loc));
                    cachedWarpList.clear();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}