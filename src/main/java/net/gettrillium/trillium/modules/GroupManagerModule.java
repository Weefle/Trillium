package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GroupManagerModule extends TrilliumModule {

    public GroupManagerModule() {
        super("group-manager");
    }

    @Command(command = "trilliumgroupmanager", description = "Add/remove/demote/promote/give perms/remove perms...", usage = "/tgm")
    public void trilliumgroupmanager(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permission.Admin.TGM) || sender.isOp()) {
            if (args.length >= 4) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                    if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {
                        TrilliumPlayer p = player(args[2]);
                        if (p != null) {
                            p.addPermission(args[3]);
                            p.addPermissionPlayer(args[3]);
                        } else {
                            Message.eplayer(sender, "TGM", args[2]);
                        }
                    } else if (args[1].equalsIgnoreCase("group") || args[1].equalsIgnoreCase("g")) {
                        TrilliumPlayer p = player(args[2]);
                        if (p != null) {
                            p.addPermission(args[3]);
                            p.addPermissionGroup(args[3]);
                        } else {
                            Message.eplayer(sender, "TGM", args[2]);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
                    if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {
                        TrilliumPlayer p = player(args[2]);
                        if (p != null) {
                            p.removePermission(args[3]);
                            p.removePermissionPlayer(args[3]);
                        } else {
                            Message.eplayer(sender, "TGM", args[2]);
                        }
                    } else if (args[1].equalsIgnoreCase("group") || args[1].equalsIgnoreCase("g")) {
                        TrilliumPlayer p = player(args[2]);
                        if (p != null) {
                            p.removePermission(args[3]);
                            p.removePermissionGroup(args[3]);
                        } else {
                            Message.eplayer(sender, "TGM", args[2]);
                        }
                    }
                }
            }
        } else {
            Message.e(sender, "TGM", Crit.P);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (!p.hasAttachment()) {
            p.addAttachment();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (p.hasAttachment()) {
            p.removeAttachment();
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (p.hasAttachment()) {
            p.removeAttachment();
        }
    }

    @Override
    public void register() {
        TrilliumAPI.getInstance().getServer().getScheduler().runTaskTimer(TrilliumAPI.getInstance(), new Runnable() {

            @Override
            public void run() {
                for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                    p.refreshPermissions();
                }
            }
        }, 0, getConfig().getInt(Configuration.GM.RELOAD) * 20 * 3600);
    }
}
