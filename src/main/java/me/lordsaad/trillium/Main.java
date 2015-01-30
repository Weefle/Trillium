package me.lordsaad.trillium;

import me.lordsaad.trillium.events.PlayerJoin;
import me.lordsaad.trillium.events.PlayerLeave;
import me.lordsaad.trillium.events.ServerListPing;
import me.lordsaad.trillium.motd.CommandMotd;
import me.lordsaad.trillium.tp.CommandTeleport;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by saad on 29-Jan-15.
 */
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
    }

    public void onDisable() {
        saveDefaultConfig();
    }
}
