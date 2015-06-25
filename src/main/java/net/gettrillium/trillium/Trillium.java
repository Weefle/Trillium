package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.report.ReportDatabase;
import net.gettrillium.trillium.api.report.Reports;
import net.gettrillium.trillium.api.serializer.LocationSerializer;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Trillium extends JavaPlugin {

    public void onEnable() {

        saveDefaultConfig();

        TrilliumAPI.setInstance(this);

        TrilliumAPI.loadPlayers();

        TrilliumAPI.registerModules();
        TrilliumAPI.registerSerializer(Location.class, new LocationSerializer());

        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        generateFiles();

        CommandBinder.set();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new TpsRunnable(), 100, 1);
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Broadcast.AUTO_ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AutoBroadcastRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Broadcast.FREQUENCY)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AFKRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Afk.AUTO_AFK_TIME)));
        }


        if (getConfig().getBoolean(Configuration.Server.METRICS)) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                getLogger().warning("Failed to send plugin metrics... :(");
            }
        }

        Reports.setReports(Reports.deserialize());

        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("Plugin made with love by:");
        getLogger().info("LordSaad, VortexSeven, Turbotailz,");
        getLogger().info("samczsun, WouterG");
        getLogger().info("<3");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("<<<-------------------------------------->>>");

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            getLogger().info("<<<-------------------------------------->>>");
            getLogger().warning("Essentials plugin detected!");
            getLogger().warning("Essentials might heavily interfere with Trillium!");
            getLogger().warning("Please consider removing it!");
            getLogger().info("<<<-------------------------------------->>>");
        }
    }

    public void onDisable() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(ReportDatabase.report());
        yml.set("rows", Reports.serialize());
        Reports.save();

        Bukkit.getScheduler().cancelAllTasks();
        TrilliumAPI.disposePlayers();
        TrilliumAPI.unregisterModules();
    }

    private void generateFiles() {
        URL world = getClass().getResource("/world.yml");
        URL lordSaad = getClass().getResource("/LordSaad.yml");
        URL book = getClass().getResource("/example-book.txt");
        URL tune = getClass().getResource("/example-tune.txt");

        File bookDir = new File(getDataFolder() + "/Books/example-book.txt");
        File lordSaadDir = new File(getDataFolder() + "/Trillium Group Manager/players/LordSaad.yml");
        File worldDir = new File(getDataFolder() + "/Trillium Group Manager/worlds/world.yml");
        File tuneDir = new File(getDataFolder() + "/Tunes/example-tune.txt");

        try {
            FileUtils.copyURLToFile(book, bookDir);
            FileUtils.copyURLToFile(lordSaad, lordSaadDir);
            FileUtils.copyURLToFile(world, worldDir);
            FileUtils.copyURLToFile(tune, tuneDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}