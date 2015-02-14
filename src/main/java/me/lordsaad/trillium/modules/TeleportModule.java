package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportModule extends TrilliumModule {

    public TeleportModule() {
        super("teleport");
    }

    @Command(command = "spawn", description = "Teleport to the server's spawn.", usage = "/spawn")
    public void spawn(CommandSender cs) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.SPAWN)) {
                p.getProxy().teleport(p.getProxy().getWorld().getSpawnLocation());
            } else {
                Message.e(p.getProxy(), "Spawn", Crit.P);
            }
        } else {
            Message.e(cs, "Spawn", Crit.C);
        }
    }

    @Command(command = "teleport", description = "Teleport to a person or a set of coordinates.", usage = "/tp <player> [<x>, <y>, <z>]", aliases = "tp")
    public void spawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 0) {
                if (p.hasPermission(Permission.Teleport.TP)) {
                    Message.earg(p.getProxy(), "TP", "/tp <player> [player]");
                }
            } else if (args.length == 1) {
                if (p.hasPermission(Permission.Teleport.TP)) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {

                        p.getProxy().teleport(target);
                        Message.m(MType.G, p.getProxy(), "TP", "You teleported to " + target.getName());

                    } else {
                        Message.eplayer(p.getProxy(), "TP", args[0]);
                    }
                } else {
                    Message.e(p.getProxy(), "TP", Crit.P);
                }

            } else if (args.length == 2) {
                if (p.hasPermission(Permission.Teleport.TP_OTHER)) {
                    TrilliumPlayer target1 = player(args[0]);
                    TrilliumPlayer target2 = player(args[1]);
                    if (target1 != null) {
                        if (target2 != null) {


                            target1.getProxy().teleport(target2.getProxy());
                            Message.m(MType.G, p.getProxy(), "TP", "You teleported " + target1.getProxy().getName() + " to " + target2.getProxy().getName());
                            Message.m(MType.G, target1.getProxy(), "TP", p.getProxy().getName() + " teleported you to " + target2.getProxy().getName());

                        } else {
                            Message.eplayer(p.getProxy(), "TP", args[1]);
                        }
                    } else {
                        Message.eplayer(p.getProxy(), "TP", args[2]);
                    }
                } else {
                    Message.e(p.getProxy(), "TP", Crit.P);
                }

            } else {
                if (p.getProxy().hasPermission(Permission.Teleport.TP_COORDS)) {
                    Player pl = Bukkit.getPlayer(args[0]);
                    String c1 = args[1];
                    String c2 = args[2];
                    String c3 = args[3];
                    if (pl != null) {
                        if (Utils.isNumeric(c1) && Utils.isNumeric(c2) && Utils.isNumeric(c3)) {
                            int c4 = Integer.parseInt(c1);
                            int c5 = Integer.parseInt(c2);
                            int c6 = Integer.parseInt(c3);
                            Location loc = new Location(p.getProxy().getWorld(), c4, c5, c6);
                            pl.teleport(loc);
                            Message.m(MType.G, p.getProxy(), "TP", "You teleported to " + ChatColor.AQUA + c4 + ", " + c5 + ", " + c6);
                        } else {
                            if (c1.startsWith("~") && c2.startsWith("~") && c3.startsWith("~")) {
                                if (Utils.isNumeric(c1.substring(1)) && Utils.isNumeric(c2.substring(1)) && Utils.isNumeric(c3.substring(1))) {
                                    int c4;
                                    int c5;
                                    int c6;
                                    if (c1.substring(1).equals("")) {
                                        c4 = 0;
                                    } else {
                                        c4 = Integer.parseInt(c1.substring(1));
                                    }
                                    if (c2.substring(1).equals("")) {
                                        c5 = 0;
                                    } else {
                                        c5 = Integer.parseInt(c1.substring(1));
                                    }
                                    if (c3.substring(1).equals("")) {
                                        c6 = 0;
                                    } else {
                                        c6 = Integer.parseInt(c1.substring(1));
                                    }

                                    Location loc = new Location(p.getProxy().getWorld(), p.getProxy().getLocation().getX() + c4, p.getProxy().getLocation().getY() + c5, p.getProxy().getLocation().getZ() + c6);
                                    pl.teleport(loc);
                                    Message.m(MType.G, p.getProxy(), "TP", "You teleported to " + ChatColor.AQUA + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
                                } else {
                                    Message.m(MType.W, p.getProxy(), "TP", "Something isn't a number...");
                                }
                            } else {
                                Message.earg2(p.getProxy(), "TP", "/tp <player> [x] [y] [z]");
                            }
                        }
                    } else {
                        Message.eplayer(p.getProxy(), "TP", args[0]);
                    }
                } else {
                    Message.e(p.getProxy(), "TP", Crit.P);
                }
            }
        } else {
            Message.e(cs, "TP", Crit.C);
        }
    }
}
