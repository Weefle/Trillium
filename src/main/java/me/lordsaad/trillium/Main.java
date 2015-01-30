package me.lordsaad.trillium;

import me.lordsaad.trillium.events.PlayerJoin;
import me.lordsaad.trillium.events.PlayerLeave;
import me.lordsaad.trillium.events.ServerListPing;
import me.lordsaad.trillium.motd.CommandMotd;
import me.lordsaad.trillium.tp.CommandTeleport;
import me.lordsaad.trillium.tp.CommandTeleportRequest;
import me.lordsaad.trillium.tp.CommandTeleportRequestAccept;
import me.lordsaad.trillium.tp.CommandTeleportRequestDeny;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new ServerListPing(), this);

        getCommand("trillium").setExecutor(new CommandTrillium());
        getCommand("motd").setExecutor(new CommandMotd());
        getCommand("teleport").setExecutor(new CommandTeleport());
        getCommand("teleportrequest").setExecutor(new CommandTeleportRequest());
        getCommand("teleportrequestaccept").setExecutor(new CommandTeleportRequestAccept());
        getCommand("teleportrequestdeny").setExecutor(new CommandTeleportRequestDeny());
    }

    public void onDisable() {
        saveDefaultConfig();
    }
}
