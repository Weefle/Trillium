package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.godmode.CommandGodMode;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        try {
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //join message
        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Join Message")));

        //motd
        ArrayList<String> motd = (ArrayList<String>) Main.plugin.getConfig().getStringList("Motd");
        for (String s : motd) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            p.sendMessage(s);
        }

        //god mode?
        if (Main.plugin.getConfig().getBoolean("God Mode")) {
            CommandGodMode.godmodeusers.add(p.getUniqueId());
        } else {
            CommandGodMode.godmodeusers.remove(p.getUniqueId());
        }
    }
}
