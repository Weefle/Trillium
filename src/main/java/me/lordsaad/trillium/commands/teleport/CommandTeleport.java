package me.lordsaad.trillium.commands.teleport;

import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleport implements CommandExecutor {

    //TODO: save last location

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    if (p.hasPermission("tr.teleport")) {
                        Message.earg(p, "TP", "/tp <player> [player]");
                    }
                } else if (args.length == 1) {
                    if (p.hasPermission("tr.teleport")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {

                            p.teleport(target);
                            Message.m(MType.G, p, "TP", "You teleported to " + target.getName());

                        } else {
                            Message.eplayer(p, "TP", args[0]);
                        }
                    } else {
                        Message.e(p, "TP", Crit.P);
                    }

                } else if (args.length == 2) {
                    if (p.hasPermission("tr.teleport.other")) {
                        Player target1 = Bukkit.getPlayer(args[0]);
                        Player target2 = Bukkit.getPlayer(args[1]);
                        if (target1 != null) {
                            if (target2 != null) {


                                target1.teleport(target2);
                                Message.m(MType.G, p, "TP", "You teleported " + target1.getName() + " to " + target2.getName());
                                Message.m(MType.G, target1, "TP", p.getName() + " teleported you to " + target2.getName());

                            } else {
                                Message.eplayer(p, "TP", args[1]);
                            }
                        } else {
                            Message.eplayer(p, "TP", args[2]);
                        }
                    } else {
                        Message.e(p, "TP", Crit.P);
                    }

                } else {
                    if (p.hasPermission("tr.teleport.coord")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        String c1 = args[1];
                        String c2 = args[2];
                        String c3 = args[3];
                        if (pl != null) {
                            if (Utils.isNumeric(c1) && Utils.isNumeric(c2) && Utils.isNumeric(c3)) {
                                int c4 = Integer.parseInt(c1);
                                int c5 = Integer.parseInt(c2);
                                int c6 = Integer.parseInt(c3);
                                Location loc = new Location(p.getWorld(), c4, c5, c6);
                                pl.teleport(loc);
                                Message.m(MType.G, p, "TP", "You teleported to " + ChatColor.AQUA + c4 + ", " + c5 + ", " + c6);
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
                                        
                                        Location loc = new Location(p.getWorld(), p.getLocation().getX() + c4, p.getLocation().getY() + c5, p.getLocation().getZ() + c6);
                                        pl.teleport(loc);
                                        Message.m(MType.G, p, "TP", "You teleported to " + ChatColor.AQUA + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
                                    } else {
                                        Message.m(MType.W, p, "TP", "Something isn't a number...");
                                    }
                                } else {
                                    Message.earg2(p, "TP", "/tp <player> [x] [y] [z]");
                                }
                            }
                        } else {
                            Message.eplayer(p, "TP", args[0]);
                        }
                    } else {
                        Message.e(p, "TP", Crit.P);
                    }
                }
            } else {
                Message.e(sender, "TP", Crit.C);
            }
        }
        return true;
    }
}