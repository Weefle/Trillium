package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.command.CommandException;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class AFKModule extends TrilliumModule {

    public AFKModule() {
        super("afk");
    }

    @Command(command = "afk", description = "Indicate that you are away from your keyboard.", usage = "/afk")
    public void onCommand(CommandSender sender, String[] args) throws CommandException {
        if (sender instanceof Player) {
            TrilliumPlayer player = player((Player) sender);
            if (player.getProxy().hasPermission(Permission.Afk.USE)) {
                player.toggleAfk();
            } else {
                Message.e(sender, "AFK", Crit.P);
            }
        } else {
            Message.e(sender, "AFK", Crit.C);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            player.toggleAfk();
        }
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            player.toggleAfk();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            TrilliumPlayer player = player((Player) event.getEntity());
            if (player.isAfk()) {
                player.toggleAfk();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isAfk()) {
            player.toggleAfk();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            TrilliumPlayer player = player(event.getPlayer());
            if (player.isAfk()) {
                player.toggleAfk();
            }
        }
    }

    @Override
    public void register() {
        if (getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            TrilliumAPI.getInstance().getServer().getScheduler().runTaskTimer(TrilliumAPI.getInstance(), new Runnable() {
                private List<TrilliumPlayer> toKick = new ArrayList<>();

                @Override
                public void run() {
                    for (TrilliumPlayer player : TrilliumAPI.getOnlinePlayers()) {
                        if (!player.isAfk()) {
                            if (player.getInactiveTime() >= getConfig().getInt(Configuration.Afk.AUTO_AFK_TIME)) {
                                if (getConfig().getBoolean(Configuration.Afk.AUTO_AFK_KICK)) {
                                    toKick.add(player);
                                } else {
                                    player.toggleAfk();
                                }
                            }
                        }
                    }

                    for (TrilliumPlayer player : toKick) {
                        player.getProxy().kickPlayer("You idled for too long.");
                        Message.b(MType.W, "AFK", player.getProxy().getName() + " got kicked for idling for too long.");
                    }
                    toKick.clear();
                }
            }, 0, 20);
        }
    }
}
