package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.SignType;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.events.SignInteractEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock().getState() instanceof Sign) {

            Sign sign = (Sign) event.getClickedBlock().getState();
            String defaultFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_DETECT);
            String errorFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_ERROR);
            String successFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_SUCCESS);
            String openSymbol = defaultFormat.split("%SIGN%")[0];
            String closeSymbol = defaultFormat.split("%SIGN%")[1];
            String requiredString = sign.getLine(0).substring(sign.getLine(0).indexOf(openSymbol) + 1, sign.getLine(0).indexOf(closeSymbol));

            if (!requiredString.equals(" ")) {
                if (sign.getLine(0).startsWith(openSymbol) && sign.getLine(0).endsWith(closeSymbol)) {
                    if (Utils.isInEnum(requiredString, SignType.class)) {
                        sign.setLine(0, ChatColor.translateAlternateColorCodes('&', successFormat.replace("%SIGN%", requiredString)));
                        SignInteractEvent e = new SignInteractEvent(event.getPlayer(), sign, SignType.valueOf(requiredString));
                        Bukkit.getPluginManager().callEvent(e);
                    } else {
                        sign.setLine(0, ChatColor.translateAlternateColorCodes('&', errorFormat.replace("%SIGN%", requiredString)));
                    }
                }
            }
        }
    }
}
