package me.lordsaad.trillium;

import java.io.IOException;
import java.util.List;

import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandGodMode;
import me.lordsaad.trillium.commands.CommandMute;
import me.lordsaad.trillium.commands.CommandVanish;
import me.lordsaad.trillium.databases.PlayerDatabase;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.runnables.TpsRunnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class API {

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isOnline(Player p) {
        return p.isOnline() && !CommandVanish.vanishedusers.contains(p.getUniqueId());
    }

    public static String locationString(Player p) {
        if (p.isOnline()) {
            Location loc = p.getLocation();
            return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + p.getWorld().getName();
        } else {
            return "null";
        }
    }

    public static int getFoodLevel(Player p) {
        if (p.isOnline()) {
            return p.getFoodLevel();
        } else {
            return 0;
        }
    }

    public static double getHealthLevel(Player p) {
        if (p.isOnline()) {
            return p.getHealthScale();
        } else {
            return 0;
        }
    }

    public static String getGamemode(Player p) {
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

    public static int getTimePlayed(Player p) {
        return p.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
    }

    public static String lastLocationString(Player p) {
        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        int x = pdb.getInt("Last Location.x");
        int y = pdb.getInt("Last Location.y");
        int z = pdb.getInt("Last Location.z");
        String world = pdb.getString("Last Location.world");
        return world + ", " + x + ", " + y + ", " + z;
    }

    public static boolean isMuted(Player p) {
        return CommandMute.muted.contains(p.getUniqueId());
    }

    public static void setMuted(boolean b, Player p) {
        if (b) {
            if (!isMuted(p)) {
                CommandMute.muted.add(p.getUniqueId());
            }
        } else {
            if (isMuted(p)) {
                CommandMute.muted.remove(p.getUniqueId());
            }
        }
    }

    public static boolean isFlying(Player p) {
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

    public static boolean isGodMode(Player p) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        return CommandGodMode.godmodeusers.contains(p.getUniqueId()) || yml.getBoolean("God Mode");
    }

    public static void setGodMode(boolean b, Player p) {
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

        List<String> format = TrilliumAPI.getInstance().getConfig().getStringList("Broadcast");

        for (String s : format) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replace("[msg]", message);
            Bukkit.broadcastMessage(s);
        }
    }

    public static boolean isVanished(Player p) {
        return CommandVanish.vanishedusers.contains(p.getUniqueId());
    }

    public static void setVanished(boolean b, Player p) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        if (b) {
            if (!CommandVanish.vanishedusers.contains(p.getUniqueId())) {
                yml.set("Vanish Mode", true);
                CommandVanish.vanishedusers.add(p.getUniqueId());

                if (TrilliumAPI.getInstance().getConfig().getBoolean("spectator mode")) {
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

                if (TrilliumAPI.getInstance().getConfig().getBoolean("spectator mode")) {
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

    public static void printCurrentMemory(CommandSender sender) {
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

    public static int getPing(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }

    public static String getPingBar(Player p) {
        if (getPing(p) <= 100 && getPing(p) >= 0) {
            return bar(100);
        } else if (getPing(p) <= 200 && getPing(p) > 100) {
            return bar(90);
        } else if (getPing(p) <= 300 && getPing(p) > 200) {
            return bar(80);
        } else if (getPing(p) <= 400 && getPing(p) > 300) {
            return bar(70);
        } else if (getPing(p) <= 500 && getPing(p) > 400) {
            return bar(60);
        } else if (getPing(p) <= 600 && getPing(p) > 500) {
            return bar(50);
        } else if (getPing(p) <= 700 && getPing(p) > 600) {
            return bar(40);
        } else if (getPing(p) <= 800 && getPing(p) > 700) {
            return bar(30);
        } else if (getPing(p) <= 900 && getPing(p) > 800) {
            return bar(20);
        } else if (getPing(p) <= 1000 && getPing(p) > 900) {
            return bar(10);
        } else {
            return bar(0);
        }
    }
}
