package me.lordsaad.trillium;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.serializer.LocationSerializer;
import me.lordsaad.trillium.commands.*;
import me.lordsaad.trillium.commands.teleport.*;
import me.lordsaad.trillium.databases.CmdBinderDatabase;
import me.lordsaad.trillium.events.*;
import me.lordsaad.trillium.modules.*;
import me.lordsaad.trillium.runnables.TpsRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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

        setupcmdbinder();

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        getCommand("trillium").setExecutor(new CommandTrillium());
        getCommand("motd").setExecutor(new CommandMotd());
        getCommand("teleport").setExecutor(new CommandTeleport());
        getCommand("teleportrequest").setExecutor(new CommandTeleportR());
        getCommand("teleportrequestaccept").setExecutor(new CommandTeleportRA());
        getCommand("teleportrequestdeny").setExecutor(new CommandTeleportRD());
        getCommand("teleporthere").setExecutor(new CommandTeleportH());
        getCommand("teleportrequesthere").setExecutor(new CommandTeleportRH());
        getCommand("inventory").setExecutor(new CommandInventory());
        getCommand("info").setExecutor(new CommandInfo());
        getCommand("message").setExecutor(new CommandMessage());
        getCommand("commandbinder").setExecutor(new CommandCmdBinder());
        getCommand("kittybomb").setExecutor(new CommandKittyBomb());
        getCommand("me").setExecutor(new CommandMe());
        getCommand("say").setExecutor(new CommandSay());
        getCommand("killall").setExecutor(new CommandKillall());
        getCommand("smite").setExecutor(new CommandSmite());
        getCommand("report").setExecutor(new CommandReport());
        getCommand("reports").setExecutor(new CommandReports());
        getCommand("nickname").setExecutor(new CommandNickname());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TpsRunnable(), 100, 1);

        File reports = new File(TrilliumAPI.getInstance().getDataFolder(), "Reports.yml");

        if (!reports.exists()) {
            try {
                reports.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration ymlreports = YamlConfiguration.loadConfiguration(reports);

        for (String s : ymlreports.getStringList("Reports")) {
            CommandReport.reportlist.add(s);
        }

        PluginDescriptionFile pdf = getDescription();
        getLogger().info("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        getLogger().info("           Plugin made with love");
        getLogger().info("   by LordSaad, VortexSeven, Turbotailz");
        getLogger().info("               and Samczsun");
        getLogger().info("                     ❤");
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
        yml.set("Reports", CommandReport.reportlist);
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

            CommandCmdBinder.touchconsole.put(loc, cmd);
            CommandCmdBinder.antilagcheckloc.add(loc);
        }

        for (String s : yml.getStringList("touchplayer")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CommandCmdBinder.touchplayer.put(loc, cmd);
            CommandCmdBinder.antilagcheckloc.add(loc);
        }

        for (String s : yml.getStringList("walkconsole")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CommandCmdBinder.walkconsole.put(loc, cmd);
            CommandCmdBinder.antilagcheckloc.add(loc);
        }

        for (String s : yml.getStringList("walkplayer")) {
            String w = s.split("'")[0];
            int x = Integer.parseInt(s.split(";")[0]);
            int y = Integer.parseInt(s.split(";")[1]);
            int z = Integer.parseInt(s.split(",")[1]);
            Location loc = new Location(Bukkit.getWorld(w), x, y, z);
            String cmd = s.split("/")[1];

            CommandCmdBinder.walkplayer.put(loc, cmd);
            CommandCmdBinder.antilagcheckloc.add(loc);
        }
    }
}