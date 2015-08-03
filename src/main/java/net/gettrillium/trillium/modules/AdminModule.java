package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.report.Reports;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class AdminModule extends TrilliumModule {

    private ArrayList<String> lagPrompt = new ArrayList<>();
    private ArrayList<String> reloadPrompt = new ArrayList<>();

    @Command(name = "Trillium",
            command = "trillium",
            description = "The main command of the plugin.",
            usage = "/tr",
            aliases = "tr",
            permissions = {Permission.Admin.TRILLIUM})
    public void trillium(CommandSender cs, String[] args) {
        String cmd = "trillium";
        if (args.length == 0) {
            cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
            cs.sendMessage(ChatColor.GRAY + "Plugin made with love by:");
            cs.sendMessage(ChatColor.GRAY + "LordSaad, VortexSeven, Turbotailz,");
            cs.sendMessage(ChatColor.GRAY + "samczsun");
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
                        Utils.unload();
                        Utils.load();
                        cs.sendMessage(ChatColor.DARK_GRAY + "<<<---{[O]}--->>> " + ChatColor.BLUE + "Trillium" + ChatColor.DARK_GRAY + " <<<---{[O]}--->>>");
                        cs.sendMessage(ChatColor.GRAY + "Plugin successfully reloaded");
                        cs.sendMessage(ChatColor.DARK_GRAY + "<<<-------------------------------->>>");
                    } else {
                        reloadPrompt.add(cs.getName());
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Wow there tiger!").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Running the reload command isn't a walk in the park!").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "It's not advised to run this command frequently as it unloads and reloads a lot of things.").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Are you SURE you want to continue?").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "If yes, then run this command again.").to(cs);
                    }
                } else {
                    new Message("Trillium", Error.NO_PERMISSION).to(cs);
                }
            }
        }
    }

    @Command(name = "Chest Finder",
            command = "chestfinder",
            description = "Track down any hidden chests.",
            usage = "/chestfinder [radius]",
            aliases = "cf",
            permissions = {Permission.Admin.CHESTFINDER})
    public void chestfinder(CommandSender cs, String[] args) {
        String cmd = "chestfinder";
        if (cs instanceof Player) {
            final TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.CHESTFINDER)) {

                Location loc = p.getProxy().getLocation();
                int radius;
                if (args.length != 0) {
                    if (StringUtils.isNumeric(args[0])) {
                        if (Integer.parseInt(args[0]) <= 100) {
                            radius = Integer.parseInt(args[0]);
                        } else {
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[0] + " is too big and will crash your server. Setting radius to 50.").to(p);
                            radius = Integer.parseInt(args[0]);
                        }
                    } else {
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[0] + " is not a number. Setting radius to 50.").to(p);
                        radius = 50;
                    }
                } else {
                    radius = 50;
                }

                final ArrayList<Location> chests = new ArrayList<>();

                World w = loc.getWorld();
                int radiusSquared = radius * radius;

                int chunkRadius = radius / 16 + 1;
                Chunk centerChunk = loc.getWorld().getChunkAt(loc);
                int centerX = centerChunk.getX();
                int centerZ = centerChunk.getZ();

                for (int x = centerX - chunkRadius; x < centerX + chunkRadius; x++) {
                    for (int z = centerZ - chunkRadius; z < centerZ + chunkRadius; z++) {
                        Chunk c = w.getChunkAt(x, z);
                        c.load();

                        for (BlockState bs : c.getTileEntities()) {
                            Material type = bs.getType();
                            if (type == Material.CHEST || type == Material.TRAPPED_CHEST) {
                                Location chestLoc = bs.getLocation();

                                if (loc.distanceSquared(chestLoc) < radiusSquared) {
                                    chests.add(bs.getLocation());
                                }
                            }
                        }
                    }
                }

                if (!chests.isEmpty()) {
                    for (Location block : chests) {
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), Utils.locationToString(block)).to(p);
                    }

                    // create an array with the blocks in vector form so we don't need to do as much object creation in the loop
                    final List<Vector> blockVectors = new ArrayList<>();

                    for (Location chest : chests) {
                        blockVectors.add(chest.toVector());
                    }

                    new BukkitRunnable() {
                        int count = 30;

                        public void run() {
                            if (count == 0) {
                                cancel();
                            }

                            count--;

                            Location loc = p.getProxy().getLocation();
                            Vector playerVector = loc.toVector();

                            for (Vector v : blockVectors) {
                                v.subtract(playerVector);
                                double dist = v.length();

                                double dx = (v.getX() / dist) / 2;
                                double dy = (v.getY() / dist) / 2;
                                double dz = (v.getZ() / dist) / 2;

                                // re-add the playerVector to this vector so we can reuse this vector next run
                                v.add(playerVector);

                                for (double d = 0; d <= dist; d += 0.5) {
                                    loc.add(dx * d, dy * d, dz * d);
                                    ParticleEffect.FLAME.display(0, 0, 0, 0, 1, loc, p.getProxy());
                                    loc.subtract(dx, dy, dz);
                                }
                            }
                        }
                    }.runTaskTimer(TrilliumAPI.getInstance(), 5, 5);
                } else {
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No chests found.").to(p);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Set Spawn",
            command = "setspawn",
            description = "Set the spawn of the server.",
            usage = "/setspawn",
            permissions = {Permission.Admin.SETSPAWN})
    public void setspawn(CommandSender cs, String[] args) {
        String cmd = "setspawn";
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Admin.SETSPAWN)) {

                p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Spawn location set. " + ChatColor.AQUA + Utils.locationToString(p.getLocation())).to(p);

            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Clear Inv",
            command = "clearinventory",
            description = "Clear your own or someone else's inventory.",
            usage = "/clearinventory",
            aliases = {"clear", "clearinv", "ci"},
            permissions = {Permission.Admin.CLEARINV})
    public void clearinv(CommandSender cs, String[] args) {
        String cmd = "clearinventory";
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Admin.CLEARINV)) {
                if (args.length == 0) {
                    Utils.clearInventory(p);
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Inventory cleared.").to(p);
                } else {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {
                        Utils.clearInventory(target);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You have cleared " + target.getName() + "'s inventory.").to(p);
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(p);
                    }
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Lag",
            command = "lag",
            description = "Statistics on server lag and also clears lag through gc.",
            usage = "/lag [clear]",
            permissions = {Permission.Admin.LAG})
    public void lag(final CommandSender cs, String[] args) {
        final String cmd = "lag";
        if (cs.hasPermission(Permission.Admin.LAG)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("clear")) {
                    if (lagPrompt.contains(cs.getName())) {
                        lagPrompt.remove(cs.getName());

                        final long time = System.currentTimeMillis();

                        new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "Before GC:").to(cs);
                        Utils.printCurrentMemory(cs);
                        cs.sendMessage(" ");

                        System.gc();
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "GC complete.").to(cs);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cs.sendMessage(" ");
                                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "After GC:").to(cs);
                                Utils.printCurrentMemory(cs);

                                long need = System.currentTimeMillis() - time;
                                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "GC took " + need / 1000L + " seconds.").to(cs);

                            }
                        }.runTaskLater(TrilliumAPI.getInstance(), 5);

                    } else {
                        lagPrompt.add(cs.getName());
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Wow there tiger!").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Running the lag clearing command can potentially do more harm").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "than good if something goes wrong during the process!").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Are you SURE you want to continue?").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "If yes, then run this command again.").to(cs);

                    }
                }
            } else {
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "Server Statistics:").to(cs);
                Utils.printCurrentMemory(cs);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }

    @Command(name = "Kill All",
            command = "killall",
            description = "Kill everything in a radius.",
            usage = "/killall <mobs/players/animals/monsters/items/everything/<entity name>> [radius]",
            permissions = {Permission.Admin.KILLALL})
    public void killall(CommandSender cs, String[] args) {
        String cmd = "killall";
        if (!(cs instanceof Player)) {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Admin.KILLALL)) {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]);
            return;
        }

        if (args.length < 1) {
            new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
            return;
        }

        int radius = 50;
        if (args.length > 1) {
            if (StringUtils.isNumeric(args[1])) {
                radius = Integer.parseInt(args[1]);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Radius is not a number. Setting to 50");
            }
        }

        List<Entity> entities = p.getProxy().getNearbyEntities(radius, radius, radius);
        int i = 0;

        if (args[0].equalsIgnoreCase("mobs")) {
            for (Entity e : entities) {
                if (e instanceof Monster || e instanceof Animals) {
                    i++;
                    ((LivingEntity) e).setHealth(0D);
                }
            }

            if (i != 0) {
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Successfully murdered " + i + " mobs in a radius of " + radius).to(p);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No mobs found in a radius of " + radius).to(p);
            }

        } else if (args[0].equalsIgnoreCase("monsters")) {
            for (Entity e : entities) {
                if (e instanceof Monster) {
                    i++;
                    ((LivingEntity) e).setHealth(0D);
                }
            }

            if (i != 0) {
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Successfully murdered " + i + " monsters in a radius of " + radius).to(p);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No monsters found in a radius of " + radius).to(p);
            }

        } else if (args[0].equalsIgnoreCase("animals")) {
            for (Entity e : entities) {
                if (e instanceof Animals) {
                    i++;
                    ((LivingEntity) e).setHealth(0D);
                }
            }

            if (i != 0) {
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Successfully murdered " + i + " animals in a radius of " + radius).to(p);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No animals found in a radius of " + radius).to(p);
            }

        } else if (args[0].equalsIgnoreCase("items")) {
            for (Entity e : entities) {
                if (e instanceof Item) {
                    i++;
                    e.remove();
                }
            }

            if (i != 0) {
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Successfully destroyed " + i + " items in a radius of " + radius).to(p);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No items found in a radius of " + radius).to(p);
            }

        } else if (args[0].equalsIgnoreCase("players")) {
            for (Entity e : entities) {
                if (e instanceof Player) {
                    i++;
                    ((LivingEntity) e).setHealth(0D);
                }
            }

            if (i != 0) {
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Successfully murdered " + i + " players in a radius of " + radius).to(p);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No players found in a radius of " + radius).to(p);
            }

        } else if (EntityType.valueOf(args[0]) != null) {
            for (Entity e : entities) {
                if (e.getType() == EntityType.valueOf(args[1])) {
                    i++;
                    if (e instanceof LivingEntity) {
                        ((LivingEntity) e).setHealth(0D);
                    } else {
                        e.remove();
                    }
                }
            }

            if (i != 0) {
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Successfully murdered/destroyed " + i + "'" + args[0] + "' in a radius of " + radius).to(p);
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "No '" + args[0] + "' found in a radius of " + radius).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
        }
    }

    @Command(name = "Inventory",
            command = "inventory",
            description = "Open a crafting table GUI, or someone else's inventory or enderchest.",
            usage = "/inv <player [enderchest]/crafting>",
            aliases = "inv",
            permissions = {Permission.Admin.INV_CRAFTING, Permission.Admin.INV_PLAYER, Permission.Admin.INV_ENDERCHEST})
    public void inventory(CommandSender cs, String[] args) {
        String cmd = "inventory";
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("crafting") || args[0].equalsIgnoreCase("cv")) {
                    if (p.hasPermission(Permission.Admin.INV_CRAFTING)) {
                        p.getProxy().openWorkbench(p.getProxy().getLocation(), true);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Now viewing a crafting table.").to(p);
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
                    }
                } else {
                    if (p.hasPermission(Permission.Admin.INV_PLAYER)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getInventory());
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You are now viewing " + target.getName() + "'s inventory.").to(p);
                        } else {
                            new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(p);
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[1]).to(p);
                    }
                }

            } else if (args.length > 1) {
                if (args[1].equalsIgnoreCase("enderchest") || args[1].equalsIgnoreCase("ec")) {
                    if (p.hasPermission(Permission.Admin.INV_ENDERCHEST)) {
                        TrilliumPlayer target = player(args[0]);

                        if (target != null) {
                            p.getProxy().openInventory(target.getProxy().getEnderChest());
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Now viewing " + args[0] + "'s ender chest.").to(p);

                        } else {
                            new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(p);
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[3]).to(p);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Report",
            command = "report",
            description = "Send a report to the staff.",
            usage = "/report <msg>",
            permissions = {Permission.Admin.REPORT, Permission.Admin.REPORT_RECEIVER})
    public void report(CommandSender cs, String[] args) {
        String cmd = "report";
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Admin.REPORT) || p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
                if (args.length != 0) {

                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    String msg = sb.toString().trim();

                    Reports.addReport(p, msg, p.getLocation());
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Your report was submitted successfully.").to(p);
                    p.sendMessage(ChatColor.YELLOW + "'" + ChatColor.GRAY + msg + ChatColor.YELLOW + "'");

                    for (TrilliumPlayer pl : TrilliumAPI.getOnlinePlayers()) {
                        if (pl.hasPermission(Permission.Admin.REPORT_RECEIVER) && !pl.getProxy().getName().equals(p.getName())) {

                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "A new report was submitted: ").to(pl);
                            new Message(Mood.NEUTRAL, "Player", p.getName()).to(pl);
                            new Message(Mood.NEUTRAL, "Location", Utils.locationToString(p.getLocation())).to(pl);
                            new Message(Mood.NEUTRAL, "Message", msg).to(pl);
                        }
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, "/report <msg>").to(p);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Reports",
            command = "reports",
            description = "View the list of submitted reports. Clear reports & remove a report.",
            usage = "/reports [clear/remove <index>]",
            permissions = {Permission.Admin.REPORT_RECEIVER})
    public void reports(CommandSender cs, String[] args) {
        String cmd = "reports";
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.REPORT_RECEIVER)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        Reports.clearReports();
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Cleared report list.").to(p);

                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length < 2) {
                            new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                        } else {
                            if (StringUtils.isNumeric(args[1])) {
                                int nb = Integer.parseInt(args[1]);
                                if (nb > 0 && nb <= Reports.getReportMessages().size()) {

                                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Removed: " + nb).to(p);
                                    Reports.getReport(nb).to(p);
                                    Reports.removeReport(nb);

                                } else {
                                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[1] + " is either larger than the list index or smaller than 0.").to(p);
                                }
                            } else {
                                new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[1] + " is not a number.").to(p);
                            }
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                    }

                } else {
                    p.getProxy().sendMessage(ChatColor.BLUE + "Reports:");
                    for (Message msg : Reports.getReportMessages()) {
                        msg.to(p);
                    }
                }
            } else {
                new Message("Report", Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message("Report", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }
}
