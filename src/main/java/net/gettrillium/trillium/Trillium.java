package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Configuration.Server;
import net.gettrillium.trillium.api.SQL.SQL;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.report.Reports;
import net.gettrillium.trillium.api.warp.Warp;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Trillium extends JavaPlugin {

    public static Economy economy;
    public static Chat chat;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        TrilliumAPI.setInstance(this);

        load();
        generateFiles();
        SQL.load();

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
        SQL.close();
        unload();
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

    public static void load() {
        TrilliumAPI.getInstance().saveDefaultConfig();
        TrilliumAPI.getInstance().reloadConfig();

        TrilliumAPI.registerModules();
        TrilliumAPI.loadPlayers();

        CommandBinder.Blocks.setTable();
        CommandBinder.Items.setTable();
        Warp.loadWarps();
        Reports.setReports();

        new TpsRunnable().runTaskTimer(TrilliumAPI.getInstance(), 100L, 1L);
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Broadcast.AUTO_BROADCAST_ENABLED)) {
            new AutoBroadcastRunnable().runTaskTimer(TrilliumAPI.getInstance(), 1L, (long) Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Broadcast.AUTO_BROADCAST_FREQUENCY)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            new AFKRunnable().runTaskTimer(TrilliumAPI.getInstance(), 1L, (long) Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Afk.AUTO_AFK_TIME_UNTIL_IDLE)));
        }

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), TrilliumAPI.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(new ServerListPing(), TrilliumAPI.getInstance());
    }

    public static void unload() {
        Bukkit.getScheduler().cancelAllTasks();
        TrilliumAPI.getInstance().saveDefaultConfig();
        TrilliumAPI.disposePlayers();
        TrilliumAPI.unregisterModules();
    }
}
