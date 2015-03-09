package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.messageutils.M;
import net.gettrillium.trillium.messageutils.T;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.GroupManagerRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void printCurrentMemory(CommandSender sender) {
        int free = (int) Runtime.getRuntime().freeMemory() / 1000000;
        int max = (int) Runtime.getRuntime().maxMemory() / 1000000;
        int used = max - free;
        int i = (int) (100L * used / max);
        M.m(T.R, sender, "Lag", true, "Max memory: " + max + "MB");
        M.m(T.R, sender, "Lag", true, "Used memory: " + used + "MB");
        M.m(T.R, sender, "Lag", true, "Used memory: " + asciibar(i));
        M.m(T.R, sender, "Lag", true, "Free memory: " + free + "MB");
        M.m(T.R, sender, "Lag", true, "TPS: " + TpsRunnable.getTPS());
        M.m(T.R, sender, "Lag", true, "Lag Rate: " + asciibar((int) Math.round((1.0D - TpsRunnable.getTPS() / 20.0D) * 100.0D)));
    }

    public static String asciibar(int percent) {
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
            return asciibar(0);
        } else if (getPing(p) <= 200 && getPing(p) > 100) {
            return asciibar(10);
        } else if (getPing(p) <= 300 && getPing(p) > 200) {
            return asciibar(20);
        } else if (getPing(p) <= 400 && getPing(p) > 300) {
            return asciibar(30);
        } else if (getPing(p) <= 500 && getPing(p) > 400) {
            return asciibar(40);
        } else if (getPing(p) <= 600 && getPing(p) > 500) {
            return asciibar(50);
        } else if (getPing(p) <= 700 && getPing(p) > 600) {
            return asciibar(60);
        } else if (getPing(p) <= 800 && getPing(p) > 700) {
            return asciibar(70);
        } else if (getPing(p) <= 900 && getPing(p) > 800) {
            return asciibar(80);
        } else if (getPing(p) <= 1000 && getPing(p) > 900) {
            return asciibar(90);
        } else {
            return asciibar(100);
        }
    }

    public static List<String> centerText(String input) {
        String desturated = ChatColor.stripColor(input);
        String[] s = stringSplitter(desturated, 40);
        ArrayList<String> centered = new ArrayList<>();
        for (String slices : s) {
            centered.add(StringUtils.center(slices, 60));
        }
        return centered;
    }

    // http://stackoverflow.com/a/12297231/4327834
    // #viva la efficiency
    public static String[] stringSplitter(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        }
        result[lastIndex] = s.substring(j);

        return result;
    }

    public static int timeToTickConverter(String time) {
        int seconds = 0;
        int hours = 0;
        int minutes = 0;
        int days = 0;

        if (time.contains("s")) {
            if (StringUtils.isNumeric(time.split("s")[0])) {
                seconds = Integer.parseInt(time.split("s")[0]);
            }
        }
        if (time.contains("m")) {
            if (StringUtils.isNumeric(time.split("m")[0])) {
                minutes = Integer.parseInt(time.split("m")[0]);
            }
        }
        if (time.contains("h")) {
            if (StringUtils.isNumeric(time.split("h")[0])) {
                hours = Integer.parseInt(time.split("h")[0]);
            }
        }
        if (time.contains("d")) {
            if (StringUtils.isNumeric(time.split("d")[0])) {
                days = Integer.parseInt(time.split("d")[0]);
            }
        }

        return (seconds * 20) + (hours * 3600 * 20) + (minutes * 60 * 20) + (days * 86400 * 20);
    }

    public static void broadcastImportantMessage() {
        List<String> list = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Broadcast.IMP_BROADCAST2);
        for (String s : list) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            Bukkit.broadcastMessage(s);
        }
    }

    public static void reload() {
        Bukkit.getScheduler().cancelAllTasks();
        TrilliumAPI.getInstance().reloadConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new TpsRunnable(), 100, 1);
                Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AutoBroadcastRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Broadcast.FREQUENCY)));
                Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AFKRunnable(), 1, 20);
                Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new GroupManagerRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.GM.RELOAD)));
            }
        }.runTaskLater(TrilliumAPI.getInstance(), 5);
    }
}
