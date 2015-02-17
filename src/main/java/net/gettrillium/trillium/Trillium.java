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
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Trillium extends JavaPlugin {

    public void onEnable() {

        saveDefaultConfig();
        
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
        try {
            generateFiles();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

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
        getLogger().info("<<<--------------------------------------->>>");

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

    private void generateFiles() throws URISyntaxException, IOException {

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

        File file = new File("resources/Trillium Group Manager");
        String absolutePath = file.getAbsolutePath();
        String[] files = getResourceListing(Trillium.class, absolutePath);

        for (String s : files) {
            File f = new File(s);
            if (!f.getName().equalsIgnoreCase("config") || !f.getName().equalsIgnoreCase("plugin")) {
                if (f.isDirectory()) {
                    f.mkdirs();
                } else {
                    f.createNewFile();
                }
            }
        }
    }

    private String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {

            String me = clazz.getName().replace(".", "/") + ".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL != null) {
            if (dirURL.getProtocol().equals("jar")) {
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries();
                Set<String> result = new HashSet<>();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path)) {
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            entry = entry.substring(0, checkSubdir);
                        }
                        result.add(entry);
                    }
                }
                return result.toArray(new String[result.size()]);
            }
        }
        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }
}