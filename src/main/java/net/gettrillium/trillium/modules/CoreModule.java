package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.report.Reports;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        TrilliumAPI.loadPlayer(e.getPlayer());

        final TrilliumPlayer player = player(e.getPlayer());

        if (p.hasPlayedBefore()) {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.JOINMESSAGE));
            joinMessage = joinMessage.replace("%USERNAME%", p.getName());
            e.setJoinMessage(joinMessage);
        } else {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.NEWJOINMESSAGE));
            joinMessage = joinMessage.replace("%USERNAME%", p.getName());
            e.setJoinMessage(joinMessage);

            List<String> commands = getConfig().getStringList(Configuration.PlayerSettings.COMMANDS_TO_RUN);
            for (String command : commands) {
                command = command.replace("@p", p.getName());
                command = ChatColor.translateAlternateColorCodes('&', command);
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            }

            if (getConfig().getBoolean(Configuration.PlayerSettings.TEMP_GOD_MODE_ENABLED)) {

                player.setGod(true);
                new BukkitRunnable() {
                    public void run() {
                        player.setGod(false);
                    }
                }.runTaskLater(TrilliumAPI.getInstance(), Utils.timeToTickConverter(getConfig().getString(Configuration.PlayerSettings.TEMP_GOD_MODE_TIME)));
            }
        }

        // motd
        // world list?
        // maybe a "current online staff" list?
        // could define a trillium permission such as "trillium.staff.stafflist"
        // loop through all players with that permission and list em.
        if (p.hasPermission(Permission.Chat.MOTD)) {
            List<String> motd = getConfig().getStringList(Configuration.Chat.INGAME_MOTD);
            for (String s : motd) {
                s = s.replace("%USERNAME%", p.getName());
                s = s.replace("%UUID%", p.getUniqueId().toString());
                s = s.replace("%SLOTS%", "" + Bukkit.getMaxPlayers());
                s = s.replace("%ONLINE%", "" + Bukkit.getOnlinePlayers().size());
                s = s.replace("%UNIQUE%", "" + (Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size()));
                s = s.replace("%IP%", p.getAddress() + "");
                s = s.replace("%NICK%", TrilliumAPI.getPlayer(p).getDisplayName());
                s = ChatColor.translateAlternateColorCodes('&', s);
                p.sendMessage(s);
            }
        }

        // Send report warning
        if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
            if (Reports.getReportMessages().size() > 0) {
                new Message(Mood.BAD, "Reports", "There are " + Reports.getReportMessages().size() + " reports available for revision.").to(p);
                new Message(Mood.BAD, "Reports", "Do '/reports' to view them.").to(p);
            }
        }

        //Important broadcasts
        if (getConfig().getBoolean(Configuration.Broadcast.IMP_ENABLED)) {
            Utils.broadcastImportantMessage();
        }


        if (player.isVanished() || player.isShadowBanned()) {
            e.setJoinMessage(null);
        }

        if (player.isVanished() && !player.isShadowBanned()) {

            new Message(Mood.BAD, "Vanish", "Remember! You are still in vanish mode!").to(player);
            for (TrilliumPlayer online : TrilliumAPI.getOnlinePlayers()) {
                online.getProxy().hidePlayer(player.getProxy());
            }
        }

        if (player.isGod() && !player.isShadowBanned()) {
            new Message(Mood.BAD, "God", "Remember! You are still in god mode!").to(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        TrilliumPlayer player = player(e.getPlayer());

        String quitMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.PlayerSettings.LEAVEMESSAGE));
        quitMessage = quitMessage.replace("%USERNAME%", p.getName());
        if (!player.isVanished()) {
            e.setQuitMessage(quitMessage);
        } else {
            e.setQuitMessage(null);
        }

        TrilliumAPI.disposePlayer(p);
    }
}
