package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.events.SignInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignModule extends TrilliumModule {

    // Event trigger
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {

                    Sign sign = (Sign) event.getClickedBlock().getState();
                    String line = ChatColor.stripColor(sign.getLine(0));
                    String openText = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_OPEN);
                    String closeText = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_CLOSE);
                    if (line.startsWith(openText) && line.endsWith(closeText)) {

                        String command = line.substring(line.indexOf(openText) + 1, line.indexOf(closeText));

                        if (!command.equals(" ")) {
                            if (sign.getLine(3).equalsIgnoreCase("console")) {
                                SignInteractEvent e = new SignInteractEvent(event.getPlayer(), sign, command, Bukkit.getConsoleSender());
                                Bukkit.getPluginManager().callEvent(e);
                                event.setCancelled(true);
                            } else {
                                SignInteractEvent e = new SignInteractEvent(event.getPlayer(), sign, command, event.getPlayer());
                                Bukkit.getPluginManager().callEvent(e);
                            }
                        }
                    }
                }
            }
        }
    }

    // Make a new sign
    @EventHandler
    public void onSign(SignChangeEvent event) {

        String line = ChatColor.stripColor(event.getLine(0));
        ChatColor successColor = ChatColor.getByChar(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_COLOR_SUCCESS).split("&")[0]);
        String openText = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_OPEN);
        String closeText = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.SIGN_CLOSE);

        if (line.startsWith(openText) && line.endsWith(closeText)) {

            String command = line.substring(line.indexOf(openText) + 1, line.indexOf(closeText));

            if (!command.equals(" ")) {
                if (event.getPlayer().hasPermission(Permission.Sign.CREATE + command.toLowerCase())) {

                    event.setLine(0, ChatColor.GRAY + openText + successColor + command + ChatColor.GRAY + closeText);

                    if (event.getLine(3).contains("console")) {
                        event.setLine(3, ChatColor.GOLD + "Console");
                    } else {
                        event.setLine(3, ChatColor.GOLD + "Player");
                    }

                }
            }
        }
    }

    // The actual magic
    @EventHandler
    public void signInteract(SignInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getSign().getLine(2) == null) {
                Bukkit.dispatchCommand(event.getSender(), event.getCommand());
            } else {
                if (Trillium.permission != null) {
                    if (Trillium.permission.has(event.getPlayer(), event.getSign().getLine(2))
                            || Trillium.permission.playerInGroup(event.getPlayer(), event.getSign().getLine(2))) {
                        Bukkit.dispatchCommand(event.getSender(), event.getCommand());
                    }
                }
            }
        }
    }
}