package net.gettrillium.trillium.api.commandbinder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandBinder {

    static private Table<String, Location, Boolean> table = HashBasedTable.create();

    public static void add(String command, Location loc, boolean player) {
        table.put(command, loc, player);
        save();
    }

    public static void remove(String command, Location loc) {
        getTable().remove(command, loc);
        save();
    }

    public static Table<String, Location, Boolean> getTable() {
        return table;
    }

    public static void setTable(Table<String, Location, Boolean> newTable) {
        table = newTable;
        save();
    }

    private static String serializer(String command, Location loc, boolean player) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        String world = loc.getWorld().getName();
        return command + ";" + player + ";" + x + ";" + y + ";" + z + ";" + world;
    }

    private static void save() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        ArrayList<String> serialized = new ArrayList<>();
        Map<String, Map<Location, Boolean>> rows = table.rowMap();
        for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
            for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {
                serialized.add(serializer(row.getKey(), column.getKey(), column.getValue()));
            }
        }

        yml.set("rows", serialized);

        try {
            yml.save(CommandBinderDatabase.cbd());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set() {
        Table<String, Location, Boolean> table = HashBasedTable.create();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        List<String> serialized = yml.getStringList("rows");
        for (String deserialize : serialized) {
            String command = deserialize.split(";")[0];
            boolean player = Boolean.parseBoolean(deserialize.split(";")[1]);
            int x = Integer.parseInt(deserialize.split(";")[2]);
            int y = Integer.parseInt(deserialize.split(";")[3]);
            int z = Integer.parseInt(deserialize.split(";")[4]);
            String world = deserialize.split(";")[5];
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            table.put(command, loc, player);
        }
        setTable(table);
    }
}