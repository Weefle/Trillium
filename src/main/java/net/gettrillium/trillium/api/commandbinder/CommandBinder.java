package net.gettrillium.trillium.api.commandbinder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.LocationHandler;
import net.gettrillium.trillium.api.SQL.SQL;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public class CommandBinder {

    public static class Blocks {
        private static Table<String, String, Boolean> TABLE = HashBasedTable.create();

        public static void add(String command, Location loc, boolean player) {
            TABLE.put(command, LocationHandler.serialize(loc), player);
            save();
        }

        public static void remove(String command, Location loc) {
            TABLE.remove(command, LocationHandler.serialize(loc));
            save();
        }

        public static Table<String, String, Boolean> getTable() {
            return TABLE;
        }

        private static String serializer(String command, String loc, boolean player) {
            return command + ';' + player + ';' + loc;
        }

        private static void save() {
            if (!SQL.sqlEnabled()) {
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
                List<String> serialized = new ArrayList<>(TABLE.size());
                Map<String, Map<String, Boolean>> rows = TABLE.rowMap();

                for (Entry<String, Map<String, Boolean>> row : rows.entrySet()) {
                    for (Entry<String, Boolean> column : row.getValue().entrySet()) {
                        serialized.add(serializer(row.getKey(), column.getKey(), column.getValue()));
                    }
                }

                yml.set("rows", serialized);

                try {
                    yml.save(CommandBinderDatabase.cbd());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                Map<String, Map<String, Boolean>> rows = TABLE.rowMap();
                SQL.executeUpdate("DELETE * FROM commandbinder");

                for (Entry<String, Map<String, Boolean>> row : rows.entrySet()) {
                    for (Entry<String, Boolean> column : row.getValue().entrySet()) {
                        try {
                            PreparedStatement ps = SQL.prepareStatement("INSERT INTO commandbinder " +
                                    "(command, player, loc_x, loc_y, loc_z, loc_world)" +
                                    " VALUES (?, ?, ?, ?, ?, ?);");
                            if (ps != null) {
                                Location loc = LocationHandler.deserialize(column.getKey());
                                ps.setString(1, row.getKey());
                                ps.setBoolean(2, column.getValue());
                                ps.setInt(3, loc.getBlockX());
                                ps.setInt(4, loc.getBlockY());
                                ps.setInt(5, loc.getBlockZ());
                                ps.setString(6, loc.getWorld().getName());
                                ps.executeUpdate();
                                ps.closeOnCompletion();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public static void setTable() {
            if (!SQL.sqlEnabled()) {
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
                List<String> serialized = yml.getStringList("rows");
                for (String deserialize : serialized) {
                    String command = deserialize.split(";")[0];
                    boolean player = Boolean.parseBoolean(deserialize.split(";")[1]);
                    TABLE.put(command, deserialize.split(";")[2], player);
                }
            } else {
                try {
                    ResultSet result = SQL.executeQuery("SELECT * FROM commandbinder");
                    if (result != null) {
                        Location loc = new Location(Bukkit.getWorld(result.getString("loc_world")),
                                result.getInt("loc_x"), result.getInt("loc_y"), result.getInt("loc_z"));
                        String command = result.getString("command");
                        boolean player = result.getBoolean("player");
                        TABLE.put(command, LocationHandler.serialize(loc), player);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

            Map<UUID, Map<String, Map<Material, Boolean>>> rows = new HashMap<>(TABLE.rowMap());

            for (Entry<String, Map<Material, Boolean>> column : rows.get(p.getPlayer().getUniqueId()).entrySet()) {
                if (rows.containsKey(p.getPlayer().getUniqueId())) {
                    if (TABLE.containsColumn(p.getPlayer().getUniqueId())) {
                        TABLE.remove(p.getPlayer().getUniqueId(), column.getKey());
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
                    TABLE.put(p.getPlayer().getUniqueId(), deserialized.split(";")[0], map);
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
