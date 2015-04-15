package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.serializer.LocationSerializer;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.modules.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Trillium extends JavaPlugin {

    public static final String PERMISSION_BASE = "tr.homes.%d";
    public void onEnable() {

        TrilliumAPI.setInstance(this);

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
        TrilliumAPI.registerModule(new CommandBinderModule());
        TrilliumAPI.registerModule(new KitModule());
        //TrilliumAPI.registerModule(new GroupManagerModule());

        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        generateFiles();
        Utils.reload();

        int maxhomes = getConfig().getInt(Configuration.PlayerSettings.HOMES_MAX);
        Map<String, Boolean> children = new HashMap<>();
        for (int i = 1; i <= maxhomes; i++) {
            String node = String.format(PERMISSION_BASE, i);
            children.put(node, true);
            getServer().getPluginManager().addPermission(new Permission(node, children));
        }

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
        yml.set("Reports", AdminModule.reportlist);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration ymlreports = YamlConfiguration.loadConfiguration(reports);

        for (String s : ymlreports.getStringList("Reports")) {
            AdminModule.reportlist.add(s);
        }

        URL url = getClass().getResource("/world.yml");
        File dest = new File(getDataFolder() + "/Trillium Group Manager/worlds/world.yml");
        try {
            FileUtils.copyURLToFile(url, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        URL url2 = getClass().getResource("/LordSaad.yml");
        File dest2 = new File(getDataFolder() + "/Trillium Group Manager/players/LordSaad.yml");
        try {
            FileUtils.copyURLToFile(url2, dest2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}