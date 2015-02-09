package me.lordsaad.trillium.events;

import java.io.IOException;
import java.util.ArrayList;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandReport;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.databases.PlayerDatabase;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TrilliumPlayer player = TrilliumAPI.createNewPlayer(event.getPlayer());
        
        
        Player p = event.getPlayer();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        try {
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //join message
        if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
            String m1 = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString("Join.message"));
            m1 = m1.replace("[USERNAME]", p.getName());
            event.setJoinMessage(m1);
        }

        //motd
        ArrayList<String> motd = (ArrayList<String>) TrilliumAPI.getInstance().getConfig().getStringList("Motd");
        for (String s : motd) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replace("[USERNAME]", p.getName());
            s = s.replace("[SLOTS]", String.valueOf(TrilliumAPI.getInstance().getServer().getMaxPlayers()));
            s = s.replace("[ONLINE]", String.valueOf(Bukkit.getOnlinePlayers().size()));
            p.sendMessage(s);
        }

        //god mode?
        if (TrilliumAPI.getInstance().getConfig().getBoolean("God Mode")) {
            CommandGodMode.godmodeusers.add(p.getUniqueId());
            Message.m(MType.W, p, "God Mode", "Remember! You are still in god mode!");
        } else {
            CommandGodMode.godmodeusers.remove(p.getUniqueId());
        }

        //vanish mode?
        if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
            Message.m(MType.W, p, "Vanish Mode", "Remember! You are still in vanish mode!");
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(p);
            }
        }

        //initiate AFK
        if (TrilliumAPI.getInstance().getConfig().getBoolean("AFK.auto afk.enabled")) {
            CommandAfk.afktimer.put(p.getUniqueId(), 0);
        }

        //Send report warning
        if (p.hasPermission("div.reportreceiver")) {
            if (CommandReport.reportlist.size() > 0) {
                Message.m(MType.W, p, "Reports", "There are " + CommandReport.reportlist.size() + " reports available for revision.");
                Message.m(MType.W, p, "Reports", "/reports to view them.");
            }
        }
    }
}
