package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Configuration.Server;
import net.gettrillium.trillium.api.SQL.MySQL;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.Utils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Trillium extends JavaPlugin {

    public static Economy economy;
    public static Chat chat;
    public static  Connection connection;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        TrilliumAPI.setInstance(this);

        Utils.load();
        generateFiles();

        if (getConfig().getBoolean(Server.ALLOW_METRICS)) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                getLogger().warning("Failed to send plugin metrics... :(");
            }
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getLogger().info("Vault plugin detected! Attempting to hook into it...");
            if (setupChat()) {
                getLogger().info("Successfully hooked into vault chat.");
            } else {
                getLogger().warning("Could not hook into vault chat.");
            }

            if (getConfig().getBoolean(Configuration.Economy.ENABLED)) {
                if (setupEconomy()) {
                    getLogger().info("Successfully hooked into vault economy.");
                } else {
                    getLogger().warning("Could not hook into vault economy.");
                }
            }
        }

        if (getConfig().getBoolean(Server.SQL_ENABLED)) {
            MySQL mySQL = new MySQL(this,
                    getConfig().getString(Server.SQL_HOST_NAME),
                    getConfig().getString(Server.SQL_PORT),
                    getConfig().getString(Server.SQL_DATABASE),
                    getConfig().getString(Server.SQL_USER),
                    getConfig().getString(Server.SQL_PASS));
            try {
                connection = mySQL.openConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                        "uuid VARCHAR(36)," +
                        "nick VARCHAR(" + getConfig().getInt(Configuration.PlayerSettings.NICKNAMES_CHARACTER_LIMIT) + ")," +
                        "loc-x INT" +
                        "loc-y INT" +
                        "loc-z INT" +
                        "loc-world VARCHAR(50)" +
                        "muted BOOLEAN," +
                        "god BOOLEAN," +
                        "vanish BOOLEAN);");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS warps (" +
                        "name VARCHAR(50)," +
                        "loc-x INT" +
                        "loc-y INT" +
                        "loc-z INT" +
                        "loc-world VARCHAR(50));");
                statement.closeOnCompletion();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Update check
        String version = null;
        try (InputStream in = new URL("http://gettrillium.net/versionupdate/version.txt").openStream()) {
            version = StringEscapeUtils.escapeHtml(IOUtils.toString(in));
        } catch (IOException e) {
            getLogger().severe("Failed to check for updates!");
        }

        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("Plugin made with love by:");
        getLogger().info("LordSaad, VortexSeven, Turbotailz,");
        getLogger().info("samczsun, hintss");
        getLogger().info("<3");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("<<<-------------------------------------->>>");

        if (Utils.compareVersion(version, getDescription().getVersion())) {
            getLogger().info("<<<-------------------------------------->>>");
            getLogger().severe("NEW UPDATE AVAILABLE!");
            getLogger().severe("New version: " + version);
            getLogger().severe("http://gettrillium.net/");
            getLogger().info("<<<-------------------------------------->>>");
        }

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            getLogger().info("<<<-------------------------------------->>>");
            getLogger().warning("ESSENTIALS PLUGIN DETECTED!");
            getLogger().warning("Essentials might heavily interfere with Trillium!");
            getLogger().warning("Please consider removing it!");
            getLogger().warning("EWW! GET IT OFF, GET IT OFF, GET IT OFF! ~_~");
            getLogger().info("<<<-------------------------------------->>>");
        }
    }

    @Override
    public void onDisable() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Utils.unload();
    }

    private void generateFiles() {
        URL world = getClass().getResource("/world.yml");
        URL lordSaad = getClass().getResource("/LordSaad.yml");
        URL book = getClass().getResource("/example-book.txt");
        URL tune = getClass().getResource("/example-tune.txt");

        File bookDir = new File(getDataFolder(), "Books/example-book.txt");
        File lordSaadDir = new File(getDataFolder(), "Trillium Group Manager/players/LordSaad.yml");
        File worldDir = new File(getDataFolder(), "Trillium Group Manager/worlds/world.yml");
        File tuneDir = new File(getDataFolder(), "Tunes/example-tune.txt");

        try {
            FileUtils.copyURLToFile(book, bookDir);
            FileUtils.copyURLToFile(lordSaad, lordSaadDir);
            FileUtils.copyURLToFile(world, worldDir);
            FileUtils.copyURLToFile(tune, tuneDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
}
