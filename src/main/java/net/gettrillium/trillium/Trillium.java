package net.gettrillium.trillium;

import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.serializer.LocationSerializer;
import net.gettrillium.trillium.databases.CmdBinderDatabase;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.PlayerJoin;
import net.gettrillium.trillium.events.PlayerLeave;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.modules.*;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Trillium extends JavaPlugin {

    public void onEnable() {

        TrilliumAPI.setInstance(this);
        TrilliumAPI.registerSerializer(Location.class, new LocationSerializer());

        TrilliumAPI.registerModule(new AFKModule());
        TrilliumAPI.registerModule(new PunishModule());
        TrilliumAPI.registerModule(new AbilityModule());
        TrilliumAPI.registerModule(new AdminModule());
        TrilliumAPI.registerModule(new CoreModule());
        TrilliumAPI.registerModule(new TeleportModule());
        TrilliumAPI.registerModule(new ChatModule());
        TrilliumAPI.registerModule(new FunModule());
        TrilliumAPI.registerModule(new CmdBinderModule());
        TrilliumAPI.registerModule(new GroupManagerModule());

        setupcmdbinder();
        generateFiles();

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TpsRunnable(), 100, 1);


        PluginDescriptionFile pdf = getDescription();
        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("           Plugin made with love");
        getLogger().info("   by LordSaad, VortexSeven, Turbotailz");
        getLogger().info("               and Samczsun");
        getLogger().info("Version: " + pdf.getVersion());
        getLogger().warning("THIS PLUGIN IS STILL IN PRE-ALPHA.");
        getLogger().warning("WE HIGHLY RECOMMEND YOU DON'T USE IT FOR THE TIME BEING.");
        getLogger().warning("WE ARE FULLY AWARE OF ALL THE BUGS YOU MAY FIND.");
        getLogger().info("<<<-------------------------------->>>");

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            getLogger().warning("Essentials plugin detected!");
            getLogger().warning("Essentials might heavily interfere with Trillium!");
            getLogger().warning("Please consider removing Essentials.");
        }
    }

    public void onDisable() {
        File reports = new File(TrilliumAPI.getInstance().getDataFolder(), "Reports.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(reports);
        yml.set("Reports", AdminModule.reportlist);
        try {
            yml.save(reports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupcmdbinder() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(CmdBinderDatabase.cbd());

        for (String s : yml.getStringList("touchconsole")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CmdBinderModule.touchconsole.put(loc, cmd);
            CmdBinderModule.antilagcheckloc.add(loc);
        }

        for (String s : yml.getStringList("touchplayer")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CmdBinderModule.touchplayer.put(loc, cmd);
            CmdBinderModule.antilagcheckloc.add(loc);
        }

        for (String s : yml.getStringList("walkconsole")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CmdBinderModule.walkconsole.put(loc, cmd);
            CmdBinderModule.antilagcheckloc.add(loc);
        }

        for (String s : yml.getStringList("walkplayer")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CmdBinderModule.walkplayer.put(loc, cmd);
            CmdBinderModule.antilagcheckloc.add(loc);
        }
    }

    private void generateFiles() {

        File reports = new File(getDataFolder(), "Reports.yml");

        if (!reports.exists()) {
            try {
                reports.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration ymlreports = YamlConfiguration.loadConfiguration(reports);

        for (String s : ymlreports.getStringList("Reports")) {
            AdminModule.reportlist.add(s);
        }

        URL url = Trillium.class.getResource("/resources/");

        File dir = null;
        try {
            dir = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (dir != null) {
            for (File file : dir.listFiles()) {
                if (!file.getName().equals("config.yml") && !file.getName().equals("plugin.yml")) {
                    if (file.isDirectory()) {
                        file.mkdirs();
                    } else {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}