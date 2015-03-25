package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Type;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
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

    HashMap<UUID, UUID> tpr = new HashMap<>();
    HashMap<UUID, UUID> tprh = new HashMap<>();

    public TeleportModule() {
        super("teleport");
    }

    @Command(command = "back", description = "Teleport to your last active position", usage = "/back")
    public void back(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (player.getProxy().hasPermission(Permission.Ability.BACK)) {
                Message.message(Type.GOOD, player.getProxy(), "Back", true, "You have been sent back to your last location.");
                player.getProxy().teleport(player.getLastLocation());
            } else {
                Message.error("Back", cs);
            }
        } else {
            Message.error("Back", cs);
        }
    }

    @Command(command = "spawn", description = "Teleport to the server's spawn.", usage = "/spawn")
    public void spawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.SPAWN)) {
                p.getProxy().teleport(p.getProxy().getWorld().getSpawnLocation());
                Message.message(Type.GOOD, p.getProxy(), "Spawn", true, "You were successfully teleported to the spawn point.");
            } else {
                Message.error("Spawn", cs);
            }
        } else {
            Message.error("Spawn", cs);
        }
    }

    @Command(command = "teleport", description = "Teleport to a person or a set of coordinates.", usage = "/tp <player> [player / <x>, <y>, <z>]", aliases = "tp")
    public void tp(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            Message.error("TP", cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (args.length == 0) {
            if (!p.hasPermission(Permission.Teleport.TP)) {
                Message.error("TP", cs);
                return;
            }

            Message.error(p.getProxy(), "TP", true, "/tp <player> [player]");
        } else if (args.length == 1) {
            if (!p.hasPermission(Permission.Teleport.TP)) {
                Message.error("TP", cs);
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                Message.error("TP", cs, args[0]);
                return;
            }

            p.getProxy().teleport(target);
            Message.message(Type.GOOD, p.getProxy(), "TP", true, "You teleported to " + target.getName());
        } else if (args.length == 2) {
            if (!p.hasPermission(Permission.Teleport.TP_OTHER)) {
                Message.error("TP", cs);
                return;
            }

            TrilliumPlayer target1 = player(args[0]);
            TrilliumPlayer target2 = player(args[1]);

            if (target1 == null) {
                Message.error("TP", cs, args[1]);
                return;
            }

            if (target2 == null) {
                Message.error("TP", cs, args[2]);
                return;
            }

            target1.getProxy().teleport(target2.getProxy());
            Message.message(Type.GOOD, p.getProxy(), "TP", true, "You teleported " + target1.getProxy().getName() + " to " + target2.getProxy().getName());
            Message.message(Type.GOOD, target1.getProxy(), "TP", true, p.getProxy().getName() + " teleported you to " + target2.getProxy().getName());
        } else {
            if (!p.getProxy().hasPermission(Permission.Teleport.TP_COORDS)) {
                Message.error("TP", cs);
                return;
            }

            Player pl = Bukkit.getPlayer(args[0]);

            if (pl == null) {
                Message.error("TP", cs, args[0]);
                return;
            }

            String xArg = args[1];
            String yArg = args[2];
            String zArg = args[3];

            if (StringUtils.isNumeric(xArg) && StringUtils.isNumeric(yArg) && StringUtils.isNumeric(zArg)) {
                int x = Integer.parseInt(xArg);
                int y = Integer.parseInt(yArg);
                int z = Integer.parseInt(zArg);

                Location loc = new Location(p.getProxy().getWorld(), x, y, z);

                pl.teleport(loc);
                Message.message(Type.GOOD, p.getProxy(), "TP", true, "You teleported to " + ChatColor.AQUA + x + ", " + y + ", " + z);
            } else {
                if (!xArg.startsWith("~") || !yArg.startsWith("~") || !zArg.startsWith("~")) {
                    Message.error(p.getProxy(), "TP", false, "/tp <player> [x] [y] [z]");
                    return;
                }

                if (!StringUtils.isNumeric(xArg.substring(1)) || !StringUtils.isNumeric(yArg.substring(1)) || !StringUtils.isNumeric(zArg.substring(1))) {
                    Message.message(Type.WARNING, p.getProxy(), "TP", true, "Something isn't a number...");
                    return;
                }

                int x, y, z;

                if (xArg.substring(1).equals("") || xArg.substring(1).equals(" ")) {
                    x = 0;
                } else {
                    x = Integer.parseInt(xArg.substring(1));
                }

                if (yArg.substring(1).equals("") || yArg.substring(1).equals(" ")) {
                    y = 0;
                } else {
                    y = Integer.parseInt(yArg.substring(1));
                }

                if (zArg.substring(1).equals("") || zArg.substring(1).equals(" ")) {
                    z = 0;
                } else {
                    z = Integer.parseInt(zArg.substring(1));
                }

                Location loc = p.getProxy().getLocation();
                pl.teleport(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z));
                Message.message(Type.GOOD, p.getProxy(), "TP", true, "You teleported to " + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
            }
        }
    }

    @Command(command = "teleporthere", description = "Teleport a player to you.", usage = "/tph <player>", aliases = "tph")
    public void teleporthere(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            Message.error("TPH", cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Teleport.TPHERE)) {
            Message.error("TPH", cs);
            return;
        }

        if (args.length == 0) {
            Message.error(p.getProxy(), "TPH", true, "/tphere <player>");
            return;
        }

        TrilliumPlayer target = player(args[0]);

        if (target == null) {
            Message.error("TPH", cs, args[0]);
            return;
        }

        target.getProxy().teleport(p.getProxy());
        Message.message(Type.GOOD, p.getProxy(), "TPH", true, "You teleported " + target.getProxy().getName() + " to you.");
        Message.message(Type.GOOD, target.getProxy(), "TPH", true, p.getProxy().getName() + " teleported you to them.");
    }

    @Command(command = "teleportrequest", description = "Request permission to teleport to a player.", usage = "/tpr <player>", aliases = "tpr")
    public void teleportrequest(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.TPREQEST)) {
                if (args.length != 0) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        Message.message(Type.GENERIC, p.getProxy(), "TPR", true, target.getProxy().getName() + " is now pending. Please stand by.");

                        Message.message(Type.GENERIC, target.getProxy(), "TPR", true, p.getProxy().getName() + " would like to teleport to you.");
                        Message.message(Type.GENERIC, target.getProxy(), "TPR", true, ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.");
                        Message.message(Type.GENERIC, target.getProxy(), "TPR", true, ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.");

                        tpr.put(p.getProxy().getUniqueId(), target.getProxy().getUniqueId());

                    } else {
                        Message.error("TPR", cs, args[0]);
                    }
                } else {
                    Message.error(p.getProxy(), "TPR", true, "/tpr <player>");
                }
            } else {
                Message.error("TPR", cs);
            }
        } else {
            Message.error("TPR", cs);
        }
    }

    @Command(command = "teleportrequesthere", description = "Request permission to teleport a player to you.", usage = "/tprh <player>", aliases = "tprh")
    public void teleportrequesthere(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.TPREQESTHERE)) {
                if (args.length != 0) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        Message.message(Type.GENERIC, p.getProxy(), "TPRH", true, "Teleport request for " + target.getProxy().getName() + " to here is now pending. Please stand by.");
                        Message.message(Type.GENERIC, target.getProxy(), "TPRH", true, p.getProxy().getName() + ChatColor.BLUE + " would like you to teleport to him");
                        Message.message(Type.GENERIC, target.getProxy(), "TPRH", true, ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.");
                        Message.message(Type.GENERIC, target.getProxy(), "TPRH", true, ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.");
                        tprh.put(p.getProxy().getUniqueId(), target.getProxy().getUniqueId());

                    } else {
                        Message.error("TPRH", cs, args[0]);
                    }
                } else {
                    Message.error(p.getProxy(), "TPRH", true, "/tprh <player>");
                }
            } else {
                Message.error("TPRH", cs);
            }
        }
    }

    @Command(command = "teleportrequestaccept", description = "Accept a teleport request.", usage = "/tpra", aliases = "tpra")
    public void teleportrequestaccept(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.TPRRESPOND)) {
                if (tpr.containsValue(p.getProxy().getUniqueId())) {

                    TrilliumPlayer requester = player(Bukkit.getPlayer(tpr.get(p.getProxy().getUniqueId())));

                    requester.getProxy().teleport(p.getProxy());
                    Message.message(Type.GOOD, p.getProxy(), "TPRA", true, "You teleported " + requester.getProxy().getName() + " to you.");
                    Message.message(Type.GOOD, requester.getProxy(), "TPRA", true, p.getProxy().getName() + " accepted your teleport request.");

                } else if (tprh.containsValue(p.getProxy().getUniqueId())) {

                    TrilliumPlayer requester = player(Bukkit.getPlayer(tprh.get(p.getProxy().getUniqueId())));

                    p.getProxy().teleport(requester.getProxy());
                    Message.message(Type.GOOD, p.getProxy(), "TPRA", true, "You teleported to " + requester.getProxy().getName());
                    Message.message(Type.GOOD, requester.getProxy(), "TPRA", true, p.getProxy().getName() + " accepted to teleport to you.");

                } else {
                    Message.message(Type.WARNING, p.getProxy(), "TPRA", true, "No pending teleport requests to accept.");
                }
            } else {
                Message.error("TPRA", cs);
            }
        } else {
            Message.error("TPRA", cs);
        }
    }

    @Command(command = "teleportrequestdeny", description = "Deny a teleport request.", usage = "/tprd", aliases = "tprd")
    public void teleportrequestdeny(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            Message.error("TPRD", cs);
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Teleport.TPRRESPOND)) {
            Message.error("TPRD", cs);
            return;
        }

        if (tpr.containsValue(p.getProxy().getUniqueId())) {
            TrilliumPlayer requester = player(Bukkit.getPlayer(tpr.get(p.getProxy().getUniqueId())));

            Message.message(Type.GOOD, p.getProxy(), "TPRD", true, "You denied " + ChatColor.AQUA + requester.getProxy().getName() + "'s teleport request.");
            Message.message(Type.GOOD, requester.getProxy(), "TPRD", true, p.getProxy().getName() + " denied your teleport request.");
            tpr.remove(p.getProxy().getUniqueId());
        } else if (tprh.containsValue(p.getProxy().getUniqueId())) {
            TrilliumPlayer requester = player(Bukkit.getPlayer(tprh.get(p.getProxy().getUniqueId())));

            Message.message(Type.GOOD, p.getProxy(), "TPRD", true, "You denied " + ChatColor.AQUA + requester.getProxy().getName() + "'s teleport request.");
            Message.message(Type.GOOD, requester.getProxy(), "TPRD", true, p.getProxy().getName() + " denied your teleport request.");
            tprh.remove(p.getProxy().getUniqueId());
        } else {
            Message.message(Type.WARNING, p.getProxy(), "TPRD", true, "No pending teleport requests to deny.");
        }
    }

    @Command(command = "teleportcoordinates", description = "Teleport to a set of coordinates.", usage = "/tpc <x> <y> <z>", aliases = "tpc")
    public void teleportcoordinates(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.getProxy().hasPermission(Permission.Teleport.TP_COORDS)) {
            Message.error("TP", cs);
            return;
        }

        if (args.length < 3) {
            Message.error(p.getProxy(), "TP", true, "/tp <x> <y> <z>");
            return;
        }

        String xArg = args[0];
        String yArg = args[1];
        String zArg = args[2];

        if (StringUtils.isNumeric(xArg) && StringUtils.isNumeric(yArg) && StringUtils.isNumeric(zArg)) {
            int x = Integer.parseInt(xArg);
            int y = Integer.parseInt(yArg);
            int z = Integer.parseInt(zArg);

            Location loc = new Location(p.getProxy().getWorld(), x, y, z);
            p.getProxy().teleport(loc);
            Message.message(Type.GOOD, p.getProxy(), "TP", true, "You teleported to " + ChatColor.AQUA + x + ", " + y + ", " + z);
        } else {
            if (!xArg.startsWith("~") || !yArg.startsWith("~") || !zArg.startsWith("~")) {
                Message.error(p.getProxy(), "TP", false, "/tp <player> [x] [y] [z]");
                return;
            }

            if (!StringUtils.isNumeric(xArg.substring(1)) || !StringUtils.isNumeric(yArg.substring(1)) || !StringUtils.isNumeric(zArg.substring(1))) {
                Message.message(Type.WARNING, p.getProxy(), "TP", true, "Something isn't a number...");
                return;
            }

            int x;
            int y;
            int z;

            if (xArg.substring(1).equals("") || xArg.substring(1).equals(" ")) {
                x = 0;
            } else {
                x = Integer.parseInt(xArg.substring(1));
            }

            if (yArg.substring(1).equals("") || yArg.substring(1).equals(" ")) {
                y = 0;
            } else {
                y = Integer.parseInt(xArg.substring(1));
            }

            if (zArg.substring(1).equals("") || zArg.substring(1).equals(" ")) {
                z = 0;
            } else {
                z = Integer.parseInt(xArg.substring(1));
            }

            Location loc = p.getProxy().getLocation();
            p.getProxy().teleport(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z));
            Message.message(Type.GOOD, p.getProxy(), "TPC", true, "You teleported to " + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        }
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            p.setLastLocation(event.getFrom());
        }
        if (getConfig().getBoolean(Configuration.Broadcast.IMP_ENABLED)) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                Utils.broadcastImportantMessage();
            }
        }
    }
}
