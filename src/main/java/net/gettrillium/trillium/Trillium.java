package net.gettrillium.trillium;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.commandbinder.CommandBinderDatabase;
import net.gettrillium.trillium.api.serializer.LocationSerializer;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.modules.AdminModule;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.GroupManagerRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Trillium extends JavaPlugin {

    public void onEnable() {

        saveDefaultConfig();

        TrilliumAPI.setInstance(this);
        TrilliumAPI.registerSerializer(Location.class, new LocationSerializer());
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        TrilliumAPI.registerModules();

        generateFiles();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new TpsRunnable(), 100, 1);
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Broadcast.AUTO_ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AutoBroadcastRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Broadcast.FREQUENCY)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AFKRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Afk.AUTO_AFK_TIME)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new GroupManagerRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.GM.RELOAD)));
        }

        if (getConfig().getBoolean(Configuration.Server.METRICS)) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                getLogger().warning("Failed to send plugin metrics... :(");
            }
        }

        Table<String, Location, Boolean> table = HashBasedTable.create();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(CommandBinderDatabase.cbd());
        List<String> serialized = yml.getStringList("commands");
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
        CommandBinder.setTable(table);

        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("Plugin made with love by:");
        getLogger().info("LordSaad, VortexSeven, Turbotailz,");
        getLogger().info("samczsun, hintss, and colt");
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
        File reports = new File(getDataFolder(), "Reports.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(reports);
        yml.set("Reports", AdminModule.reportList);
        try {
            yml.save(reports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFiles() {
        File reports = new File(getDataFolder(), "Reports.yml");

        if (!reports.exists()) {
            try {
                reports.createNewFile();
                getLogger().info("Successfully generated Reports.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration ymlreports = YamlConfiguration.loadConfiguration(reports);

        for (String s : ymlreports.getStringList("Reports")) {
            AdminModule.reportList.add(s);
        }

        URL world = getClass().getResource("/world.yml");
        URL lordSaad = getClass().getResource("/LordSaad.yml");
        URL book1 = getClass().getResource("/1.txt");
        URL book2 = getClass().getResource("/2.txt");
        URL book3 = getClass().getResource("/3.txt");
        URL tune = getClass().getResource("/example-tune.txt");

        File bookDir1 = new File(getDataFolder() + "/Books/example-book/1.txt");
        File bookDir2 = new File(getDataFolder() + "/Books/example-book/2.txt");
        File bookDir3 = new File(getDataFolder() + "/Books/example-book/3.txt");
        File lordSaadDir = new File(getDataFolder() + "/Trillium Group Manager/players/LordSaad.yml");
        File worldDir = new File(getDataFolder() + "/Trillium Group Manager/worlds/world.yml");
        File tuneDir = new File(getDataFolder() + "/Tunes/example-tune.txt");

        try {
            FileUtils.copyURLToFile(book1, bookDir1);
            FileUtils.copyURLToFile(book2, bookDir2);
            FileUtils.copyURLToFile(book3, bookDir3);
            getLogger().info("Successfully generated /example-book/");

            FileUtils.copyURLToFile(lordSaad, lordSaadDir);
            getLogger().info("Successfully generated /Trillium Group Manager/players/LordSaad.yml");

            FileUtils.copyURLToFile(world, worldDir);
            getLogger().info("Successfully generated /Trillium Group Manager/worlds/world.yml");

            FileUtils.copyURLToFile(tune, tuneDir);
            getLogger().info("Successfully generated /Tunes/example-tune.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}