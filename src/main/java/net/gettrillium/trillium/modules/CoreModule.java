package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CoreModule extends TrilliumModule {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        TrilliumAPI.loadPlayer(e.getPlayer());

        if (p.hasPlayedBefore()) {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.JOINMESSAGE));
            joinMessage = joinMessage.replace("[USERNAME]", p.getName());
            e.setJoinMessage(joinMessage);
        } else {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.NEWJOINMESSAGE));
            joinMessage = joinMessage.replace("[USERNAME]", p.getName());
            e.setJoinMessage(joinMessage);

            List<String> commands = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.PlayerSettings.COMMANDS_TO_RUN);
            for (String command : commands) {
                command = command.replace("[p]", p.getName());
                command = ChatColor.translateAlternateColorCodes('&', command);
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            }

            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.PlayerSettings.TEMP_GOD_MODE_ENABLED)) {
                final TrilliumPlayer player = player(p);
                player.setGod(true);
                new BukkitRunnable() {
                    public void run() {
                        player.setGod(false);
                    }
                }.runTaskLater(TrilliumAPI.getInstance(), Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.TEMP_GOD_MODE_TIME)));
            }
        }

        //motd
        // world list?
        // maybe a "current online staff" list?
        // could define a trillium permission such as "trillium.staff.stafflist"
        // loop through all players with that permission and list em.
        if (p.hasPermission(Permission.Chat.MOTD)) {
            List<String> motd = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Chat.INGAME_MOTD);
            for (String s : motd) {
                s = s.replace("[USERNAME]", p.getName());
                s = s.replace("[UUID]", p.getUniqueId().toString());
                s = s.replace("[SLOTS]", "" + Bukkit.getMaxPlayers());
                s = s.replace("[ONLINE]", "" + Bukkit.getOnlinePlayers().size());
                s = s.replace("[UNIQUE]", getUniqueJoins());
                s = s.replace("[IP-ADDRESS]", p.getAddress().getAddress().getAddress().toString());
                s = s.replace("[NICKNAME]", TrilliumAPI.getPlayer(p).getDisplayName());
                s = ChatColor.translateAlternateColorCodes('&', s);
                p.sendMessage(s);
            }
        }

        //Send report warning
        if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
            if (AdminModule.reportlist.size() > 0) {
                new Message(Mood.BAD, "Reports", "There are " + AdminModule.reportlist.size() + " reports available for revision.").to(p);
                new Message(Mood.BAD, "Reports", "Do '/reports' to view them.").to(p);
            }
        }

        //Important broadcasts
        if (getConfig().getBoolean(Configuration.Broadcast.IMP_ENABLED)) {
            Utils.broadcastImportantMessage();
        }
    }
    
    // I wasn't sure where to put this so I just threw it here, you can move it whereever :)
    public static String getUniqueJoins() {
    	// getOfflinePlayers returns all players that have joined and are currently offline, that plus onlinePlayers..
    	// it's pretty self explanatory.
		int amount = Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size();
		String total = String.valueOf(amount);
		
    	return total;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String quitMessage = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.PlayerSettings.LEAVEMESSAGE));
        quitMessage = quitMessage.replace("[USERNAME]", p.getName());
        e.setQuitMessage(quitMessage);

        TrilliumAPI.disposePlayer(p);
    }
}
