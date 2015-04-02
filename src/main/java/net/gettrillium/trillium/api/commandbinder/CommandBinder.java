package net.gettrillium.trillium.api.commandbinder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        HashMap<String, Location> commands = new HashMap<>();
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-console")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-console")));

        Boolean player = false;

        for (String cmd : commands.keySet()) {
            if (commands.get(cmd) == this.loc) {
                player = Boolean.parseBoolean(cmd.split("#;#")[0]);
                this.player = player;
            }
        }
        return player;
    }

    public String getCommand() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        HashMap<String, Location> commands = new HashMap<>();
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("touch-console")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-player")));
        commands.putAll(CommandBinderDeserializer(yaml.getStringList("walk-console")));

        String command = "/help";

        for (String cmd : commands.keySet()) {
            if (commands.get(cmd) == this.loc) {
                command = cmd;
                this.command = command;
            }
        }
        return command.split("#;#")[0];
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

    public HashMap<String, Location> CommandBinderDeserializer(List<String> serialized) {
        HashMap<String, Location> deserialized = new HashMap<>();
        for (String deserialize : serialized) {
            String[] split = deserialize.split(";");

            String command = split[0] + "#;#" + player;

            String x = split[1];
            String y = split[2];
            String z = split[3];
            String world = split[4];
            Location loc = new Location(Bukkit.getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));

            deserialized.put(command, loc);
        }

        return deserialized;
    }

    public List<String> CommandBinderSerializer(HashMap<String, Location> deserialized) {

        ArrayList<String> serialized = new ArrayList<>();
        ArrayList<Location> locations = new ArrayList<>();
        ArrayList<String> commands = new ArrayList<>();

        for (String command : deserialized.keySet()) {
            commands.add(command);
        }

        for (Location loc : deserialized.values()) {
            locations.add(loc);
        }

        for (String cmd : commands) {
            for (Location location : locations) {

                String x = "" + location.getX();
                String y = "" + location.getY();
                String z = "" + location.getZ();
                String world = location.getWorld().getName();

                serialized.add(cmd + ";" + x + ";" + y + ";" + z + ";" + world + ";" + player);
            }
        }

        return serialized;
    }
}
