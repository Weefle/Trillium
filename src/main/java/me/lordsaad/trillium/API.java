package me.lordsaad.trillium;

import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.runnables.TpsRunnable;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class API {

    public static boolean isdouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isint(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isonline(Player p) {
        return p.isOnline() && !CommandVanish.vanishedusers.contains(p.getUniqueId());
    }

    public static String locationstring(Player p) {
        if (p.isOnline()) {
            Location loc = p.getLocation();
            return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + p.getWorld().getName();
        } else {
            return "null";
        }
    }

    public static int getfoodlevel(Player p) {
        if (p.isOnline()) {
            return p.getFoodLevel();
        } else {
            return 0;
        }
    }

    public static double gethealthlevel(Player p) {
        if (p.isOnline()) {
            return p.getHealthScale();
        } else {
            return 0;
        }
    }

    public static String getgamemode(Player p) {
        if (p.isOnline()) {
            if (p.getGameMode() == GameMode.SURVIVAL) {
                return "Survival mode";
            } else if (p.getGameMode() == GameMode.CREATIVE) {
                return "Creative mode";
            } else if (p.getGameMode() == GameMode.ADVENTURE) {
                return "Adventure mode";
            } else {
                return "Spectator mode";
            }
        } else {
            return "null";
        }
    }

    public static int gettimeplayed(Player p) {
        return p.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
    }

    public static String lastlocationstring(Player p) {
        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        int x = pdb.getInt("Last Location.x");
        int y = pdb.getInt("Last Location.y");
        int z = pdb.getInt("Last Location.z");
        String world = pdb.getString("Last Location.world");
        return world + ", " + x + ", " + y + ", " + z;
    }

    public static boolean ismuted(Player p) {
        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        if (pdb.getBoolean("Muted")) {
            return true;
        } else {
            return true;
        }
    }

    public static boolean isflying(Player p) {
        if (p.isOnline()) {
            if (p.isFlying()) {
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isgodmode(Player p) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        return CommandGodMode.godmodeusers.contains(p.getUniqueId()) || yml.getBoolean("God Mode");
    }

    public static void setgodmode(boolean b, Player p) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        if (b) {
            if (!CommandGodMode.godmodeusers.contains(p.getUniqueId())) {
                yml.set("God Mode", true);
                CommandGodMode.godmodeusers.add(p.getUniqueId());
                try {
                    yml.save(PlayerDatabase.db(p));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (CommandGodMode.godmodeusers.contains(p.getUniqueId())) {
                yml.set("God Mode", false);
                CommandGodMode.godmodeusers.remove(p.getUniqueId());
                try {
                    yml.save(PlayerDatabase.db(p));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void broadcast(String message) {

        Boolean space = Main.plugin.getConfig().getBoolean("Broadcast.line clearing");
        Boolean btwn = Main.plugin.getConfig().getBoolean("Broadcast.line clearing between header and footer");
        Boolean headerboolean = Main.plugin.getConfig().getBoolean("Broadcast.header.enabled");
        String header = Main.plugin.getConfig().getString("Broadcast.header.set");
        Boolean footerboolean = Main.plugin.getConfig().getBoolean("Broadcast.footer.enabled");
        String footer = Main.plugin.getConfig().getString("Broadcast.footer.set");
        Boolean prefixboolean = Main.plugin.getConfig().getBoolean("Broadcast.prefix.enabled");
        String prefix = Main.plugin.getConfig().getString("Broadcast.prefix.set");

        if (space) {
            Bukkit.broadcastMessage(" ");
        }
        if (headerboolean) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', header));
        }
        if (btwn) {
            Bukkit.broadcastMessage(" ");
        }

        if (prefixboolean) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " " + ChatColor.BLUE + message);
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        if (btwn) {
            Bukkit.broadcastMessage(" ");
        }
        if (footerboolean) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', footer));
        }
        if (space) {
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(" ");
        }
    }

    public static boolean isvanished(Player p) {
        return CommandVanish.vanishedusers.contains(p.getUniqueId());
    }

    public static void setvanished(boolean b, Player p) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        if (b) {
            if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                yml.set("Vanish Mode", true);
                CommandVanish.vanishedusers.add(p.getUniqueId());

                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.hidePlayer(p);
                }

                try {
                    yml.save(PlayerDatabase.db(p));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                yml.set("Vanish Mode", false);
                CommandVanish.vanishedusers.remove(p.getUniqueId());

                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.showPlayer(p);
                }

                try {
                    yml.save(PlayerDatabase.db(p));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isafk(Player p) {
        return CommandAfk.afklist.contains(p.getUniqueId());
    }

    public static void printcurrentmemory(CommandSender sender) {
        int free = (int) Runtime.getRuntime().freeMemory() / 1000000;
        int max = (int) Runtime.getRuntime().maxMemory() / 1000000;
        int used = max - free;
        int i = (int) (100L * used / max);
        Message.m(MType.R, sender, "Lag", "Max memory: " + max + "MB");
        Message.m(MType.R, sender, "Lag", "Used memory: " + used + "MB");
        Message.m(MType.R, sender, "Lag", "Used memory: " + bar(i));
        Message.m(MType.R, sender, "Lag", "Free memory: " + free + "MB");
        Message.m(MType.R, sender, "Lag", "TPS: " + TpsRunnable.getTPS());
        Message.m(MType.R, sender, "TPS", "Lag Rate: " + bar((int) Math.round((1.0D - TpsRunnable.getTPS() / 20.0D) * 100.0D)));
    }

    public static String bar(int percent) {
        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < 25; i++) {
            if (i < (percent / 4)) {
                bar.append(ChatColor.AQUA + "#");
            } else {
                bar.append(ChatColor.DARK_GRAY + "-");
            }
        }
        bar.append("]  " + percent + "%");
        return bar.toString();
    }
}
