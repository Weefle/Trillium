package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.GroupManager;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GroupManagerModule extends TrilliumModule {

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
                                new Message(Mood.GOOD, "TGM", "Added permission: " + ChatColor.AQUA + args[3]).to(cs);
                                new Message(Mood.GOOD, "TGM", "to player: " + ChatColor.AQUA + p.getProxy().getName()).to(cs);
                            } else {
                                new Message("TGM", Error.INVALID_PLAYER, args[2]).to(cs);
                            }
                        } else if (args[1].equalsIgnoreCase("group") || args[1].equalsIgnoreCase("g")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.addPermission(args[3]);
                                manager.addPermissionGroup(args[3]);
                                new Message(Mood.GOOD, "TGM", "Added permission: " + ChatColor.AQUA + args[3]).to(cs);
                                new Message(Mood.GOOD, "TGM", "to group: " + ChatColor.AQUA + manager.getGroup()).to(cs);

                            } else {
                                new Message("TGM", Error.INVALID_PLAYER, args[2]).to(cs);
                            }
                        } else {
                            new Message("TGM", Error.WRONG_ARGUMENTS, "/tgm <add/remove> <player/group> <permission>").to(cs);
                        }
                    } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
                        if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.removePermission(args[3]);
                                manager.removePermissionPlayer(args[3]);
                                new Message(Mood.GOOD, "TGM", "Removed permission: " + ChatColor.AQUA + args[3]).to(cs);
                                new Message(Mood.GOOD, "TGM", "from player: " + ChatColor.AQUA + p.getProxy().getName()).to(cs);

                            } else {
                                new Message("TGM", Error.INVALID_PLAYER, args[2]).to(cs);
                            }
                        } else if (args[1].equalsIgnoreCase("group") || args[1].equalsIgnoreCase("g")) {
                            TrilliumPlayer p = player(args[2]);
                            if (p != null) {
                                GroupManager manager = new GroupManager(p.getProxy());
                                manager.removePermission(args[3]);
                                manager.removePermissionGroup(args[3]);
                                new Message(Mood.GOOD, "TGM", "Removed permission: " + ChatColor.AQUA + args[3]).to(cs);
                                new Message(Mood.GOOD, "TGM", "from group: " + ChatColor.AQUA + manager.getGroup()).to(cs);

                            } else {
                                new Message("TGM", Error.INVALID_PLAYER, args[2]).to(cs);
                            }
                        } else {
                            new Message("TGM", Error.WRONG_ARGUMENTS, "/tgm <add/remove> <player/group> <permission>").to(cs);
                        }
                    } else {
                        new Message("TGM", Error.WRONG_ARGUMENTS, "/tgm <add/remove> <player/group> <permission>").to(cs);
                    }
                } else {
                    new Message("TGM", Error.TOO_FEW_ARGUMENTS, "/tgm <add/remove> <player/group> <permission>").to(cs);
                }
            } else {
                new Message("TGM", Error.NO_PERMISSION).to(cs);
            }
        } else {
            new Message(Mood.BAD, "TGM", "This feature is disabled.").to(cs);
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