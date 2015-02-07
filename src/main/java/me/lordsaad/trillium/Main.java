package me.lordsaad.trillium;

import me.lordsaad.trillium.commands.*;
import me.lordsaad.trillium.events.*;
import me.lordsaad.trillium.commands.teleport.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    public static Main plugin;

    public void onEnable() {
        plugin = this;

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
        getCommand("afk").setExecutor(new CommandAfk());
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

        AfkRunnable afk = new AfkRunnable();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, afk, 1, 1);
        
        File cmdbinder = new File(Main.plugin.getDataFolder() + "/cmdbinder/");
        if (!cmdbinder.exists()) {
            cmdbinder.mkdir();
        }
        
        File reports = new File(Main.plugin.getDataFolder(), "Reports.yml");

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
        System.out.println("<<<---{[0]}--->>> Trillium <<<---{[0]}--->>>");
        System.out.println("           Plugin made with love");
        System.out.println("   by LordSaad, VortexSeven, and TurboTailz");
        System.out.println("                      ❤");
        System.out.println("Version: " + pdf.getVersion());
        System.out.println("<<<-------------------------------->>>");
    }
    
    public void onDisable() {
        File reports = new File(Main.plugin.getDataFolder(), "Reports.yml");
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
}
