package me.lordsaad.trillium.events;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.commands.CommandGodMode;
import org.bukkit.Bukkit;
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
        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Join Message")).replace("[USERNAME]", p.getName()));

        //motd
        ArrayList<String> motd = (ArrayList<String>) Main.plugin.getConfig().getStringList("Motd");
        for (String s : motd) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replace("[USERNAME]", p.getName());
            s = s.replace("[SLOTS]", String.valueOf(Main.plugin.getServer().getMaxPlayers()));
            s = s.replace("[ONLINE]", String.valueOf(Bukkit.getOnlinePlayers().size()));
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
