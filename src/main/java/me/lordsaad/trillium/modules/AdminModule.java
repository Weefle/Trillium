package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.Utils;
import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.particleeffect.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AdminModule extends TrilliumModule {

    public AdminModule() {
        super("ability");
    }

    @Command(command = "chestfinder", description = "expose any hidden chests.", usage = "/chestfinder", aliases = "cf")
    public void chestfinder(CommandSender cs) {
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
    public void setspawn(CommandSender cs) {
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
    public void lag(CommandSender cs) {
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

    @Command(command = "nickname", description = "Change your nickname to anything you want.", usage = "/nick <nickname> [player]", aliases = "nick")
    public void nickname(CommandSender cs, String[] args) {
        
    }
}