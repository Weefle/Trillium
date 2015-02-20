package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.modules.AdminModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        String m1 = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.JOINMESSAGE));
        m1 = m1.replace("[USERNAME]", p.getName());
        event.setJoinMessage(m1);

        //Send report warning
        if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
            if (AdminModule.reportlist.size() > 0) {
                Message.m(MType.W, p, "Reports", "There are " + AdminModule.reportlist.size() + " reports available for revision.");
                Message.m(MType.W, p, "Reports", "/reports to view them.");
            }
        }
    }
}
