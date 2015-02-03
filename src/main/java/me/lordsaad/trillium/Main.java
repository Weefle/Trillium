package me.lordsaad.trillium;

import me.lordsaad.trillium.commands.*;
import me.lordsaad.trillium.events.*;
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
        getServer().getPluginManager().registerEvents(new PlayerPickupItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new EntityTarget(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);

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

    }
    
    public void onDisable() {
        HandlerList.unregisterAll();
        saveDefaultConfig();
    }
}
