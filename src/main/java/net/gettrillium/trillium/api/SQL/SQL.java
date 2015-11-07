package net.gettrillium.trillium.api.SQL;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;

/**
 * Created by Saad on 27/9/2015.
 */
public class SQL {

    private static Connection connection;
    private static FileConfiguration config = TrilliumAPI.getInstance().getConfig();

    /**
     * Check if sql is enabled in the config
     *
     * @return true
     */
    public static boolean sqlEnabled() {
        return config.getBoolean(Configuration.Server.SQL_ENABLED);
    }

    /**
     * Open database connection and load tables
     */
    public static void load() {
        if (sqlEnabled()) {
            if (config.getString(Configuration.Server.SQL_DATABASE_TYPE).equalsIgnoreCase("sqlite")) {
                SQLite sqlite = new SQLite(new File(TrilliumAPI.getInstance().getDataFolder(), "database.db").toString());
                try {
                    connection = sqlite.openConnection();

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                MySQL mySQL = new MySQL(
                        config.getString(Configuration.Server.SQL_HOST_NAME),
                        config.getString(Configuration.Server.SQL_PORT),
                        config.getString(Configuration.Server.SQL_DATABASE),
                        config.getString(Configuration.Server.SQL_USER),
                        config.getString(Configuration.Server.SQL_PASS));
                try {
                    connection = mySQL.openConnection();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                        "ID INT AUTO INCREMENT," +
                        "uuid VARCHAR(36)," +
                        "nick VARCHAR(" + config.getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT) + ")," +
                        "loc-x INT," +
                        "loc-y INT," +
                        "loc-z INT," +
                        "loc-world VARCHAR(255)," +
                        "muted BOOLEAN," +
                        "god BOOLEAN," +
                        "vanish BOOLEAN," +
                        "gamemode INT);");

                executeUpdate("CREATE TABLE IF NOT EXISTS warps (" +
                        "ID INT AUTO INCREMENT," +
                        "name VARCHAR(255)," +
                        "loc-x INT," +
                        "loc-y INT," +
                        "loc-z INT," +
                        "loc-world VARCHAR(255));");

                executeUpdate("CREATE TABLE IF NOT EXISTS commandbinder (" +
                        "ID INT AUTO INCREMENT," +
                        "command VARCHAR(255)," +
                        "player BOOLEAN," +
                        "loc-x INT," +
                        "loc-y INT," +
                        "loc-z INT," +
                        "loc-world VARCHAR(255));");
            }
        }
    }

    public static void executeUpdate(String sql) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                statement.closeOnCompletion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        if (connection != null) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            statement.closeOnCompletion();
            return result;
        }
        return null;
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        if (connection != null) {
            return connection.prepareStatement(sql);
        }
        return null;
    }

    /**
     * Close database connection
     */
    public static void close() {
        if (sqlEnabled()) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
