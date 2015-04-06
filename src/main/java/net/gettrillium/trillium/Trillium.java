package net.gettrillium.trillium;

import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.modules.AdminModule;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Trillium extends JavaPlugin {

    public void onEnable() {

        TrilliumAPI.setInstance(this);

        generateFiles();

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
        getLogger().warning("THIS PLUGIN IS STILL IN THE ALPHA STAGE.");
        getLogger().warning("WE HIGHLY RECOMMEND YOU DON'T USE IT FOR NOW");
        getLogger().warning("UNTIL AN OFFICIAL RELEASE IS OUT.");
        getLogger().warning("PLEASE REPORT ALL THE BUGS YOU FIND AT OUR RESOURCE PAGE.");

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