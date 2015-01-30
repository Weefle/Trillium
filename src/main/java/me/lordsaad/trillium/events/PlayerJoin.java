package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

        new BukkitRunnable() {
            double i = 1;
            boolean b = true;

            public void run() {
                if (b) {
                    i = i + 0.1;
                    if (i >= 3) {
                        b = false;
                    }
                } else {
                    i = i - 0.1;
                    if (i < 0) {
                        b = true;
                    }
                }
                for (Location l: Utils.upanimatedcircle(p.getLocation(), i, 100)) {
                    ParticleEffect.REDSTONE.display(0, 0, 0, 1, 10, l, 100);
                }
            }
        }.runTaskTimer(Main.plugin, 1, 1);
    }
}
