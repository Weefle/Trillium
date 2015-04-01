package net.gettrillium.trillium.api.commandbinder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public class CommandBinder {

    private String command;
    private Location loc;
    private Boolean player;

    public CommandBinder(String command, Boolean player, Location loc) {
        this.command = command;
        this.player = player;
        this.loc = loc;
    }

    public void setToBlock() {
        if (player) {

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CmdBinderDatabase.cbd());
            HashMap<String, Location> touchplayer = new HashMap<>();
            touchplayer.put(command, loc);
            for (String serialized : yaml.getStringList("touch-player")) {
                touchplayer.putAll(deserializer(serialized));
            }

            for (HashMap<String, Location> deserialized : touchplayer) {

            }
        }
    }

    private HashMap<String, Location> deserializer(String serialized) {
        HashMap<String, Location> deserialized = new HashMap<>();
        String[] split = serialized.split(";");

        String command = split[0];

        String x = split[1];
        String y = split[2];
        String z = split[3];
        String world = split[4];
        Location loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));

        deserialized.put(command, loc);

        return deserialized;
    }

    private String serializer(HashMap<String, Location> deserialized) {
        String cmd = "";
        Location location = null;

        for (String command : deserialized.keySet()) {
            cmd = command;
        }

        for (Location loc : deserialized.values()) {
            location = loc;
        }

        String x, y, z, world;
        if (location != null) {
            x = "" + location.getX();
            y = "" + location.getY();
            z = "" + location.getZ();
            world = "" + location.getWorld().getName();
        } else {
            return null;
        }

        return cmd + ";" + x + ";" + y + ";" + z + ";" + world;
    }
}
