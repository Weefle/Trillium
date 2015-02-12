package me.lordsaad.trillium.events;

import java.util.ArrayList;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandReport;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        p.sendMessage("Player: Player join event is working");
        Bukkit.broadcastMessage("Broadcast: player join event is working");

        String m1 = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString("join.message"));
        m1 = m1.replace("[USERNAME]", p.getName());
        event.setJoinMessage(m1);

        //motd
        ArrayList<String> motd = (ArrayList<String>) TrilliumAPI.getInstance().getConfig().getStringList("Motd");
        for (String s : motd) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replace("[USERNAME]", p.getName());
            s = s.replace("[SLOTS]", String.valueOf(TrilliumAPI.getInstance().getServer().getMaxPlayers()));
            s = s.replace("[ONLINE]", String.valueOf(Bukkit.getOnlinePlayers().size()));
            p.sendMessage(s);
        }

        //Send report warning
        if (p.hasPermission("tr.reportreceiver")) {
            if (CommandReport.reportlist.size() > 0) {
                Message.m(MType.W, p, "Reports", "There are " + CommandReport.reportlist.size() + " reports available for revision.");
                Message.m(MType.W, p, "Reports", "/reports to view them.");
            }
        }
    }
}
