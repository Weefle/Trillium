package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.Utils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Trillium extends JavaPlugin {

    public static Economy economy = null;
    public static Chat chat = null;

    public void onEnable() {

        saveDefaultConfig();

        TrilliumAPI.setInstance(this);

        Utils.load();
        generateFiles();

        if (getConfig().getBoolean(Configuration.Server.METRICS)) {
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

        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("Plugin made with love by:");
        getLogger().info("LordSaad, VortexSeven, Turbotailz,");
        getLogger().info("samczsun");
        getLogger().info("<3");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("<<<-------------------------------------->>>");

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            getLogger().info("<<<-------------------------------------->>>");
            getLogger().warning("ESSENTIALS PLUGIN DETECTED!");
            getLogger().warning("Essentials might heavily interfere with Trillium!");
            getLogger().warning("Please consider removing it!");
            getLogger().warning("EWW! GET IT OFF, GET IT OFF, GET IT OFF! ~_~");
            getLogger().info("<<<-------------------------------------->>>");
        }
    }

    public void onDisable() {
        Utils.unload();
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