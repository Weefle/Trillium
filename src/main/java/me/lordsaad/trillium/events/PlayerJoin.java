package me.lordsaad.trillium.events;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.commands.CommandReport;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.modules.AbilityModule;
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
        TrilliumPlayer player = TrilliumAPI.createNewPlayer(event.getPlayer());
        
        Player p = event.getPlayer();

        //join message
        if (!player.isVanished()) {
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
        if (TrilliumAPI.isModuleEnabled(AbilityModule.class) && TrilliumAPI.getModule(AbilityModule.class).getConfig().getBoolean("godmode.enabled") && player.isGod()) {
            Message.m(MType.W, p, "God Mode", "Remember! You are still in god mode!");
        }

        //vanish mode?
        if (player.isVanished()) {
            Message.m(MType.W, p, "Vanish Mode", "Remember! You are still in vanish mode!");
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(p);
            }
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
