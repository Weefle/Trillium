package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.cooldown.Cooldown;
import net.gettrillium.trillium.api.cooldown.CooldownType;
import net.gettrillium.trillium.api.events.PlayerHomeEvent;
import net.gettrillium.trillium.api.events.PlayerWarpEvent;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.warp.Warp;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class TeleportModule extends TrilliumModule {

    // TODO: compress and improve

    private HashMap<UUID, UUID> tpr = new HashMap<>();
    private HashMap<UUID, UUID> tprh = new HashMap<>();

    @Command(command = "back",
            description = "Teleport to your last active position",
            usage = "/back",
            permissions = {Permission.Teleport.BACK, Permission.Teleport.COOLDOWN_EXEMPT})
    public void back(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player(cs.getName());
            if (p.getProxy().hasPermission(Permission.Teleport.BACK)) {
                if (!Cooldown.hasCooldown(p.getProxy(), CooldownType.TELEPORTATION)) {
                    if (!p.getProxy().isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                        Cooldown.setCooldown(p.getProxy(), CooldownType.TELEPORTATION, false);
                    }
                    new Message(Mood.GOOD, "Back", "You have been sent back to your last location.").to(p);
                    p.getProxy().teleport(p.getLastLocation());

                } else {
                    new Message(Mood.BAD, "Back", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.TELEPORTATION)).to(p);
                }
            } else {
                new Message("Back", Error.NO_PERMISSION).to(cs);
            }
        } else {
            new Message("Back", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "spawn",
            description = "Teleport to the server's spawn.",
            usage = "/spawn",
            permissions = {Permission.Teleport.SPAWN, Permission.Teleport.COOLDOWN_EXEMPT})
    public void spawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.SPAWN)) {
                if (!Cooldown.hasCooldown(p.getProxy(), CooldownType.TELEPORTATION)) {
                    if (!p.getProxy().isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                        Cooldown.setCooldown(p.getProxy(), CooldownType.TELEPORTATION, false);
                    }
                    p.getProxy().teleport(p.getProxy().getWorld().getSpawnLocation());
                    new Message(Mood.GOOD, "Spawn", "You were successfully teleported to the spawn point.").to(p);
                } else {
                    new Message(Mood.BAD, "Spawn", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.TELEPORTATION)).to(p);
                }
            } else {
                new Message("Spawn", Error.NO_PERMISSION).to(cs);
            }
        } else {
            new Message("Spawn", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "teleport",
            description = "Teleport to a person or a set of coordinates.",
            usage = "/tp <player/x y z] [player/x y z]",
            aliases = "tp",
            permissions = {Permission.Teleport.TP, Permission.Teleport.COOLDOWN_EXEMPT, Permission.Teleport.TP_OTHER, Permission.Teleport.TP_COORDS})
    public void tp(CommandSender cs, String[] args) {
        if (!cs.hasPermission(Permission.Teleport.TP_COORDS)) {
            new Message("TP", Error.NO_PERMISSION).to(cs);
            return;
        }

        if (args.length == 0) {
            new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <player> [player]/<x>, <y>, <z>]").to(cs);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        Player target2 = Bukkit.getPlayer(args[1]);

        if (target != null) {
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    new Message("TP", Error.CONSOLE_NOT_ALLOWED).to(cs);
                    return;
                }

                Player p = (Player) cs;
                p.teleport(target);

            } else {
                if (target2 != null) {
                    target.teleport(target2);
                    if (!target.getName().equals(cs.getName())) {
                        new Message(Mood.GOOD, "TP", target.getName() + " was teleported to " + target2.getName()).to(cs);
                    } else {
                        new Message(Mood.GOOD, "TP", "You were teleported to " + target2.getName()).to(cs);
                    }

                } else {
                    if (args.length < 4) {
                        new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <player> [player]/<x>, <y>, <z>]").to(cs);
                        return;
                    }

                    double x;
                    double y;
                    double z;

                    if (StringUtils.isNumeric(args[1])) {
                        x = Double.parseDouble(args[1]);
                    } else if (args[1].startsWith("~")) {
                        x = target.getLocation().getX() + Double.parseDouble(args[1]);
                    } else {
                        new Message("TP", Error.INVALID_PLAYER, args[1]).to(cs);
                        return;
                    }

                    if (StringUtils.isNumeric(args[2])) {
                        y = Double.parseDouble(args[2]);
                    } else if (args[2].startsWith("~")) {
                        y = target.getLocation().getX() + Double.parseDouble(args[2]);
                    } else {
                        new Message("TP", Error.WRONG_ARGUMENTS, args[2]).to(cs);
                        return;
                    }

                    if (StringUtils.isNumeric(args[3])) {
                        z = Double.parseDouble(args[3]);
                    } else if (args[3].startsWith("~")) {
                        z = target.getLocation().getX() + Double.parseDouble(args[3]);
                    } else {
                        new Message("TP", Error.WRONG_ARGUMENTS, args[3]).to(cs);
                        return;
                    }

                    Location loc = new Location(target.getLocation().getWorld(), x, y, z);
                    target.teleport(loc);
                    new Message(Mood.GOOD, "TP", cs.getName() + " teleported you to " + ChatColor.AQUA + Utils.locationSerializer(loc)).to(target);
                    new Message(Mood.GOOD, "TP", "You teleported " + target.getName() + " to " + ChatColor.AQUA + Utils.locationSerializer(loc)).to(cs);
                }
            }
        } else {
            if (args.length < 3) {
                new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <player> [player]/<x>, <y>, <z>]").to(cs);
                return;
            }

            if (!(cs instanceof Player)) {
                new Message("TP", Error.CONSOLE_NOT_ALLOWED).to(cs);
                return;
            }

            Player p = (Player) cs;

            double x;
            double y;
            double z;

            if (StringUtils.isNumeric(args[0])) {
                x = Double.parseDouble(args[0]);
            } else if (args[0].startsWith("~")) {
                x = p.getLocation().getX() + Double.parseDouble(args[0]);
            } else {
                new Message("TP", Error.INVALID_PLAYER, args[0]).to(cs);
                return;
            }

            if (StringUtils.isNumeric(args[1])) {
                y = Double.parseDouble(args[1]);
            } else if (args[1].startsWith("~")) {
                y = p.getLocation().getX() + Double.parseDouble(args[1]);
            } else {
                new Message("TP", Error.WRONG_ARGUMENTS, args[1]).to(cs);
                return;
            }

            if (StringUtils.isNumeric(args[2])) {
                z = Double.parseDouble(args[2]);
            } else if (args[2].startsWith("~")) {
                z = p.getLocation().getX() + Double.parseDouble(args[2]);
            } else {
                new Message("TP", Error.WRONG_ARGUMENTS, args[3]).to(cs);
                return;
            }

            Location loc = new Location(p.getLocation().getWorld(), x, y, z);
            p.teleport(loc);
            new Message(Mood.GOOD, "TP", "You were teleported to " + ChatColor.AQUA + Utils.locationSerializer(loc)).to(p);
        }
    }

    @Command(command = "teleporthere",
            description = "Teleport a player to you.",
            usage = "/tph <player>",
            aliases = "tph",
            permissions = {Permission.Teleport.TPHERE, Permission.Teleport.COOLDOWN_EXEMPT})
    public void teleporthere(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPH", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Teleport.TPHERE)) {
            new Message("TPH", Error.NO_PERMISSION).to(cs);
            return;
        }

        if (args.length == 0) {
            new Message("TPH", Error.TOO_FEW_ARGUMENTS, "/tphere <player>").to(p);
            return;
        }

        TrilliumPlayer target = player(args[0]);

        if (target == null) {
            new Message("TPH", Error.INVALID_PLAYER, args[0]).to(p);
            return;
        }
        if (!Cooldown.hasCooldown(p.getProxy(), CooldownType.TELEPORTATION)) {
            if (!p.getProxy().isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                Cooldown.setCooldown(p.getProxy(), CooldownType.TELEPORTATION, false);
            }
            target.getProxy().teleport(p.getProxy());
            new Message(Mood.GOOD, "TPH", "You teleported " + target.getProxy().getName() + " to you.").to(p);
            new Message(Mood.GOOD, "TPH", p.getProxy().getName() + " teleported you to them.").to(target);
        } else {
            new Message(Mood.BAD, "TPH", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.TELEPORTATION)).to(p);
        }
    }

    @Command(command = "teleportrequest",
            description = "Request permission to teleport to a player.",
            usage = "/tpr <player>",
            aliases = "tpr",
            permissions = {Permission.Teleport.TPREQEST, Permission.Teleport.COOLDOWN_EXEMPT})
    public void teleportrequest(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPR", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Teleport.TPREQEST)) {
            new Message("TPR", Error.NO_PERMISSION).to(p);
            return;
        }

        if (args.length != 0) {
            new Message("TPR", Error.TOO_FEW_ARGUMENTS, "/tpr <player>").to(p);
            return;
        }

        TrilliumPlayer target = player(args[0]);

        if (target == null) {
            new Message("TPR", Error.INVALID_PLAYER, args[0]).to(p);
            return;
        }

        if (Cooldown.hasCooldown(p.getProxy(), CooldownType.TELEPORTATION)) {
            new Message(Mood.BAD, "TPR", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.TELEPORTATION)).to(p);
            return;
        }

        if (!p.getProxy().isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
            Cooldown.setCooldown(p.getProxy(), CooldownType.TELEPORTATION, false);
        }

        new Message(Mood.NEUTRAL, "TPR", target.getName() + " is now pending. Please stand by.").to(p);

        new Message(Mood.NEUTRAL, "TPR", p.getName() + " would like to teleport to you.").to(target);
        new Message(Mood.NEUTRAL, "TPR", ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.").to(target);
        new Message(Mood.NEUTRAL, "TPR", ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.").to(target);

        tpr.put(p.getProxy().getUniqueId(), target.getProxy().getUniqueId());
    }

    @Command(command = "teleportrequesthere",
            description = "Request permission to teleport a player to you.",
            usage = "/tprh <player>",
            aliases = "tprh",
            permissions = {Permission.Teleport.TPREQESTHERE, Permission.Teleport.COOLDOWN_EXEMPT})
    public void teleportrequesthere(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPRH", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        Player p = (Player) cs;

        if (!p.hasPermission(Permission.Teleport.TPREQESTHERE)) {
            new Message("TPRH", Error.NO_PERMISSION).to(p);
            return;
        }

        if (args.length == 0) {
            new Message("TPRH", Error.TOO_FEW_ARGUMENTS, "/tprh <player>").to(p);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            new Message("TPRH", Error.INVALID_PLAYER, args[0]).to(p);
            return;
        }

        if (Cooldown.hasCooldown(p, CooldownType.TELEPORTATION)) {
            new Message(Mood.BAD, "TPRH", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p, CooldownType.TELEPORTATION)).to(p);
            return;
        }

        if (!p.isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
            Cooldown.setCooldown(p, CooldownType.TELEPORTATION, false);
        }

        new Message(Mood.NEUTRAL, "TPRH", "Teleport request for " + target.getName() + " to here is now pending. Please stand by.").to(p);

        new Message(Mood.NEUTRAL, "TPRH", p.getName() + ChatColor.BLUE + " would like you to teleport to him").to(target);
        new Message(Mood.NEUTRAL, "TPRH", ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.").to(target);
        new Message(Mood.NEUTRAL, "TPRH", ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.").to(target);
        tprh.put(p.getUniqueId(), target.getUniqueId());
    }

    @Command(command = "teleportrequestaccept",
            description = "Accept a teleport request.",
            usage = "/tpra",
            aliases = "tpra",
            permissions = {Permission.Teleport.TPRRESPOND, Permission.Teleport.COOLDOWN_EXEMPT})
    public void teleportrequestaccept(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPRA", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        Player p = (Player) cs;

        if (!p.hasPermission(Permission.Teleport.TPRRESPOND)) {
            new Message("TPRA", Error.NO_PERMISSION).to(p);
            return;
        }

        if (tpr.containsValue(p.getUniqueId())) {

            if (Cooldown.hasCooldown(p, CooldownType.TELEPORTATION)) {
                new Message(Mood.BAD, "TPRA", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p, CooldownType.TELEPORTATION)).to(p);
                return;
            }

            if (!p.isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                Cooldown.setCooldown(p, CooldownType.TELEPORTATION, false);
            }

            TrilliumPlayer requester = player(Bukkit.getPlayer(tpr.get(p.getUniqueId())));
            requester.getProxy().teleport(p);

            new Message(Mood.GOOD, "TPRA", "You teleported " + requester.getName() + " to you.").to(p);
            new Message(Mood.GOOD, "TPRA", p.getName() + " accepted your teleport request.").to(requester);
            tpr.remove(p.getUniqueId());

        } else if (tprh.containsValue(p.getUniqueId())) {

            if (Cooldown.hasCooldown(p, CooldownType.TELEPORTATION)) {
                new Message(Mood.BAD, "TPRA", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p, CooldownType.TELEPORTATION)).to(p);
                return;
            }

            if (!p.isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                Cooldown.setCooldown(p, CooldownType.TELEPORTATION, false);
            }

            TrilliumPlayer requester = player(Bukkit.getPlayer(tprh.get(p.getUniqueId())));

            p.teleport(requester.getProxy());
            new Message(Mood.GOOD, "TPRA", "You teleported to " + requester.getName()).to(p);
            new Message(Mood.GOOD, "TPRA", p.getName() + " accepted to teleport to you.").to(requester);

            tprh.remove(p.getUniqueId());

        } else {
            new Message(Mood.BAD, "TPRA", "No pending teleport requests to accept.").to(p);
        }
    }

    @Command(command = "teleportrequestdeny",
            description = "Deny a teleport request.",
            usage = "/tprd",
            aliases = "tprd",
            permissions = {Permission.Teleport.TPRRESPOND})
    public void teleportrequestdeny(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPRD", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        Player p = (Player) cs;

        if (!p.hasPermission(Permission.Teleport.TPRRESPOND)) {
            new Message("TPRD", Error.NO_PERMISSION).to(p);
            return;
        }

        if (tpr.containsValue(p.getUniqueId())) {
            Player requester = Bukkit.getPlayer(tpr.get(p.getUniqueId()));

            new Message(Mood.GOOD, "TPRD", "You denied " + ChatColor.AQUA + requester.getName() + "'s teleport request.").to(p);
            new Message(Mood.GOOD, "TPRD", p.getName() + " denied your teleport request.").to(requester);
            tpr.remove(p.getUniqueId());

        } else if (tprh.containsValue(p.getUniqueId())) {
            Player requester = Bukkit.getPlayer(tprh.get(p.getUniqueId()));

            new Message(Mood.GOOD, "TPRD", "You denied " + ChatColor.AQUA + requester.getName() + "'s teleport request.").to(p);
            new Message(Mood.GOOD, "TPRD", p.getName() + " denied your teleport request.").to(requester);
            tprh.remove(p.getUniqueId());

        } else {
            new Message(Mood.BAD, "TPRD", "No pending teleport requests to deny.").to(p);
        }
    }

    @Command(command = "teleportcoordinates",
            description = "Teleport to a set of coordinates.",
            usage = "/tpc [player] <x> <y> <z>",
            aliases = "tpc",
            permissions = {Permission.Teleport.TP_COORDS, Permission.Teleport.COOLDOWN_EXEMPT})
    public void teleportcoordinates(CommandSender cs, String[] args) {

        if (!cs.hasPermission(Permission.Teleport.TP_COORDS)) {
            new Message("TPC", Error.NO_PERMISSION).to(cs);
            return;
        }

        if (args.length < 3) {
            new Message("TPC", Error.TOO_FEW_ARGUMENTS, "/tpc [player] <x> <y> <z>").to(cs);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target != null) {
            if (args.length >= 4) {

                double x;
                double y;
                double z;

                if (StringUtils.isNumeric(args[1])) {
                    x = Double.parseDouble(args[1]);
                } else if (args[1].startsWith("~")) {
                    x = target.getLocation().getX() + Double.parseDouble(args[1]);
                } else {
                    new Message("TPC", Error.WRONG_ARGUMENTS, args[1]).to(cs);
                    return;
                }

                if (StringUtils.isNumeric(args[2])) {
                    y = Double.parseDouble(args[2]);
                } else if (args[2].startsWith("~")) {
                    y = target.getLocation().getX() + Double.parseDouble(args[2]);
                } else {
                    new Message("TPC", Error.WRONG_ARGUMENTS, args[2]).to(cs);
                    return;
                }

                if (StringUtils.isNumeric(args[3])) {
                    z = Double.parseDouble(args[3]);
                } else if (args[3].startsWith("~")) {
                    z = target.getLocation().getX() + Double.parseDouble(args[3]);
                } else {
                    new Message("TPC", Error.WRONG_ARGUMENTS, args[3]).to(cs);
                    return;
                }

                Location loc = new Location(target.getLocation().getWorld(), x, y, z);
                target.teleport(loc);
                new Message(Mood.GOOD, "TPC", "The console teleported you to " + ChatColor.AQUA + Utils.locationSerializer(loc)).to(target);
                new Message(Mood.GOOD, "TPC", "You teleported " + target.getName() + " to " + ChatColor.AQUA + Utils.locationSerializer(loc)).to(cs);

            } else {
                new Message("TPC", Error.TOO_FEW_ARGUMENTS, "/tpc [player] <x> <y> <z>").to(cs);
            }
        } else {
            if (!(cs instanceof Player)) {
                new Message("TPC", Error.CONSOLE_NOT_ALLOWED).to(cs);
                return;
            }

            Player p = (Player) cs;
            double x;
            double y;
            double z;

            if (StringUtils.isNumeric(args[0])) {
                x = Double.parseDouble(args[0]);
            } else if (args[0].startsWith("~")) {
                x = p.getLocation().getX() + Double.parseDouble(args[0]);
            } else {
                new Message("TPC", Error.WRONG_ARGUMENTS, args[0]).to(cs);
                return;
            }

            if (StringUtils.isNumeric(args[1])) {
                y = Double.parseDouble(args[1]);
            } else if (args[1].startsWith("~")) {
                y = p.getLocation().getX() + Double.parseDouble(args[1]);
            } else {
                new Message("TPC", Error.WRONG_ARGUMENTS, args[1]).to(cs);
                return;
            }

            if (StringUtils.isNumeric(args[2])) {
                z = Double.parseDouble(args[2]);
            } else if (args[2].startsWith("~")) {
                z = p.getLocation().getX() + Double.parseDouble(args[2]);
            } else {
                new Message("TPC", Error.WRONG_ARGUMENTS, args[2]).to(cs);
                return;
            }

            Location loc = new Location(p.getLocation().getWorld(), x, y, z);
            p.teleport(loc);
            new Message(Mood.GOOD, "TPC", "You were teleported to " + ChatColor.AQUA + Utils.locationSerializer(loc)).to(p);
        }
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            p.setLastLocation(event.getFrom());
        }
    }

    @Command(command = "warps",
            description = "View the list of warps available.",
            usage = "/warps",
            permissions = {Permission.Teleport.WARP})
    public void warps(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Teleport.WARP)) {

                p.sendMessage(ChatColor.GREEN + "Warps:");
                for (Message warps : Warp.getWarpList()) {
                    warps.to(p);
                }

            } else {
                new Message("Warps", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Warps", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "warp",
            description = "Teleport to a certain defined point.",
            usage = "/warp <name>",
            permissions = {Permission.Teleport.WARP, Permission.Teleport.COOLDOWN_EXEMPT})
    public void warp(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Teleport.WARP)) {
                if (args.length == 0) {
                    new Message("Warp", Error.TOO_FEW_ARGUMENTS, "/warps for a list of warps. /warp <name> to tp to a warp.").to(p);
                } else {
                    if (Warp.isNotNull(args[0])) {
                        if (!Cooldown.hasCooldown(p, CooldownType.TELEPORTATION)) {
                            if (!p.isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                                Cooldown.setCooldown(p, CooldownType.TELEPORTATION, false);
                            }
                            PlayerWarpEvent event = new PlayerWarpEvent(args[0], p, p.getLocation(), Warp.getLocation(args[0]));
                            Bukkit.getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                p.teleport(Warp.getLocation(args[0]));
                                new Message(Mood.GOOD, "Warp", "You were teleported to " + args[0]).to(p);
                            }
                        } else {
                            new Message(Mood.BAD, "Warp", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p, CooldownType.TELEPORTATION)).to(p);
                        }
                    } else {
                        new Message(Mood.BAD, "Warp", "There are no warps with that name.").to(p);
                    }
                }
            } else {
                new Message("Warp", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Warp", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "setwarp",
            description = "Save a new warp.",
            usage = "/setwarp <name>",
            permissions = {Permission.Teleport.WARP_SET})
    public void setwarp(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Teleport.WARP_SET)) {
                if (args.length == 0) {
                    new Message("Set Warp", Error.TOO_FEW_ARGUMENTS, "/setwarp <name>").to(p);
                } else {
                    if (Warp.isNotNull(args[0])) {
                        Warp.setWarp(args[0], p.getLocation());
                        new Message(Mood.GOOD, "Set Warp", "Warp saved as " + args[0]).to(p);
                    } else {
                        Warp.setWarp(args[0], p.getLocation());
                        new Message(Mood.GOOD, "Set Warp", "Warp " + args[0] + "'s position changed.").to(p);
                    }
                }
            } else {
                new Message("Set Warp", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Set Warp", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "delwarp",
            description = "Delete a warp.",
            usage = "/delwarp <name>",
            permissions = {Permission.Teleport.WARP_SET})
    public void delwarp(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Teleport.WARP_SET)) {
                if (args.length == 0) {
                    new Message("Delete Warp", Error.TOO_FEW_ARGUMENTS, "/setwarp <name>").to(p);
                } else {
                    if (Warp.isNotNull(args[0])) {
                        Warp.delWarp(args[0]);
                        new Message(Mood.GOOD, "Delete Warp", "Warp deleted was: " + args[0]).to(p);
                    } else {
                        new Message(Mood.BAD, "Delete Warp", "There are no warps with that name.").to(p);
                    }
                }
            } else {
                new Message("Delete Warp", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Delete Warp", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "homes",
            description = "View a list of all your homes.",
            usage = "/homes",
            permissions = {Permission.Teleport.VIEWHOME})
    public void homes(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.PlayerSettings.HOMES_ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);

                if (p.hasPermission(Permission.Teleport.VIEWHOME)) {

                    p.getProxy().sendMessage(ChatColor.GREEN + "Homes:");
                    for (Message homes : p.getHomeList()) {
                        homes.to(p);
                    }

                } else {
                    new Message("Homes", Error.NO_PERMISSION).to(p);
                }
            } else {
                new Message("Homes", Error.CONSOLE_NOT_ALLOWED).to(cs);
            }
        } else {
            new Message(Mood.BAD, "Homes", "This feature is disabled.").to(cs);
        }
    }

    @Command(command = "home",
            description = "Teleport to your home or view your home.",
            usage = "/home [home name]",
            permissions = {Permission.Teleport.TPHOME, Permission.Teleport.COOLDOWN_EXEMPT})
    public void home(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.PlayerSettings.HOMES_ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);

                if (p.hasPermission(Permission.Teleport.TPHOME)) {
                    if (args.length == 0) {
                        if (p.getHomeList().size() == 0) {
                            new Message(Mood.BAD, "Home", "You don't have any homes set.").to(p);

                        } else if (p.getHomeList().size() == 1) {
                            if (!Cooldown.hasCooldown(p.getProxy(), CooldownType.TELEPORTATION)) {
                                if (!p.getProxy().isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                                    Cooldown.setCooldown(p.getProxy(), CooldownType.TELEPORTATION, false);
                                }

                                if (p.homeIsNotNull("default")) {
                                    PlayerHomeEvent event = new PlayerHomeEvent("default", p.getProxy(), p.getProxy().getLocation(), p.getHomeLocation("default"));
                                    Bukkit.getPluginManager().callEvent(event);
                                    if (!event.isCancelled()) {
                                        p.getProxy().teleport(p.getHomeLocation("default"));
                                        new Message(Mood.GOOD, "Home", "You were teleported home.").to(p);
                                    }
                                }
                            } else {
                                new Message(Mood.BAD, "Home", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.TELEPORTATION)).to(p);
                            }
                        } else {
                            p.getProxy().sendMessage(ChatColor.GREEN + "Homes:");
                            for (Message homes : p.getHomeList()) {
                                homes.to(p);
                            }
                        }
                    } else {
                        if (p.homeIsNotNull(args[0])) {
                            if (!Cooldown.hasCooldown(p.getProxy(), CooldownType.TELEPORTATION)) {
                                if (!p.getProxy().isOp() && !p.hasPermission(Permission.Teleport.COOLDOWN_EXEMPT)) {
                                    Cooldown.setCooldown(p.getProxy(), CooldownType.TELEPORTATION, false);
                                }
                                p.getProxy().teleport(p.getHomeLocation(args[0]));
                                new Message(Mood.GOOD, "Home", "You teleported to home: " + args[0]).to(p);

                            } else {
                                new Message(Mood.BAD, "Home", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.TELEPORTATION)).to(p);
                            }
                        } else {
                            new Message(Mood.BAD, "Home", "You don't have a home with that name.").to(p);
                        }
                    }
                } else {
                    new Message("Home", Error.NO_PERMISSION).to(p);
                }
            } else {
                new Message("Home", Error.CONSOLE_NOT_ALLOWED).to(cs);
            }
        } else {
            new Message(Mood.BAD, "Home", "This feature is disabled.").to(cs);
        }
    }

    @Command(command = "sethome",
            description = "Set a new home.",
            usage = "/sethome [home name]",
            permissions = {Permission.Teleport.SETHOME, Permission.Teleport.SETHOME + "<input>"})
    public void sethome(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.PlayerSettings.HOMES_ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);

                if (p.hasPermission(Permission.Teleport.SETHOME)) {
                    if (args.length == 0) {
                        if (p.getHomeList().size() <= 1) {
                            p.setHome("default", p.getProxy().getLocation());
                            new Message(Mood.GOOD, "Set Home", "New default home position set.").to(p);
                        } else {
                            p.getProxy().sendMessage(ChatColor.GREEN + "Homes:");
                            for (Message homes : p.getHomeList()) {
                                homes.to(p);
                            }
                        }
                    } else {
                        new Message(Mood.BAD, "Set Home", "You're not allowed to set more than one home.").to(p);
                    }
                } else {
                    for (int i = 0; i < getConfig().getInt(Configuration.PlayerSettings.HOMES_MAX); i++) {
                        if (p.hasPermission(Permission.Teleport.SETHOME + "." + i) || p.getProxy().isOp()) {
                            if (args.length != 0) {
                                if (p.getHomeList().size() < i) {
                                    p.setHome(args[0], p.getProxy().getLocation());
                                    new Message(Mood.GOOD, "Set Home", "New home set: " + args[0]).to(p);
                                } else {
                                    if (p.homeIsNotNull(args[0])) {
                                        p.setHome(args[0], p.getProxy().getLocation());
                                        new Message(Mood.GOOD, "Set Home", "Home " + args[0] + "'s position changed.").to(p);
                                    } else {
                                        new Message(Mood.BAD, "Set Home", "You reached the maximum amount of homes you're allowed to set: " + i).to(p);
                                    }
                                }
                            } else {
                                new Message("Set Home", Error.TOO_FEW_ARGUMENTS, "/sethome <home name>").to(p);
                            }
                        } else {
                            new Message(Mood.BAD, "Set Home", "You have reached the maximum amount of homes you are allowed to have.").to(p);
                        }
                    }
                }
            } else {
                new Message("Set Home", Error.CONSOLE_NOT_ALLOWED).to(cs);
            }
        } else {
            new Message(Mood.BAD, "Set Home", "This feature is disabled.").to(cs);
        }
    }

    @Command(command = "delhome",
            description = "Delete a home.",
            usage = "/delhome [home name]",
            aliases = "deletehome",
            permissions = {Permission.Teleport.DELHOME})
    public void delhome(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.PlayerSettings.HOMES_ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);

                if (p.hasPermission(Permission.Teleport.DELHOME)) {
                    if (args.length != 0) {
                        if (p.homeIsNotNull(args[0])) {
                            p.delHome(args[0]);
                            new Message(Mood.GOOD, "Del Home", "Home " + args[0] + " successfully deleted.").to(p);
                        } else {
                            new Message(Mood.BAD, "Del Home", "You don't have a home with that name.").to(p);
                        }
                    } else {
                        new Message("Del Home", Error.TOO_FEW_ARGUMENTS).to(p);
                    }
                } else {
                    new Message("Del Home", Error.NO_PERMISSION).to(p);
                }
            } else {
                new Message("Del Home", Error.CONSOLE_NOT_ALLOWED).to(cs);
            }
        } else {
            new Message(Mood.BAD, "Del Home", "This feature is disabled.").to(cs);
        }
    }
}