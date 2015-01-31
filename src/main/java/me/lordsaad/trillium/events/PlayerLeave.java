package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.godmode.CommandGodMode;
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
    }
}
