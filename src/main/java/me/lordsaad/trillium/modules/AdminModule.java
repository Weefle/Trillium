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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

    @Command(command = "chestfinder", description = "expose any hidden chests.", usage = "/chestfinder")
    public void chestfinder(CommandSender cs) {
        if (cs instanceof Player) {
            final TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.CHESTFINDER)) {

                Location loc = p.getProxy().getLocation();
                int radius = 50;
                final ArrayList<Block> chests = new ArrayList<>();

                for (int X = loc.getBlockX() - radius; X <= loc.getBlockX() + radius; X++) {
                    for (int Y = loc.getBlockY() - radius; Y <= loc.getBlockY() + radius; Y++) {
                        for (int Z = loc.getBlockZ() - radius; Z <= loc.getBlockZ() + radius; Z++) {
                            Block block = loc.getWorld().getBlockAt(X, Y, Z);

                            if (block != null) {
                                if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                                    chests.add(block);
                                }
                            }
                        }
                    }
                }

                if (!chests.isEmpty()) {
                    for (Block b : chests) {
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
                            for (Block b : chests) {
                                ParticleEffect.DRIP_LAVA.display((float) 0.5, (float) 250, (float) 0.5, 0, 0, new Location(p.getProxy().getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5), 100);
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
}
