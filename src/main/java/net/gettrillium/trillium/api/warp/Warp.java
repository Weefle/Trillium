package net.gettrillium.trillium.api.warp;

import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warp {

    private String name;
    private Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Warp(String name) {
        this.name = name;
        this.location = getLocation();
    }

    public Warp(Location location) {
        this.location = location;
        this.name = getName();
    }

    public void teleport(Player p) {
        p.teleport(this.location);
    }

    public void teleport(TrilliumPlayer p) {
        p.getProxy().teleport(this.location);
    }

    public HashMap<String, Location> list() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> warps = yaml.getStringList("warps");
        return deserializer(warps);
    }

    public void delete() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> warps = yaml.getStringList("warps");
        HashMap<String, Location> deserialized = deserializer(warps);
        if (deserialized.containsKey(name)) {
            deserialized.remove(name);
            yaml.set("warps", serializer(deserialized));
            try {
                yaml.save(WarpDatabase.wd());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> warps = yaml.getStringList("warps");
        warps.add(serializer());
        yaml.set("warps", warps);
        try {
            yaml.save(WarpDatabase.wd());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> warps = yaml.getStringList("warps");
        HashMap<String, Location> deserialized = deserializer(warps);

        if (deserialized.get(name) != null) {
            return deserialized.get(name);
        } else {
            return null;
        }
    }

    public String getName() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(WarpDatabase.wd());
        List<String> warps = yaml.getStringList("warps");
        HashMap<String, Location> deserialized = deserializer(warps);

        for (Map.Entry<String, Location> keys : deserialized.entrySet()) {
            if (keys.getValue().equals(location)) {
                return keys.getKey();
            }
        }
        return null;
    }

    private String serializer() {

        String x = location.getX() + "";
        String y = location.getY() + "";
        String z = location.getZ() + "";
        String world = location.getWorld().getName();

        return name + ";" + x + ";" + y + ";" + z + ";" + world;
    }

    private List<String> serializer(HashMap<String, Location> deserialized) {

        List<String> serialized = new ArrayList<>();

        for (Map.Entry<String, Location> serialize : deserialized.entrySet()) {

            String name = serialize.getKey();
            String x = serialize.getValue().getX() + "";
            String y = serialize.getValue().getY() + "";
            String z = serialize.getValue().getZ() + "";
            String world = serialize.getValue().getWorld().getName();
            serialized.add(name + ";" + x + ";" + y + ";" + z + ";" + world);
        }

        return serialized;
    }

    private HashMap<String, Location> deserializer(List<String> warps) {
        HashMap<String, Location> deserialized = new HashMap<>();

        for (String serialized : warps) {
            String[] splits = serialized.split(";");

            String name = splits[0];
            double x = Double.parseDouble(splits[1]);
            double y = Double.parseDouble(splits[2]);
            double z = Double.parseDouble(splits[3]);
            World world = Bukkit.getWorld(splits[4]);
            Location location = new Location(world, x, y, z);
            deserialized.put(name, location);
        }
        return deserialized;
    }
}
