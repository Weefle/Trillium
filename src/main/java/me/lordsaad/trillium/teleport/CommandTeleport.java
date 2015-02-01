package me.lordsaad.trillium.teleport;

import me.lordsaad.trillium.PlayerDatabase;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandTeleport implements CommandExecutor {

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

                            String world = p.getLocation().getWorld().getName();
                            int x = p.getLocation().getBlockX();
                            int y = p.getLocation().getBlockY();
                            int z = p.getLocation().getBlockZ();

                            YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
                            yml.set("Previous Location.world", world);
                            yml.set("Previous Location.x", x);
                            yml.set("Previous Location.y", y);
                            yml.set("Previous Location.z", z);

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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

                                String world = target1.getLocation().getWorld().getName();
                                int x = target1.getLocation().getBlockX();
                                int y = target1.getLocation().getBlockY();
                                int z = target1.getLocation().getBlockZ();

                                YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(target1));

                                yml.set("Previous Location.world", world);
                                yml.set("Previous Location.x", x);
                                yml.set("Previous Location.y", y);
                                yml.set("Previous Location.z", z);

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
                        if (Utils.isdouble(c1) && Utils.isdouble(c2) && Utils.isdouble(c3)
                                || Utils.isint(c1) && Utils.isint(c2) && Utils.isint(c3)) {
                            int c4 = Integer.parseInt(c1);
                            int c5 = Integer.parseInt(c2);
                            int c6 = Integer.parseInt(c3);
                            Location loc = new Location(p.getWorld(), c4, c5, c6);
                            pl.teleport(loc);
                            Message.m(MType.G, p, "TP", "You teleported to " + ChatColor.AQUA + c4 + ", " + c5 + ", " + c6);
                        } else {
                            if (c1.contains("~") && c2.contains("~") && c3.contains("~")) {
                                int c4 = Integer.parseInt(c1.split("~")[1]);
                                int c5 = Integer.parseInt(c2.split("~")[1]);
                                int c6 = Integer.parseInt(c3.split("~")[1]);
                                Location loc = new Location(p.getWorld(), p.getLocation().getX() + c4, p.getLocation().getY() + c5, p.getLocation().getZ() + c6);
                                pl.teleport(loc);
                                Message.m(MType.G, p, "TP", "You teleported to " + ChatColor.AQUA + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());

                            }
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