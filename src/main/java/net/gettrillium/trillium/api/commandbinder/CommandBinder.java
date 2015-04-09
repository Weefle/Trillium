package net.gettrillium.trillium.api.commandbinder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandBinder {

    private String command;
    private Location loc;
    private Boolean player;

    public CommandBinder(String command, Boolean player, Location location) {
        this.command = command;
        this.player = player;
        this.loc = location;
    }

    public CommandBinder(Location loc) {
        this.loc = loc;
        this.player = isPlayer();
        this.command = getCommand();
    }

    public void setToBlock() {
        if (player) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
            HashMap<String, Location> touch = new HashMap<>();
            touch.put(command, loc);
            touch.putAll(CommandBinderDeserializer(yaml.getStringList("touch-player")));

            List<String> serialized = CommandBinderSerializer(touch);
            yaml.set("touch-player", serialized);
            try {
                yaml.save(CommandBinderDatabase.cbd());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
            HashMap<String, Location> touch = new HashMap<>();
            touch.put(command, loc);
            touch.putAll(CommandBinderDeserializer(yaml.getStringList("touch-console")));

            List<String> serialized = CommandBinderSerializer(touch);
            yaml.set("touch-console", serialized);
            try {
                yaml.save(CommandBinderDatabase.cbd());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setToWalkableBlock() {
        if (player) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
            HashMap<String, Location> touch = new HashMap<>();
            touch.put(command, loc);
            touch.putAll(CommandBinderDeserializer(yaml.getStringList("walk-player")));

            List<String> serialized = CommandBinderSerializer(touch);
            yaml.set("walk-player", serialized);
            try {
                yaml.save(CommandBinderDatabase.cbd());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
            HashMap<String, Location> touch = new HashMap<>();
            touch.put(command, loc);
            touch.putAll(CommandBinderDeserializer(yaml.getStringList("walk-console")));

            List<String> serialized = CommandBinderSerializer(touch);
            yaml.set("walk-console", serialized);
            try {
                yaml.save(CommandBinderDatabase.cbd());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isPlayer() {
        return Boolean.valueOf(getCommandUnsplit().split("#~#")[1]);
    }

    public HashMap<String, Location> getAllCommands() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        HashMap<String, Location> commands = new HashMap<>();
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-console")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-console")));
        return commands;
    }

    private String getCommandUnsplit() {

        if (hasCommand()) {
            HashMap<String, Location> commands = getAllCommands();

            for (Map.Entry<String, Location> entry : commands.entrySet()) {
                if (this.loc.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
        }
        return "help#~#true";
    }

    public String getCommand() {
        return getCommandUnsplit().split("#~#")[0];
    }

    public Boolean hasCommand() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        HashMap<String, Location> commands = new HashMap<>();
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-console")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-console")));

        return commands.containsValue(loc);
    }

    public void removeCommand() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        HashMap<String, Location> commands = new HashMap<>();
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-console")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-console")));

        if (commands.containsValue(loc) || commands.containsKey(command)) {
            commands.remove(command);
        }
    }

    public HashMap<String, Location> CommandBinderDeserializer(List<String> serialized) {
        HashMap<String, Location> deserialized = new HashMap<>();
        for (String deserialize : serialized) {
            String[] split = deserialize.split(";");

            String command = split[0];

            String x = split[1];
            String y = split[2];
            String z = split[3];
            String world = split[4];
            Location loc = new Location(Bukkit.getWorld(world), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));

            deserialized.put(command, loc);
        }
        return deserialized;
    }

    public List<String> CommandBinderSerializer(HashMap<String, Location> deserialized) {

        ArrayList<String> serialized = new ArrayList<>();
        ArrayList<Location> locations = new ArrayList<>();
        ArrayList<String> commands = new ArrayList<>();

        for (Map.Entry<String, Location> serialize : deserialized.entrySet()) {
            if (serialize.getKey().split("#~#")[2] == null) {
                commands.add(serialize.getKey());
                locations.add(serialize.getValue());
            }
        }

        for (String cmd : commands) {
            for (Location location : locations) {

                String x = "" + location.getX();
                String y = "" + location.getY();
                String z = "" + location.getZ();
                String world = location.getWorld().getName();

                serialized.add(cmd + ";" + x + ";" + y + ";" + z + ";" + world);
            }
        }
        return serialized;
    }
}
