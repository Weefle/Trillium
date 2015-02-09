package me.lordsaad.trillium;

import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.databases.PlayerDatabase;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.runnables.TpsRunnable;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

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
        
        List<String> format =  Main.plugin.getConfig().getStringList("Broadcast");
        
        for (String s : format) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replace("[msg]", message);
            Bukkit.broadcastMessage(s);
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

                if (Main.plugin.getConfig().getBoolean("spectator mode")) {
                    p.setGameMode(GameMode.SPECTATOR);
                }
                
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

                if (Main.plugin.getConfig().getBoolean("spectator mode")) {
                    p.setGameMode(GameMode.SURVIVAL);
                }
                
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
        StringBuilder bar = new StringBuilder(ChatColor.GRAY + "[");

        for (int i = 0; i < 25; i++) {
            if (i < (percent / 4)) {
                bar.append(ChatColor.AQUA + "#");
            } else {
                bar.append(ChatColor.DARK_GRAY + "-");
            }
        }
        bar.append(ChatColor.GRAY + "]  " + ChatColor.AQUA + percent + "%");
        return bar.toString();
    }
    
    public static int getping(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }
    
    public static String getpingbar(Player p) {
        if (getping(p) <= 100 && getping(p) >= 0) {
            return bar(100);
        } else if (getping(p) <= 200 && getping(p) > 100) {
            return bar(90);
        } else if (getping(p) <= 300 && getping(p) > 200) {
            return bar(80);
        } else if (getping(p) <= 400 && getping(p) > 300) {
            return bar(70);
        } else if (getping(p) <= 500 && getping(p) > 400) {
            return bar(60);
        } else if (getping(p) <= 600 && getping(p) > 500) {
            return bar(50);
        } else if (getping(p) <= 700 && getping(p) > 600) {
            return bar(40);
        } else if (getping(p) <= 800 && getping(p) > 700) {
            return bar(30);
        } else if (getping(p) <= 900 && getping(p) > 800) {
            return bar(20);
        } else if (getping(p) <= 1000 && getping(p) > 900) {
            return bar(10);
        } else {
            return bar(0);
        }
    }
}
