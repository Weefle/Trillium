package net.gettrillium.trillium.api.commandbinder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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
        private static Table<UUID, String, HashMap<Material, Boolean>> table = HashBasedTable.create();

        public static void add(Player p, String command, Material mat, Boolean player) {
            HashMap<Material, Boolean> map = new HashMap<>();
            map.put(mat, player);
            table.put(p.getUniqueId(), command, map);
            save(p);
        }

        public static void remove(Player p) {
            Map<UUID, Map<String, HashMap<Material, Boolean>>> rows = new HashMap<>();
            rows.putAll(table.rowMap());

            for (Map.Entry<UUID, Map<String, HashMap<Material, Boolean>>> row : rows.entrySet()) {
                if (row.getKey().equals(p.getUniqueId())) {
                    for (Map.Entry<String, HashMap<Material, Boolean>> column : row.getValue().entrySet()) {
                        table.remove(row.getKey(), column.getKey());
                    }
                }
            }
            save(p);
        }

        public static String serializer(String command, Material mat, Boolean player) {
            return command + ";" + mat.name() + ";" + player;
        }

        public static void setTable() {
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                List<String> serialized = p.getConfig().getStringList("command-binder-items");
                for (String deserialized : serialized) {
                    HashMap<Material, Boolean> map = new HashMap<>();
                    map.put(Material.valueOf(deserialized.split(";")[1]), Boolean.parseBoolean(deserialized.split(";")[2]));
                    table.put(p.getProxy().getUniqueId(), deserialized.split(";")[0], map);
                }
            }
        }

        public static void save(Player player) {
            TrilliumPlayer p = TrilliumAPI.getPlayer(player);
            Map<UUID, Map<String, HashMap<Material, Boolean>>> rows = table.rowMap();

            for (Map.Entry<UUID, Map<String, HashMap<Material, Boolean>>> row : rows.entrySet()) {
                if (row.getKey().equals(player.getUniqueId())) {
                    for (Map.Entry<String, HashMap<Material, Boolean>> column : row.getValue().entrySet()) {
                        for (Map.Entry<Material, Boolean> secondColumn : column.getValue().entrySet()) {
                            List<String> bindings = p.getConfig().getStringList("command-binder-items");
                            bindings.add(serializer(column.getKey(), secondColumn.getKey(), secondColumn.getValue()));
                            p.getConfig().set("command-binder-items", bindings);
                        }
                    }
                }
            }
            try {
                p.getConfig().save(p.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static ArrayList<String> getCommands(Player p, Material mat) {
            ArrayList<String> commands = new ArrayList<>();
            for (String deserialize : TrilliumAPI.getPlayer(p).getConfig().getStringList("command-binder-items")) {
                if (deserialize.split(";")[1].equals(mat.name())) {
                    commands.add(deserialize.split(";")[0]);
                }
            }
            return commands;
        }

        public static CommandSender getSender(Player p, String command) {
            CommandSender sender = p;

            for (String deserialize : TrilliumAPI.getPlayer(p).getConfig().getStringList("command-binder-items")) {
                if (deserialize.split(";")[0].equals(command)) {
                    if (Boolean.parseBoolean(deserialize.split(";")[2])) {
                        sender = p;
                    } else {
                        sender = Bukkit.getConsoleSender();
                    }
                }
            }
            return sender;
        }
    }
}