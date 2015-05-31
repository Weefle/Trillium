package net.gettrillium.trillium.api.commandbinder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
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
}
