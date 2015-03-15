package net.gettrillium.trillium;

import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.serializer.LocationSerializer;
import net.gettrillium.trillium.databases.CmdBinderDatabase;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.modules.*;
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
        TrilliumAPI.registerModule(new KitModule());

        setupcmdbinder();
        generateFiles();

        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        Utils.reload();

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().warning("Failed to send plugin metrics... :(");
        }

        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("        Plugin made with love by:");
        getLogger().info("    LordSaad, VortexSeven, Turbotailz,");
        getLogger().info("           samczsun, and hintss");
        getLogger().info("                    <3");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("<<<-------------------------------------->>>");
        getLogger().warning("THIS PLUGIN IS STILL IN PRE-ALPHA.");
        getLogger().warning("WE HIGHLY RECOMMEND YOU DON'T USE IT FOR NOW");
        getLogger().warning("UNTIL AN OFFICIAL RELEASE IS OUT.");
        getLogger().warning("WE ARE FULLY AWARE OF ALL THE BUGS YOU MIGHT FIND.");

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            getLogger().info("<<<-------------------------------------->>>");
            getLogger().warning("Essentials plugin detected!");
            getLogger().warning("Essentials might heavily interfere with Trillium!");
            getLogger().warning("Please consider removing Essentials.");
            getLogger().info("<<<-------------------------------------->>>");
        }
    }

    public void onDisable() {
        File reports = new File(getDataFolder(), "Reports.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(reports);
        yml.set("Reports", AdminModule.reportlist);
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

        URL Url = getClass().getResource("/world.yml");
        File dest = new File(getDataFolder() + "/Trillium Group Manager/worlds/world.yml");
        try {
            FileUtils.copyURLToFile(Url, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        URL Url2 = getClass().getResource("/LordSaad.yml");
        File dest2 = new File(getDataFolder() + "/Trillium Group Manager/players/LordSaad.yml");
        try {
            FileUtils.copyURLToFile(Url2, dest2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}