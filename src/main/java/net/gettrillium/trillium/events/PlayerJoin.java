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

import java.util.List;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        String joinMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.JOINMESSAGE));
        joinMessage = joinMessage.replace("[USERNAME]", p.getName());
        event.setJoinMessage(joinMessage);

        //motd
        if (p.hasPermission(Permission.Chat.MOTD)) {
            List<String> motd = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Chat.INGAME_MOTD);
            for (String s : motd) {
                s = s.replace("[USERNAME]", p.getName());
                s = s.replace("[SLOTS]", "" + Bukkit.getMaxPlayers());
                s = s.replace("[ONLINE]", "" + Bukkit.getOnlinePlayers().size());
                s = ChatColor.translateAlternateColorCodes('&', s);
                p.sendMessage(s);
            }
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
