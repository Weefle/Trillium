package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AdminModule extends TrilliumModule {

    public static ArrayList<String> reportlist = new ArrayList<>();

    public AdminModule() {
        super("ability");
    }

    @Command(command = "chestfinder", description = "expose any hidden chests.", usage = "/chestfinder", aliases = "cf")
    public void chestfinder(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            final TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.CHESTFINDER)) {

                Location loc = p.getProxy().getLocation();
                int radius = 50;
                final ArrayList<Location> chests = new ArrayList<>();

                for (int X = loc.getBlockX() - radius; X <= loc.getBlockX() + radius; X++) {
                    for (int Y = loc.getBlockY() - radius; Y <= loc.getBlockY() + radius; Y++) {
                        for (int Z = loc.getBlockZ() - radius; Z <= loc.getBlockZ() + radius; Z++) {
                            Material block = loc.getWorld().getBlockAt(X, Y, Z).getType();

                            if (block == Material.CHEST || block == Material.TRAPPED_CHEST) {
                                chests.add(loc.getWorld().getBlockAt(X, Y, Z).getLocation());
                            }
                        }
                    }
                }

                if (!chests.isEmpty()) {
                    for (Location b : chests) {
                        Message.m(MType.G, p.getProxy(), "Chest Finder", b.getX() + ", " + b.getY() + ", " + b.getZ());
                    }

                    new BukkitRunnable() {
                        int count = 30;

                        public void run() {
                            if (count != 0) {
                                count--;
                            } else {
                                cancel();
                                chests.clear();
                            }
                            for (Location b : chests) {
                                ParticleEffect.DRIP_LAVA.display((float) 0.5, (float) 250, (float) 0.5, 0, 0, new Location(p.getProxy().getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5), p.getProxy());
                            }
                        }
                    }.runTaskTimer(TrilliumAPI.getInstance(), 30, 30);

                } else {
                    Message.m(MType.W, p.getProxy(), "Chest Finder", "No chests found.");
                }
            } else {
                Message.e(p.getProxy(), "Chest Finder", Crit.P);
            }
        } else {
            Message.e(cs, "Chest Finder", Crit.C);
        }
    }

    @Command(command = "setspawn", description = "Set the spawn of the server.", usage = "/setspawn")
    public void setspawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = (TrilliumPlayer) cs;
            if (p.hasPermission(Permission.Admin.SETSPAWN)) {

                p.getProxy().getWorld().setSpawnLocation(p.getProxy().getLocation().getBlockX(), p.getProxy().getLocation().getBlockY(), p.getProxy().getLocation().getBlockZ());
                Message.m(MType.G, p.getProxy(), "Set Spawn", "Spawn location set. " + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ", " + p.getProxy().getLocation().getBlockY() + ", " + p.getProxy().getLocation().getBlockZ());

            } else {
                Message.e(p.getProxy(), "Set Spawn", Crit.P);
            }
        } else {
            Message.e(cs, "Set Spawn", Crit.C);
        }
    }

    @Command(command = "lag", description = "Statistics on server lag and also clears lag through gc.", usage = "/lag")
    public void lag(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.LAG)) {

            long time = System.currentTimeMillis();

            Message.m(MType.R, cs, "Lag", "Before GC:");
            Utils.printCurrentMemory(cs);
            cs.sendMessage(" ");

            System.gc();
            Message.m(MType.G, cs, "Lag", "GC complete.");

            cs.sendMessage(" ");
            Message.m(MType.R, cs, "Lag", "After GC:");
            Utils.printCurrentMemory(cs);
            cs.sendMessage(" ");

            long need = System.currentTimeMillis() - time;
            Message.m(MType.R, cs, "Lag", "GC took " + need / 1000L + " seconds.");

        } else {
            Message.e(cs, "Lag", Crit.P);
        }
    }

    @Command(command = "killall", description = "Kill everything in a radius.", usage = "/killall <radius> <mobs/players/animals/monsters/items/everything>")
    public void killall(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = (TrilliumPlayer) cs;
            if (p.hasPermission(Permission.Admin.KILLALL)) {
                if (args.length <= 1) {
                    Message.earg(p.getProxy(), "Killall", "/killall <radius> <mobs/players/animals/monsters/items/everything>");
                } else {
                    if (Utils.isNumeric(args[0])) {
                        List<Entity> entities = p.getProxy().getNearbyEntities(Double.parseDouble(args[0]), Double.parseDouble(args[0]), Double.parseDouble(args[0]));
                        if (args[1].equalsIgnoreCase("mobs")
                                || args[1].equalsIgnoreCase("animals")
                                || args[1].equalsIgnoreCase("players")
                                || args[1].equalsIgnoreCase("monsters")) {
                            Message.m(MType.G, p.getProxy(), "Killall", "Successfully murdered all " + args[1] + " in a radius of " + args[0]);
                        } else if (args[1].equalsIgnoreCase("items")) {
                            Message.m(MType.G, p.getProxy(), "Killall", "Successfully destroyed all items in a radius of " + args[0]);
                        } else if (args[1].equalsIgnoreCase("everything")) {
                            Message.m(MType.G, p.getProxy(), "Killall", "Successfully destroyed and murdered everything... you monster...");
                        } else {
                            p.getProxy().sendMessage("");
                        }
                        for (Entity e : entities) {
                            if (args[1].equalsIgnoreCase("mobs")) {
                                if (e instanceof Monster || e instanceof Animals) {
                                    ((LivingEntity) e).setHealth(0D);
                                }
                            } else if (args[1].equalsIgnoreCase("monsters")) {
                                if (e instanceof Monster) {
                                    ((LivingEntity) e).setHealth(0D);
                                }
                            } else if (args[1].equalsIgnoreCase("animals")) {
                                if (e instanceof Animals) {
                                    ((LivingEntity) e).setHealth(0D);
                                }
                            } else if (args[1].equalsIgnoreCase("players")) {
                                if (e instanceof Player) {
                                    ((LivingEntity) e).setHealth(0D);
                                }
                            } else if (args[1].equalsIgnoreCase("items")) {
                                if (e instanceof Item) {
                                    e.remove();
                                }
                            } else if (args[1].equalsIgnoreCase("everything")) {
                                if (e instanceof Damageable) {
                                    ((Damageable) e).setHealth(0D);
                                } else {
                                    e.remove();
                                }
                            } else {
                                Message.m(MType.W, p.getProxy(), "Killall", "Unknown argument: " + args[1]);
                            }
                        }
                    } else {
                        Message.m(MType.W, p.getProxy(), "Killall", args[0] + " is not a number.");
                    }
                }
            } else {
                Message.e(p.getProxy(), "Killall", Crit.P);
            }
        } else {
            Message.e(cs, "Killall", Crit.C);
        }
    }

    @Command(command = "inventory", description = "Open a crafting table GUI, or someone else's inventory or enderchest.", usage = "/inv <player [enderchest]/crafting>", aliases = "inv")
    public void inventory(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = (TrilliumPlayer) cs;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("crafting") || args[0].equalsIgnoreCase("cv")) {
                    if (p.hasPermission(Permission.Admin.INV_CRAFTING)) {
                        p.getProxy().openWorkbench(p.getProxy().getLocation(), true);
                        Message.m(MType.G, p.getProxy(), "Inventory", "Now viewing a crafting table.");
                    } else {
                        Message.e(p.getProxy(), "Inventory", Crit.P);
                    }
                } else {
                    if (p.hasPermission(Permission.Admin.INV_PLAYER)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getInventory());
                            Message.m(MType.G, p.getProxy(), "Inventory", "You are now viewing " + target.getProxy().getName() + "'s inventory");
                        } else {
                            Message.eplayer(p.getProxy(), "Inventory", args[0]);
                        }
                    } else {
                        Message.e(p.getProxy(), "Inventory", Crit.P);
                    }
                }

            } else if (args.length > 1) {
                if (args[1].equalsIgnoreCase("enderchest") || args[1].equalsIgnoreCase("ec")) {
                    if (p.hasPermission(Permission.Admin.INV_ENDERCHEST)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getEnderChest());
                            Message.m(MType.G, p.getProxy(), "Inventory", "Now viewing " + args[0] + "'s ender chest.");

                        } else {
                            Message.eplayer(p.getProxy(), "Inventory", args[0]);
                        }
                    } else {
                        Message.e(p.getProxy(), "Inventory", Crit.P);
                    }
                } else {
                    Message.m(MType.W, p.getProxy(), "Inventory", "What is that? /inventory <player [enderchest]/crafting>");
                }
            } else {
                Message.earg(p.getProxy(), "Inventory", " /inventory <player [enderchest]/crafting>");
            }
        } else {
            Message.e(cs, "Inventory", Crit.C);
        }
    }

    @Command(command = "report", description = "Send a report to the staff.", usage = "/report <msg>")
    public void report(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = (TrilliumPlayer) cs;
            if (p.hasPermission(Permission.Admin.REPORT) || p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
                if (args.length != 0) {

                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    String msg = sb.toString().trim();

                    String big = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Reports" + ChatColor.DARK_GRAY + "]"
                            + ChatColor.BLUE + " {"
                            + ChatColor.AQUA + p.getProxy().getName() + ChatColor.BLUE + ", "
                            + ChatColor.AQUA + p.getProxy().getWorld().getName() + ChatColor.BLUE + ", "
                            + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ChatColor.BLUE + ", "
                            + ChatColor.AQUA + p.getProxy().getLocation().getBlockY() + ChatColor.BLUE + ", "
                            + ChatColor.AQUA + p.getProxy().getLocation().getBlockZ() + ChatColor.BLUE + "} >> "
                            + ChatColor.GRAY + msg;

                    reportlist.add(big);
                    Message.m(MType.G, p.getProxy(), "Report", "Your report was submitted successfully.");
                    p.getProxy().sendMessage(ChatColor.YELLOW + "'" + ChatColor.GRAY + msg + ChatColor.YELLOW + "'");

                    for (TrilliumPlayer pl : TrilliumAPI.getOnlinePlayers()) {
                        if (pl.hasPermission(Permission.Admin.REPORT_RECEIVER) && !pl.getProxy().getName().equals(p.getProxy().getName())) {

                            Message.m(MType.W, pl.getProxy(), "Report", "A new report was submitted by: " + p.getProxy().getName());
                            pl.getProxy().sendMessage(big);
                            Message.m(MType.R, pl.getProxy(), "Report", "/reports for a list of all reports.");
                        }
                    }
                } else {
                    Message.m(MType.W, p.getProxy(), "Report", "What's your report? /report <msg>");
                }
            } else {
                Message.e(p.getProxy(), "Report", Crit.P);
            }
        } else {
            Message.e(cs, "Report", Crit.C);
        }
    }

    @Command(command = "reports", description = "View the list of submitted reports. Clear reports & remove a report.", usage = "/reports [clear/remove <index>]")
    public void reports(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = (TrilliumPlayer) cs;
            if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        reportlist.clear();
                        Message.m(MType.G, p.getProxy(), "Reports", "Cleared Report List.");

                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length < 2) {
                            Message.earg(p.getProxy(), "Reports", "/reports remove <index number>");
                        } else {
                            if (Utils.isNumeric(args[1])) {
                                int nb = Integer.parseInt(args[1]);
                                if (nb > 0 && nb <= reportlist.size() + 1) {
                                    Message.m(MType.G, p.getProxy(), "Reports", "Removed: " + nb);
                                    p.getProxy().sendMessage(reportlist.get(nb - 1));
                                    reportlist.remove(nb - 1);

                                } else {
                                    Message.m(MType.W, p.getProxy(), "Reports", args[1] + " is either larger than the list index or smaller than 0");
                                }
                            } else {
                                Message.m(MType.W, p.getProxy(), "Reports", args[1] + " is not a number.");
                            }
                        }
                    } else {
                        Message.earg(p.getProxy(), "Reports", "/reports [remove <index>/clear]");
                    }

                } else {
                    p.getProxy().sendMessage(ChatColor.BLUE + "Report List:");
                    int nb = 0;
                    for (String big : reportlist) {
                        nb++;
                        p.getProxy().sendMessage(ChatColor.GRAY + "-" + ChatColor.AQUA + nb + ChatColor.GRAY + "- " + big);
                    }
                }
            } else {
                Message.e(p.getProxy(), "Report", Crit.P);
            }
        } else {
            Message.e(cs, "Report", Crit.C);
        }
    }
}
