package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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

    public static ArrayList<String> reportList = new ArrayList<>();
    public static ArrayList<String> lagPrompt = new ArrayList<>();
    public static ArrayList<String> reloadPrompt = new ArrayList<>();

    @Command(command = "trillium", description = "The main command of the plugin.", usage = "/tr", aliases = "tr")
    public void trillium(CommandSender cs, String[] args) {
        if (args.length == 0) {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "Plugin made with love by:");
            cs.sendMessage(ChatColor.GRAY + "LordSaad, VortexSeven, Turbotailz,");
            cs.sendMessage(ChatColor.GRAY + "samczsun, hintss, and colt");
            cs.sendMessage(ChatColor.DARK_RED + "<3");
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
            cs.sendMessage(ChatColor.GRAY + "Version: " + ChatColor.YELLOW + TrilliumAPI.getInstance().getDescription().getVersion());
            cs.sendMessage(ChatColor.GRAY + "Support email: " + ChatColor.YELLOW + "support@gettrillium.net");
            cs.sendMessage(ChatColor.GRAY + "Website: " + ChatColor.YELLOW + "http://www.gettrillium.net/");
            cs.sendMessage(ChatColor.GRAY + "Resource page: " + ChatColor.YELLOW + "http://www.spigotmc.org/resources/trillium.3882/");
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (cs.hasPermission(Permission.Admin.TRILLIUM)) {
                    if (reloadPrompt.contains(cs.getName())) {
                        reloadPrompt.remove(cs.getName());
                        Utils.reload();
                        cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
                        cs.sendMessage(ChatColor.GRAY + "Plugin successfully reloaded");
                        cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
                    } else {
                        reloadPrompt.add(cs.getName());
                        new Message(Mood.BAD, "Trillium", "Wow there tiger!").to(cs);
                        new Message(Mood.BAD, "Trillium", "Running the reload command isn't a walk in the park!").to(cs);
                        new Message(Mood.BAD, "Trillium", "It's not advised to run this command frequently as it unloads and reloads a lot of things.").to(cs);
                        new Message(Mood.BAD, "Trillium", "Are you SURE you want to continue?").to(cs);
                        new Message(Mood.BAD, "Trillium", "If yes, then run this command again.").to(cs);
                    }
                } else {
                    new Message("Trillium", Error.NO_PERMISSION).to(cs);
                }
            }
        }
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
                        new Message(Mood.BAD, "Chest Finder", args[0] + " is not a number. Setting radius to 50.").to(p);
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
                        new Message(Mood.GOOD, "Chest Finder", b.getX() + ", " + b.getY() + ", " + b.getZ()).to(p);
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
                    new Message(Mood.BAD, "Chest Finder", "No chests found.").to(p);
                }
            } else {
                new Message("Chest Finder", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Chest Finder", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "setspawn", description = "Set the spawn of the server.", usage = "/setspawn")
    public void setspawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.SETSPAWN)) {

                p.getProxy().getWorld().setSpawnLocation(p.getProxy().getLocation().getBlockX(), p.getProxy().getLocation().getBlockY(), p.getProxy().getLocation().getBlockZ());
                new Message(Mood.GOOD, "Set Spawn", "Spawn location set. " + ChatColor.AQUA + p.getProxy().getLocation().getBlockX() + ", " + p.getProxy().getLocation().getBlockY() + ", " + p.getProxy().getLocation().getBlockZ()).to(p);

            } else {
                new Message("Set Spawn", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Set Spawn", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "clearinventory", description = "Clear your own or someone else's inventory.", usage = "/clearinventory", aliases = {"clear", "clearinv", "ci"})
    public void clearinv(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Admin.CLEARINV)) {
                if (args.length == 0) {
                    Utils.clearInventory(p);
                    new Message(Mood.GOOD, "Clear Inv", "Inventory cleared.").to(p);
                } else {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {
                        Utils.clearInventory(target);
                        new Message(Mood.GOOD, "Clear Inv", "You have cleared " + target.getName() + "'s inventory.").to(p);
                    } else {
                        new Message("Clear Inv", Error.INVALID_PLAYER, args[0]).to(p);
                    }
                }
            } else {
                new Message("Clear Inv", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Clear Inv", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "lag", description = "Statistics on server lag and also clears lag through gc.", usage = "/lag [clear]")
    public void lag(final CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.LAG)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("clear")) {
                    if (lagPrompt.contains(cs.getName())) {
                        lagPrompt.remove(cs.getName());

                        final long time = System.currentTimeMillis();

                        new Message(Mood.NEUTRAL, "Lag", "Before GC:").to(cs);
                        Utils.printCurrentMemory(cs);
                        cs.sendMessage(" ");

                        System.gc();
                        new Message(Mood.GOOD, "Lag", "GC complete.").to(cs);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cs.sendMessage(" ");
                                new Message(Mood.NEUTRAL, "Lag", "After GC:").to(cs);
                                Utils.printCurrentMemory(cs);

                                long need = System.currentTimeMillis() - time;
                                new Message(Mood.NEUTRAL, "Lag", "GC took " + need / 1000L + " seconds.").to(cs);

                            }
                        }.runTaskLater(TrilliumAPI.getInstance(), 5);

                    } else {
                        lagPrompt.add(cs.getName());
                        new Message(Mood.BAD, "Lag", "Wow there tiger!").to(cs);
                        new Message(Mood.BAD, "Lag", "Running the lag clearing command can potentially do more harm").to(cs);
                        new Message(Mood.BAD, "Lag", "than good if something goes wrong during the process!").to(cs);
                        new Message(Mood.BAD, "Lag", "Are you SURE you want to continue?").to(cs);
                        new Message(Mood.BAD, "Lag", "If yes, then run this command again.").to(cs);

                    }
                }
            } else {
                new Message(Mood.NEUTRAL, "Lag", "Server Statistics:").to(cs);
                Utils.printCurrentMemory(cs);
            }
        } else {
            new Message("Lag", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "killall", description = "Kill everything in a radius.", usage = "/killall <radius> <mobs/players/animals/monsters/items/everything>")
    public void killall(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.KILLALL)) {
                if (args.length <= 1) {
                    new Message("Killall", Error.TOO_FEW_ARGUMENTS, "/killall <radius> <mobs/players/animals/monsters/items/everything>").to(p);
                } else {
                    if (StringUtils.isNumeric(args[0])) {
                        List<Entity> entities = p.getProxy().getNearbyEntities(Double.parseDouble(args[0]), Double.parseDouble(args[0]), Double.parseDouble(args[0]));

                        if (args[1].equalsIgnoreCase("mobs")
                                || args[1].equalsIgnoreCase("animals")
                                || args[1].equalsIgnoreCase("players")
                                || args[1].equalsIgnoreCase("monsters")) {
                            new Message(Mood.GOOD, "Killall", "Successfully murdered all " + args[1] + " in a radius of " + args[0]).to(p);
                        } else if (args[1].equalsIgnoreCase("items")) {
                            new Message(Mood.GOOD, "Killall", "Successfully destroyed all items in a radius of " + args[0]).to(p);
                        } else if (args[1].equalsIgnoreCase("everything")) {
                            new Message(Mood.GOOD, "Killall", "Successfully destroyed and murdered everything... you monster...").to(p);
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
                                new Message("Killall", Error.WRONG_ARGUMENTS, "/killall <radius> <mobs/players/animals/monsters/items/everything>").to(p);
                            }
                        }
                    } else {
                        new Message(Mood.BAD, "Killall", args[0] + " is not a number.").to(p);
                    }
                }
            } else {
                new Message("Killall", Error.NO_PERMISSION);
            }
        } else {
            new Message("Killall", Error.CONSOLE_NOT_ALLOWED);
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
                        new Message(Mood.GOOD, "Inventory", "Now viewing a crafting table.").to(p);
                    } else {
                        new Message("Inventory", Error.NO_PERMISSION).to(p);
                    }
                } else {
                    if (p.hasPermission(Permission.Admin.INV_PLAYER)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getInventory());
                            new Message(Mood.GOOD, "Inventory", "You are now viewing " + target.getName() + "'s inventory.").to(p);
                        } else {
                            new Message("Inventory", Error.INVALID_PLAYER, args[0]).to(p);
                        }
                    } else {
                        new Message("Inventory", Error.NO_PERMISSION).to(p);
                    }
                }

            } else if (args.length > 1) {
                if (args[1].equalsIgnoreCase("enderchest") || args[1].equalsIgnoreCase("ec")) {
                    if (p.hasPermission(Permission.Admin.INV_ENDERCHEST)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getEnderChest());
                            new Message(Mood.GOOD, "Inventory", "Now viewing " + args[0] + "'s ender chest.").to(p);

                        } else {
                            new Message("Inventory", Error.INVALID_PLAYER, args[0]).to(p);
                        }
                    } else {
                        new Message("Inventory", Error.NO_PERMISSION).to(p);
                    }
                } else {
                    new Message("Inventory", Error.WRONG_ARGUMENTS, "/inventory <player [enderchest]/crafting>").to(p);
                }
            } else {
                new Message("Inventory", Error.TOO_FEW_ARGUMENTS, "/inventory <player [enderchest]/crafting>").to(p);
            }
        } else {
            new Message("Inventory", Error.CONSOLE_NOT_ALLOWED).to(cs);
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

                    reportList.add(big);
                    new Message(Mood.GOOD, "Report", "Your report was submitted successfully.").to(p);
                    p.getProxy().sendMessage(ChatColor.YELLOW + "'" + ChatColor.GRAY + msg + ChatColor.YELLOW + "'");

                    for (TrilliumPlayer pl : TrilliumAPI.getOnlinePlayers()) {
                        if (pl.hasPermission(Permission.Admin.REPORT_RECEIVER) && !pl.getProxy().getName().equals(p.getProxy().getName())) {

                            new Message(Mood.GOOD, "Report", "A new report was submitted by: " + p.getName()).to(pl);
                            pl.getProxy().sendMessage(big);
                            new Message(Mood.NEUTRAL, "Report", "/reports for a list of all reports.").to(pl);
                        }
                    }
                } else {
                    new Message("Report", Error.TOO_FEW_ARGUMENTS, "/report <msg>").to(p);
                }
            } else {
                new Message("Report", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Report", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "reports", description = "View the list of submitted reports. Clear reports & remove a report.", usage = "/reports [clear/remove <index>]")
    public void reports(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        reportList.clear();
                        new Message(Mood.GOOD, "Reports", "Cleared report list.").to(p);

                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length < 2) {
                            new Message("Reports", Error.TOO_FEW_ARGUMENTS, "/reports remove <index number>").to(p);
                        } else {
                            if (StringUtils.isNumeric(args[1])) {
                                int nb = Integer.parseInt(args[1]);
                                if (nb > 0 && nb <= reportList.size() + 1) {
                                    new Message(Mood.GOOD, "Reports", "Removed: " + nb).to(p);
                                    p.getProxy().sendMessage(reportList.get(nb - 1));
                                    reportList.remove(nb - 1);

                                } else {
                                    new Message(Mood.BAD, "Reports", args[1] + " is either larger than the list index or smaller than 0.").to(p);
                                }
                            } else {
                                new Message(Mood.BAD, "Reports", args[1] + " is not a number.").to(p);
                            }
                        }
                    } else {
                        new Message("Reports", Error.TOO_FEW_ARGUMENTS, "/reports [remove <index>/clear]").to(p);
                    }

                } else {
                    p.getProxy().sendMessage(ChatColor.BLUE + "Report List:");
                    int nb = 0;
                    for (String big : reportList) {
                        nb++;
                        p.getProxy().sendMessage(ChatColor.GRAY + "-" + ChatColor.AQUA + nb + ChatColor.GRAY + "- " + big);
                    }
                }
            } else {
                new Message("Report", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Report", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }
}
