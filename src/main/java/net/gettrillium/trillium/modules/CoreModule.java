package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class CoreModule extends TrilliumModule {
    public CoreModule() {
        super("core");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        TrilliumAPI.createNewPlayer(e.getPlayer());

        Player p = e.getPlayer();
        String joinMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.JOINMESSAGE));
        joinMessage = joinMessage.replace("[USERNAME]", p.getName());
        e.setJoinMessage(joinMessage);

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

        //Important broadcasts
        if (getConfig().getBoolean(Configuration.Broadcast.IMP_ENABLED)) {
            Utils.broadcastImportantMessage();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String quitMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.LEAVEMESSAGE));
        quitMessage = quitMessage.replace("[USERNAME]", p.getName());
        e.setQuitMessage(quitMessage);
        
        TrilliumAPI.getPlayer(e.getPlayer().getName()).dispose();
    }
}
