package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.messageutils.Type;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class AdminModule extends TrilliumModule {

    public static ArrayList<String> reportlist = new ArrayList<>();

    public AdminModule() {
        super("ability");
    }

    @Command(command = "chestfinder", description = "Track down any hidden chests.", usage = "/chestfinder [radius]", aliases = "cf")
    public void chestfinder(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            final TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.CHESTFINDER)) {

                Location loc = p.getProxy().getLocation();
                int radius;
                if (args.length != 0) {
                    if (StringUtils.isNumeric(args[0])) {
                        radius = Integer.parseInt(args[0]);
                    } else {
                        Message.message(Type.WARNING, cs, "Chest Finder", true, args[0] + " is not a number. Setting radius to 50");
                        radius = 50;
                    }
                } else {
                    radius = 50;
                }
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
                        Message.message(Type.GOOD, p.getProxy(), "Chest Finder", true, b.getX() + ", " + b.getY() + ", " + b.getZ());
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
                                Vector v1 = p.getProxy().getLocation().clone().toVector();
                                Vector v2 = b.toVector();
                                Vector diff = v2.subtract(v1);

                                double dist = diff.length();
                                double dx = (diff.getX() / dist) * 0.5;
                                double dy = (diff.getY() / dist) * 0.5;
                                double dz = (diff.getZ() / dist) * 0.5;

                                Location loc = p.getProxy().getLocation().clone();

                                for (double d = 0; d <= dist; d += 0.5) {
                                    loc.add(dx, dy, dz);
                                    ParticleEffect.FLAME.display(0, 0, 0, 0, 1, loc, p.getProxy());
                                }
                            }
                        }
                    }.runTaskTimer(TrilliumAPI.getInstance(), 5, 5);

                } else {
                    Message.message(Type.WARNING, p.getProxy(), "Chest Finder", true, "No chests found.");
                }
            } else {
                Message.error("Chest Finder", cs);
            }
        } else {
            Message.error("Chest Finder", cs);
        }
    }

    @Command(command = "setspawn", description = "Set the spawn of the server.", usage = "/setspawn")
    public void setspawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.SETSPAWN)) {

                p.getProxy().getWorld().setSpawnLocation(p.getProxy().getLocation().getBlockX(), p.getProxy().getLocation().getBlockY(), p.getProxy().getLocation().getBlockZ());
                Message.message(Type.GOOD, p.getProxy(), "Set Spawn", true, "Spawn location set. " + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ", " + p.getProxy().getLocation().getBlockY() + ", " + p.getProxy().getLocation().getBlockZ());

            } else {
                Message.error("Set Spawn", cs);
            }
        } else {
            Message.error("Set Spawn", cs);
        }
    }

    @Command(command = "lag", description = "Statistics on server lag and also clears lag through gc.", usage = "/lag [clear]")
    public void lag(final CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.LAG)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("clear")) {

                    final long time = System.currentTimeMillis();

                    Message.message(Type.GENERIC, cs, "Lag", true, "Before GC:");
                    Utils.printCurrentMemory(cs);
                    cs.sendMessage(" ");

                    System.gc();
                    Message.message(Type.GOOD, cs, "Lag", true, "GC complete.");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cs.sendMessage(" ");
                            Message.message(Type.GENERIC, cs, "Lag", true, "After GC:");
                            Utils.printCurrentMemory(cs);

                            long need = System.currentTimeMillis() - time;
                            Message.message(Type.GENERIC, cs, "Lag", true, "GC took " + need / 1000L + " seconds.");

                        }
                    }.runTaskLater(TrilliumAPI.getInstance(), 5);
                }
            } else {

                Message.message(Type.GENERIC, cs, "Lag", true, "Server Statistics:");
                Utils.printCurrentMemory(cs);
            }
        } else {
            Message.error("Lag", cs);
        }
    }

    @Command(command = "killall", description = "Kill everything in a radius.", usage = "/killall <radius> <mobs/players/animals/monsters/items/everything>")
    public void killall(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.KILLALL)) {
                if (args.length <= 1) {
                    Message.error(p.getProxy(), "Killall", true, "/killall <radius> <mobs/players/animals/monsters/items/everything>");
                } else {
                    if (StringUtils.isNumeric(args[0])) {
                        List<Entity> entities = p.getProxy().getNearbyEntities(Double.parseDouble(args[0]), Double.parseDouble(args[0]), Double.parseDouble(args[0]));
                        if (args[1].equalsIgnoreCase("mobs")
                                || args[1].equalsIgnoreCase("animals")
                                || args[1].equalsIgnoreCase("players")
                                || args[1].equalsIgnoreCase("monsters")) {
                            Message.message(Type.GOOD, p.getProxy(), "Killall", true, "Successfully murdered all " + args[1] + " in a radius of " + args[0]);
                        } else if (args[1].equalsIgnoreCase("items")) {
                            Message.message(Type.GOOD, p.getProxy(), "Killall", true, "Successfully destroyed all items in a radius of " + args[0]);
                        } else if (args[1].equalsIgnoreCase("everything")) {
                            Message.message(Type.GOOD, p.getProxy(), "Killall", true, "Successfully destroyed and murdered everything... you monster...");
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
                                Message.message(Type.WARNING, p.getProxy(), "Killall", true, "Unknown argument: " + args[1]);
                            }
                        }
                    } else {
                        Message.message(Type.WARNING, p.getProxy(), "Killall", true, args[0] + " is not a number.");
                    }
                }
            } else {
                Message.error("Killall", cs);
            }
        } else {
            Message.error("Killall", cs);
        }
    }

    @Command(command = "inventory", description = "Open a crafting table GUI, or someone else's inventory or enderchest.", usage = "/inv <player [enderchest]/crafting>", aliases = "inv")
    public void inventory(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("crafting") || args[0].equalsIgnoreCase("cv")) {
                    if (p.hasPermission(Permission.Admin.INV_CRAFTING)) {
                        p.getProxy().openWorkbench(p.getProxy().getLocation(), true);
                        Message.message(Type.GOOD, p.getProxy(), "Inventory", true, "Now viewing a crafting table.");
                    } else {
                        Message.error("Inventory", cs);
                    }
                } else {
                    if (p.hasPermission(Permission.Admin.INV_PLAYER)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getInventory());
                            Message.message(Type.GOOD, p.getProxy(), "Inventory", true, "You are now viewing " + target.getProxy().getName() + "'s inventory");
                        } else {
                            Message.error("Inventory", cs, args[0]);
                        }
                    } else {
                        Message.error("Inventory", cs);
                    }
                }

            } else if (args.length > 1) {
                if (args[1].equalsIgnoreCase("enderchest") || args[1].equalsIgnoreCase("ec")) {
                    if (p.hasPermission(Permission.Admin.INV_ENDERCHEST)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getEnderChest());
                            Message.message(Type.GOOD, p.getProxy(), "Inventory", true, "Now viewing " + args[0] + "'s ender chest.");

                        } else {
                            Message.error("Inventory", cs, args[0]);
                        }
                    } else {
                        Message.error("Inventory", cs);
                    }
                } else {
                    Message.error(cs, "Inventory", false, "/inventory <player [enderchest]/crafting>");
                }
            } else {
                Message.error(cs, "Inventory", true, "/inventory <player [enderchest]/crafting>");
            }
        } else {
            Message.error("Inventory", cs);
        }
    }

    @Command(command = "report", description = "Send a report to the staff.", usage = "/report <msg>")
    public void report(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
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
                    Message.message(Type.GOOD, p.getProxy(), "Report", true, "Your report was submitted successfully.");
                    p.getProxy().sendMessage(ChatColor.YELLOW + "'" + ChatColor.GRAY + msg + ChatColor.YELLOW + "'");

                    for (TrilliumPlayer pl : TrilliumAPI.getOnlinePlayers()) {
                        if (pl.hasPermission(Permission.Admin.REPORT_RECEIVER) && !pl.getProxy().getName().equals(p.getProxy().getName())) {

                            Message.message(Type.WARNING, pl.getProxy(), "Report", true, "A new report was submitted by: " + p.getProxy().getName());
                            pl.getProxy().sendMessage(big);
                            Message.message(Type.GENERIC, pl.getProxy(), "Report", true, "/reports for a list of all reports.");
                        }
                    }
                } else {
                    Message.message(Type.WARNING, p.getProxy(), "Report", true, "What's your report? /report <msg>");
                }
            } else {
                Message.error("Report", cs);
            }
        } else {
            Message.error("Report", cs);
        }
    }

    @Command(command = "reports", description = "View the list of submitted reports. Clear reports & remove a report.", usage = "/reports [clear/remove <index>]")
    public void reports(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        reportlist.clear();
                        Message.message(Type.GOOD, p.getProxy(), "Reports", true, "Cleared Report List.");

                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length < 2) {
                            Message.error(p.getProxy(), "Reports", true, "/reports remove <index number>");
                        } else {
                            if (StringUtils.isNumeric(args[1])) {
                                int nb = Integer.parseInt(args[1]);
                                if (nb > 0 && nb <= reportlist.size() + 1) {
                                    Message.message(Type.GOOD, p.getProxy(), "Reports", true, "Removed: " + nb);
                                    p.getProxy().sendMessage(reportlist.get(nb - 1));
                                    reportlist.remove(nb - 1);

                                } else {
                                    Message.message(Type.WARNING, p.getProxy(), "Reports", true, args[1] + " is either larger than the list index or smaller than 0");
                                }
                            } else {
                                Message.message(Type.WARNING, p.getProxy(), "Reports", true, args[1] + " is not a number.");
                            }
                        }
                    } else {
                        Message.error(p.getProxy(), "Reports", true, "/reports [remove <index>/clear]");
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
                Message.error("Report", cs);
            }
        } else {
            Message.error("Report", cs);
        }
    }
}
