package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.particleeffect.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AdminModule extends TrilliumModule {

    public AdminModule() {
        super("ability");
    }

    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast <message>")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.BROADCAST)) {
            if (args.length == 0) {
                Message.earg(cs, "Broadcast", "Too few arguments. /broadcast <message>");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString().trim();

                List<String> format = getConfig().getStringList("broadcast");

                for (String s : format) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    s = s.replace("[msg]", message);
                    Bukkit.broadcastMessage(s);
                }
            }
        } else {
            Message.e(cs, "Broadcast", Crit.P);
        }
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
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Admin.SETSPAWN)) {

                p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
                Message.m(MType.G, p, "Set Spawn", "Spawn location set. " + ChatColor.AQUA + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ());

            } else {
                Message.e(p, "Set Spawn", Crit.P);
            }
        } else {
            Message.e(cs, "Set Spawn", Crit.C);
        }
    }
}

