package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandVanish;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        try {
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //leave message
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Leave Message")));

        //remove godmode?
        if (Main.plugin.getConfig().getBoolean("God Mode")) {
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
        }
    }
}
