package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.events.SignInteractEvent;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignModule extends TrilliumModule {

    // Event trigger
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock().getState() instanceof Sign) {

            Sign sign = (Sign) event.getClickedBlock().getState();
            String line = sign.getLine(0);
            String defaultFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_DETECT);
            String openSymbol = defaultFormat.split("%SIGN%")[0];
            String closeSymbol = defaultFormat.split("%SIGN%")[1];
            if (!openSymbol.equalsIgnoreCase(closeSymbol)) {
                openSymbol = openSymbol + " ";
                closeSymbol = " " + closeSymbol;
            }
            String requiredString = line.substring(line.indexOf(openSymbol) + 1, line.indexOf(closeSymbol));

            if (!requiredString.equals(" ")) {
                if (line.startsWith(openSymbol) && line.endsWith(closeSymbol)) {
                    if (Utils.isInEnum(requiredString, SignType.class)) {
                        if (event.getPlayer().hasPermission(Permission.Sign.USE + requiredString.toLowerCase())) {
                            SignInteractEvent e = new SignInteractEvent(event.getPlayer(), sign, SignType.valueOf(requiredString.toUpperCase()));
                            Bukkit.getPluginManager().callEvent(e);
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    // Make a new sign
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getState() instanceof Sign) {

            Sign sign = (Sign) event.getBlockPlaced().getState();
            String line = sign.getLine(0);
            String defaultFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_DETECT);
            String errorFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_ERROR);
            String successFormat = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_SUCCESS);
            String openSymbol = defaultFormat.split("%SIGN%")[0];
            String closeSymbol = defaultFormat.split("%SIGN%")[1];
            if (!openSymbol.equalsIgnoreCase(closeSymbol)) {
                openSymbol = openSymbol + " ";
                closeSymbol = " " + closeSymbol;
            }
            String requiredString = line.substring(line.indexOf(openSymbol) + 1, line.indexOf(closeSymbol));

            if (!requiredString.equals(" ")) {
                if (line.startsWith(openSymbol) && line.endsWith(closeSymbol)) {
                    if (event.getPlayer().hasPermission(Permission.Sign.CREATE + requiredString.toLowerCase())) {
                        if (Utils.isInEnum(requiredString, SignType.class)) {
                            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', successFormat.replace("%SIGN%", requiredString)));
                        } else {
                            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', errorFormat.replace("%SIGN%", requiredString)));
                        }
                    }
                }
            }
        }
    }

    // The actual magic
    @EventHandler
    public void signInteract(SignInteractEvent event) {
        if (event.getSignType() == SignType.FEED) {
            if (event.getSign().getLine(1) != null) {
                event.getPlayer().setFoodLevel(20);
                new Message(Mood.GOOD, "Feed", "Your hunger has been saturated.").to(event.getPlayer());
            } else {
                if (event.getPlayer().hasPermission(event.getSign().getLine(1))) {
                    event.getPlayer().setFoodLevel(20);
                    new Message(Mood.GOOD, "Feed", "Your hunger has been saturated.").to(event.getPlayer());
                }
            }
        }

        if (event.getSignType() == SignType.HEAL) {
            if (event.getSign().getLine(1) != null) {
                event.getPlayer().setHealth(20.0);
                new Message(Mood.GOOD, "Heal", "Your health has been restored.").to(event.getPlayer());
            } else {
                if (event.getPlayer().hasPermission(event.getSign().getLine(1))) {
                    event.getPlayer().setHealth(20.0);
                    new Message(Mood.GOOD, "Heal", "Your health has been restored.").to(event.getPlayer());
                }
            }
        }
    }
}
