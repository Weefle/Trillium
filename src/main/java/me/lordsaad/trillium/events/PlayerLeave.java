package me.lordsaad.trillium.events;

import me.lordsaad.trillium.PlayerDatabase;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        YamlConfiguration yml;

        try {
            yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
