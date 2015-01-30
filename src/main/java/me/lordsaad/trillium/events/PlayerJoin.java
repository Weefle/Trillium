package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.PlayerDatabase;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by saad on 30-Jan-15.
 */
public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        YamlConfiguration yml;
        try {
            yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //motd
        ArrayList<String> motd = (ArrayList<String>) Main.plugin.getConfig().getStringList("Motd");
        for (String s : motd) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            p.sendMessage(s);
        }
    }
}
