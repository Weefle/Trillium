package me.lordsaad.trillium;

import java.io.File;
import java.io.IOException;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandBan;
import me.lordsaad.trillium.commands.CommandBroadcast;
import me.lordsaad.trillium.commands.CommandCmdBinder;
import me.lordsaad.trillium.commands.CommandFly;
import me.lordsaad.trillium.commands.CommandGamemode;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandInfo;
import me.lordsaad.trillium.commands.CommandInventory;
import me.lordsaad.trillium.commands.CommandKick;
import me.lordsaad.trillium.commands.CommandKillall;
import me.lordsaad.trillium.commands.CommandKittyBomb;
import me.lordsaad.trillium.commands.CommandLag;
import me.lordsaad.trillium.commands.CommandMe;
import me.lordsaad.trillium.commands.CommandMessage;
import me.lordsaad.trillium.commands.CommandMotd;
import me.lordsaad.trillium.commands.CommandMute;
import me.lordsaad.trillium.commands.CommandNickname;
import me.lordsaad.trillium.commands.CommandReport;
import me.lordsaad.trillium.commands.CommandReports;
import me.lordsaad.trillium.commands.CommandSay;
import me.lordsaad.trillium.commands.CommandSetSpawn;
import me.lordsaad.trillium.commands.CommandSmite;
import me.lordsaad.trillium.commands.CommandSpawn;
import me.lordsaad.trillium.commands.CommandSpeed;
import me.lordsaad.trillium.commands.CommandTrillium;
import me.lordsaad.trillium.commands.CommandUnban;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.commands.teleport.CommandBack;
import me.lordsaad.trillium.commands.teleport.CommandTeleport;
import me.lordsaad.trillium.commands.teleport.CommandTeleportH;
import me.lordsaad.trillium.commands.teleport.CommandTeleportR;
import me.lordsaad.trillium.commands.teleport.CommandTeleportRA;
import me.lordsaad.trillium.commands.teleport.CommandTeleportRD;
import me.lordsaad.trillium.commands.teleport.CommandTeleportRH;
import me.lordsaad.trillium.databases.CmdBinderDatabase;
import me.lordsaad.trillium.events.AsyncPlayerChat;
import me.lordsaad.trillium.events.EntityDamage;
import me.lordsaad.trillium.events.EntityRegainHealth;
import me.lordsaad.trillium.events.EntityTarget;
import me.lordsaad.trillium.events.FoodLevelChange;
import me.lordsaad.trillium.events.PlayerCommandPreprocess;
import me.lordsaad.trillium.events.PlayerDeath;
import me.lordsaad.trillium.events.PlayerDropItem;
import me.lordsaad.trillium.events.PlayerInteract;
import me.lordsaad.trillium.events.PlayerJoin;
import me.lordsaad.trillium.events.PlayerLeave;
import me.lordsaad.trillium.events.PlayerMove;
import me.lordsaad.trillium.events.PlayerPickupItem;
import me.lordsaad.trillium.events.ServerListPing;
import me.lordsaad.trillium.runnables.AfkRunnable;
import me.lordsaad.trillium.runnables.TpsRunnable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Trillium extends JavaPlugin {
    
    public void onEnable() {
        TrilliumAPI.setInstance(this);
        
        TrilliumAPI.registerCommand(CommandAfk.class);
        
        setupcmdbinder();

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        getServer().getPluginManager().registerEvents(new PlayerPickupItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new EntityRegainHealth(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        getCommand("trillium").setExecutor(new CommandTrillium());
        getCommand("motd").setExecutor(new CommandMotd());
        getCommand("teleport").setExecutor(new CommandTeleport());
        getCommand("teleportrequest").setExecutor(new CommandTeleportR());
        getCommand("teleportrequestaccept").setExecutor(new CommandTeleportRA());
        getCommand("teleportrequestdeny").setExecutor(new CommandTeleportRD());
        getCommand("teleporthere").setExecutor(new CommandTeleportH());
        getCommand("teleportrequesthere").setExecutor(new CommandTeleportRH());
        getCommand("gamemode").setExecutor(new CommandGamemode());
        getCommand("back").setExecutor(new CommandBack());
        getCommand("god").setExecutor(new CommandGodMode());
        getCommand("inventory").setExecutor(new CommandInventory());
        getCommand("broadcast").setExecutor(new CommandBroadcast());
        getCommand("info").setExecutor(new CommandInfo());
        getCommand("vanish").setExecutor(new CommandVanish());
        getCommand("fly").setExecutor(new CommandFly());
        getCommand("message").setExecutor(new CommandMessage());
        getCommand("commandbinder").setExecutor(new CommandCmdBinder());
        getCommand("kittybomb").setExecutor(new CommandKittyBomb());
        getCommand("setspawn").setExecutor(new CommandSetSpawn());
        getCommand("spawn").setExecutor(new CommandSpawn());
        getCommand("me").setExecutor(new CommandMe());
        getCommand("say").setExecutor(new CommandSay());
        getCommand("killall").setExecutor(new CommandKillall());
        getCommand("smite").setExecutor(new CommandSmite());
        getCommand("report").setExecutor(new CommandReport());
        getCommand("reports").setExecutor(new CommandReports());
        getCommand("lag").setExecutor(new CommandLag());
        getCommand("speed").setExecutor(new CommandSpeed());
        getCommand("nickname").setExecutor(new CommandNickname());
        getCommand("mute").setExecutor(new CommandMute());
        getCommand("ban").setExecutor(new CommandBan());
        getCommand("unban").setExecutor(new CommandUnban());
        getCommand("kick").setExecutor(new CommandKick());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AfkRunnable(), 1, getConfig().getInt("AFK.auto afk.time until idle") * 20);
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
        getLogger().info("   by LordSaad, VortexSeven, and Turbotailz");
        getLogger().info("                      ❤");
        getLogger().info("Version: " + pdf.getVersion());
        getLogger().warning("THIS PLUGIN IS STILL IN PRE-ALPHA.");
        getLogger().warning("WE HIGHLY RECOMMEND YOU DON'T USE IT FOR THE TIME BEING.");
        getLogger().warning("WE ARE FULLY AWARE OF ALL THE BUGS YOU MAY FIND.");
        getLogger().info("<<<-------------------------------->>>");

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            getLogger().warning("Essentials plugin detected!");
            getLogger().warning("Essentials might heavily interfere with Trillium");
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

        HandlerList.unregisterAll();
        saveDefaultConfig();
        Bukkit.getScheduler().cancelAllTasks();
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
