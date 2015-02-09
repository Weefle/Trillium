package me.lordsaad.trillium.events;

import java.io.IOException;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.databases.PlayerDatabase;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        try {
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //leave message
        if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
            String m1 = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString("Leave.message"));
            m1 = m1.replace("[USERNAME]", p.getName());
            event.setQuitMessage(m1);
        }
        //remove godmode?
        if (TrilliumAPI.getInstance().getConfig().getBoolean("God Mode")) {
            CommandGodMode.godmodeusers.add(p.getUniqueId());
        } else {
            CommandGodMode.godmodeusers.remove(p.getUniqueId());
        }

        //remove vanish
        if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(p);
            }
        }

        //remove afk
        if (CommandAfk.afklist.contains(p.getUniqueId())) {
            CommandAfk.afklist.remove(p.getUniqueId());
            CommandAfk.afktimer.remove(p.getUniqueId());
        }
    }
}
