package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.GroupManager;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.messageutils.Type;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GroupManagerModule extends TrilliumModule {

    public GroupManagerModule() {
        super("group-manager");
    }

    @Command(command = "trilliumgroupmanager", description = "Add/remove/demote/promote/give perms/remove perms...", usage = "/tgm <add/remove> <player/group> <permission>")
    public void trilliumgroupmanager(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.GM.ENABLED)) {
            if (cs.hasPermission(Permission.Admin.TGM) || cs.isOp()) {
                if (args.length >= 4) {
                    if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                        if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.addPermission(args[3]);
                                manager.addPermissionPlayer(args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "Added permission: " + ChatColor.AQUA + args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "to player: " + ChatColor.AQUA + p.getProxy().getName());
                            } else {
                                Message.error("TGM", cs, args[2]);
                            }
                        } else if (args[1].equalsIgnoreCase("group") || args[1].equalsIgnoreCase("g")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.addPermission(args[3]);
                                manager.addPermissionGroup(args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "Added permission: " + ChatColor.AQUA + args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "to group: " + ChatColor.AQUA + manager.getGroup());
                            } else {
                                Message.error("TGM", cs, args[2]);
                            }
                        } else {
                            Message.error(cs, "TGM", false, "/tgm <add/remove> <player/group> <permission>");
                        }
                    } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
                        if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.removePermission(args[3]);
                                manager.removePermissionPlayer(args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "Removed permission: " + ChatColor.AQUA + args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "from player: " + ChatColor.AQUA + p.getProxy().getName());
                            } else {
                                Message.error("TGM", cs, args[2]);
                            }
                        } else if (args[1].equalsIgnoreCase("group") || args[1].equalsIgnoreCase("g")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.removePermission(args[3]);
                                manager.removePermissionGroup(args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "Removed permission: " + ChatColor.AQUA + args[3]);
                                Message.message(Type.GOOD, cs, "TGM", true, "from group: " + ChatColor.AQUA + manager.getGroup());
                            } else {
                                Message.error("TGM", cs, args[2]);
                            }
                        } else {
                            Message.error(cs, "TGM", false, "/tgm <add/remove> <player/group> <permission>");
                        }
                    } else {
                        Message.error(cs, "TGM", false, "/tgm <add/remove> <player/group> <permission>");
                    }
                } else {
                    Message.error(cs, "TGM", true, "/tgm <add/remove> <player/group> <permission>");
                }
            } else {
                Message.error("TGM", cs);
            }
        } else {
            Message.message(Type.WARNING, cs, "TGM", true, "This feature is disabled.");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean(Configuration.GM.ENABLED)) {
            TrilliumPlayer p = player(event.getPlayer());
            GroupManager manager = new GroupManager(p.getProxy());
            if (!manager.hasAttachment()) {
                manager.addAttachment();
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (getConfig().getBoolean(Configuration.GM.ENABLED)) {
            TrilliumPlayer p = player(event.getPlayer());
            GroupManager manager = new GroupManager(p.getProxy());
            if (manager.hasAttachment()) {
                manager.removeAttachment();
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (getConfig().getBoolean(Configuration.GM.ENABLED)) {
            TrilliumPlayer p = player(event.getPlayer());
            GroupManager manager = new GroupManager(p.getProxy());
            if (manager.hasAttachment()) {
                manager.removeAttachment();
            }
        }
    }
}