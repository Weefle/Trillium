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
import java.util.Map.Entry;

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
            return command + ';' + player + ';' + x + ';' + y + ';' + z + ';' + world;
        }

        private static void save() {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
            List<String> serialized = new ArrayList<>(table.size());
            Map<String, Map<Location, Boolean>> rows = table.rowMap();

            for (Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                for (Entry<Location, Boolean> column : row.getValue().entrySet()) {
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
        private static Table<UUID, String, Map<Material, Boolean>> TABLE = HashBasedTable.create();

        public static void add(Player p, String command, Material mat, Boolean player) {
            Map<Material, Boolean> map = new EnumMap<>(Material.class);
            map.put(mat, player);
            TABLE.put(p.getUniqueId(), command, map);
            save(p);
        }

        public static void remove(Player player) {
            TrilliumPlayer p = TrilliumAPI.getPlayer(player);
            Map<UUID, Map<String, Map<Material, Boolean>>> rows = new HashMap<>();
            rows.putAll(TABLE.rowMap());

            for (Entry<UUID, Map<String, Map<Material, Boolean>>> row : rows.entrySet()) {
                if (row.getKey().equals(p.getProxy().getUniqueId())) {
                    for (Entry<String, Map<Material, Boolean>> column : row.getValue().entrySet()) {
                        TABLE.remove(row.getKey(), column.getKey());
                    }
                }
            }

            p.getConfig().set("command-binder-items", null);
            try {
                p.getConfig().save(p.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static String serializer(String command, Material mat, boolean player) {
            return command + ';' + mat.name() + ';' + player;
        }

        public static void setTable() {
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                List<String> serialized = p.getConfig().getStringList("command-binder-items");
                for (String deserialized : serialized) {
                    Map<Material, Boolean> map = new HashMap<>(1);
                    map.put(Material.valueOf(deserialized.split(";")[1]), Boolean.parseBoolean(deserialized.split(";")[2]));
                    TABLE.put(p.getProxy().getUniqueId(), deserialized.split(";")[0], map);
                }
            }
        }

        public static void save(Player player) {
            TrilliumPlayer p = TrilliumAPI.getPlayer(player);
            Map<UUID, Map<String, Map<Material, Boolean>>> rows = TABLE.rowMap();
            List<String> bindings = p.getConfig().getStringList("command-binder-items");

            for (Entry<UUID, Map<String, Map<Material, Boolean>>> row : rows.entrySet()) {
                if (row.getKey().equals(player.getUniqueId())) {
                    for (Entry<String, Map<Material, Boolean>> column : row.getValue().entrySet()) {
                        for (Entry<Material, Boolean> secondColumn : column.getValue().entrySet()) {
                            bindings.add(serializer(column.getKey(), secondColumn.getKey(), secondColumn.getValue()));
                        }
                    }
                }
            }
            remove(player);
            p.getConfig().set("command-binder-items", bindings);
            try {
                p.getConfig().save(p.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<String> getCommands(Player p, Material mat) {
            List<String> commands = new ArrayList<>();
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
