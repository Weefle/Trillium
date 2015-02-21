package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.modules.AdminModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        String joinMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString("join.message"));
        joinMessage = joinMessage.replace("[USERNAME]", p.getName());
        event.setJoinMessage(joinMessage);

        //motd
        ArrayList<String> motd = (ArrayList<String>) TrilliumAPI.getInstance().getConfig().getStringList("Motd");
        for (String s : motd) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replace("[USERNAME]", p.getName());
            s = s.replace("[SLOTS]", "" + Bukkit.getMaxPlayers());
            s = s.replace("[ONLINE]", "" + Bukkit.getOnlinePlayers().size());
            p.sendMessage(s);
        }

        //Send report warning
        if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
            if (AdminModule.reportlist.size() > 0) {
                Message.m(MType.W, p, "Reports", "There are " + AdminModule.reportlist.size() + " reports available for revision.");
                Message.m(MType.W, p, "Reports", "/reports to view them.");
            }
        }
    }
}
