package net.gettrillium.trillium.api.commandbinder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class CommandBinder {

    public static class Blocks {
        private static Table<String, Location, Boolean> table = HashBasedTable.create();

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

        public static void setTable() {
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
        }
    }

    public static class Items {
        private static Table<String, UUID, HashMap<Material, Boolean>> table = HashBasedTable.create();

        public static void add(String command, Player p, Material mat, Boolean player) {
            HashMap<Material, Boolean> map = new HashMap<>();
            map.put(mat, player);
            table.put(command, p.getUniqueId(), map);
            save(p);
        }

        public static void remove(String command, Player p) {
            table.remove(command, p.getUniqueId());
            save(p);
        }

        public static String serializer(String command, UUID uuid, Material mat, Boolean player) {
            return command + ";" + uuid + ";" + mat.name() + ";" + player;
        }

        public static Table<String, UUID, HashMap<Material, Boolean>> getTable() {
            return table;
        }

        public static void setTable() {
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                String serialized = p.getConfig().getString("command-binder-item");
                HashMap<Material, Boolean> map = new HashMap<>();
                map.put(Material.valueOf(serialized.split(";")[2]), Boolean.parseBoolean(serialized.split(";")[3]));
                table.put(serialized.split(";")[0], Bukkit.getPlayer(serialized.split(";")[1]).getUniqueId(), map);
            }
        }

        public static void save(Player player) {
            TrilliumPlayer p = TrilliumAPI.getPlayer(player);
            Map<String, Map<UUID, HashMap<Material, Boolean>>> rows = table.rowMap();

            for (Map.Entry<String, Map<UUID, HashMap<Material, Boolean>>> row : rows.entrySet()) {
                for (Map.Entry<UUID, HashMap<Material, Boolean>> column : row.getValue().entrySet()) {
                    if (column.getKey().equals(player.getUniqueId())) {
                        for (Map.Entry<Material, Boolean> secondColumn : column.getValue().entrySet()) {
                            p.getConfig().set("command-binder-item", serializer(row.getKey(), column.getKey(), secondColumn.getKey(), secondColumn.getValue()));
                        }
                    } else {
                        p.getConfig().set("command-binder-item", null);
                    }
                }
            }

            try {
                p.getConfig().save(p.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static Boolean hasItemBound(Player player) {
            TrilliumPlayer p = TrilliumAPI.getPlayer(player);
            return p.getConfig().get("command-binder-item") != null;
        }
    }
}