package me.lordsaad.trillium;

import me.lordsaad.trillium.events.PlayerJoin;
import me.lordsaad.trillium.events.PlayerLeave;
import me.lordsaad.trillium.events.ServerListPing;
import me.lordsaad.trillium.gamemode.CommandGamemode;
import me.lordsaad.trillium.godmode.EntityDamage;
import me.lordsaad.trillium.godmode.CommandGodMode;
import me.lordsaad.trillium.invsee.CommandInvsee;
import me.lordsaad.trillium.motd.CommandMotd;
import me.lordsaad.trillium.teleport.*;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);

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
        getCommand("invsee").setExecutor(new CommandInvsee());

    }
    
    public void onDisable() {
        HandlerList.unregisterAll();
        saveDefaultConfig();
    }
}
