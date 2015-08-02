package net.gettrillium.trillium.api;

import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.report.Reports;
import net.gettrillium.trillium.api.warp.Warp;
import net.gettrillium.trillium.events.PlayerDeath;
import net.gettrillium.trillium.events.ServerListPing;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void printCurrentMemory(CommandSender sender) {
        int free = (int) Runtime.getRuntime().freeMemory() / 1000000;
        int max = (int) Runtime.getRuntime().maxMemory() / 1000000;
        int used = max - free;
        int i = (int) (100L * used / max);

        new Message(Mood.NEUTRAL, "Max Memory", max + "MB").to(sender);
        new Message(Mood.NEUTRAL, "Used Memory", used + "MB").to(sender);
        new Message(Mood.NEUTRAL, "Free Memory", free + "MB").to(sender);
        new Message(Mood.NEUTRAL, "Used Memory", asciibar(i)).to(sender);
        new Message(Mood.NEUTRAL, "TPS", "" + TpsRunnable.getTPS()).to(sender);
        new Message(Mood.NEUTRAL, "Lag Rate", asciibar((int) Math.round((1.0D - TpsRunnable.getTPS() / 20.0D)))).to(sender);
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

    public static List<String> centerText(String input) {
        String desturated = ChatColor.stripColor(input);
        String[] s = stringSplitter(desturated, 40);
        ArrayList<String> centered = new ArrayList<>();
        for (String slices : s) {
            centered.add(StringUtils.center(slices, 60));
        }
        return centered;
    }

    public static void clearChat(Player p) {
        for (int i = 0; i < 200; i++) {
            p.sendMessage(" ");
        }
    }

    // http://stackoverflow.com/a/12297231/4327834
    // #efficiency
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

    // http://stackoverflow.com/a/3083197/4327834
    public static int endOfStringInt(String input) {
        Pattern p = Pattern.compile("[0-9]+$");
        Matcher m = p.matcher(input);
        if (m.find()) {
            return Integer.parseInt(m.group());
        } else {
            return 0;
        }
    }

    public static boolean compareVersion(String version1, String version2) {
        if (version1 != null && version2 != null) {
            String[] a = version1.split("\\.");
            String[] b = version2.split("\\.");
            for (int i = 0; i < a.length || i < b.length; ++i) {
                if (i < a.length && i < b.length) {
                    if (Integer.valueOf(a[i]) > Integer.valueOf(b[i])) return true;
                }
            }
        }
        return false;
    }

    public static int timeToTickConverter(String time) {
        int seconds = 0;
        int hours = 0;
        int minutes = 0;
        int days = 0;

        if (time.contains("s")) {
            seconds = endOfStringInt(time.split("s")[0]);
        }
        if (time.contains("m")) {
            minutes = endOfStringInt(time.split("m")[0]);
        }
        if (time.contains("h")) {
            hours = endOfStringInt(time.split("h")[0]);
        }
        if (time.contains("d")) {
            days = endOfStringInt(time.split("d")[0]);
        }

        return (seconds * 20) + (hours * 3600 * 20) + (minutes * 60 * 20) + (days * 86400 * 20);
    }

    public static String timeToString(int ticks) {
        int millis = ticks / 20 * 1000;

        return String.format("%02d:%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis) % TimeUnit.DAYS.toHours(1),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static void broadcastImportantMessage() {
        List<String> list = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Broadcast.IMP_BROADCAST2);
        for (String s : list) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            Bukkit.broadcastMessage(s);
        }
    }

    public static void clearInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
    }

    public static ArrayList<String> convertFileToBookPages(File book) {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> pages = new ArrayList<>();
        try {
            for (Object line : FileUtils.readLines(book)) {
                String s = line + "";
                s = ChatColor.translateAlternateColorCodes('&', s);
                lines.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = "<-NEXT->";
        for (String line : lines) {
            if (!line.equals("<-NEXT->")) {
                if (!s.equals("<-NEXT->")) {
                    s = s + "\n" + line;
                } else {
                    s = line;
                }
            } else {
                pages.add(s);
                s = "<-NEXT->";
            }
        }
        return pages;
    }

    public static String locationToString(Location l) {
        if (l == null) {
            return null;
        }

        return l.getWorld().getName() + ", " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
    }

    public static Location locationFromString(String s) {
        if (s == null) {
            return null;
        }

        World world = Bukkit.getWorld(s.split(", ")[0]);
        int x = Integer.parseInt(s.split(", ")[1]);
        int y = Integer.parseInt(s.split(", ")[2]);
        int z = Integer.parseInt(s.split(", ")[3]);
        return new Location(world, x, y, z);
    }

    public static String locationSerializer(Location loc) {
        return loc.getWorld() + "%" + loc.getX() + "%" + loc.getY() + "%" + loc.getZ() + "%" + loc.getPitch() + "%" + loc.getYaw();
    }

    public static Location locationDeserializer(String loc) {
        World world = Bukkit.getWorld(loc.split("%")[0]);
        double x = Double.parseDouble(loc.split("%")[1]);
        double y = Double.parseDouble(loc.split("%")[2]);
        double z = Double.parseDouble(loc.split("%")[3]);
        float pitch = Float.parseFloat(loc.split("%")[4]);
        float yaw = Float.parseFloat(loc.split("%")[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String commandBlockify(String command, Player p) {
        return command.replace("@p", p.getName());
    }

    public static void load() {
        TrilliumAPI.getInstance().saveDefaultConfig();

        TrilliumAPI.registerModules();
        TrilliumAPI.loadPlayers();

        CommandBinder.Blocks.setTable();
        CommandBinder.Items.setTable();
        Warp.setWarps();
        Reports.setReports();

        new TpsRunnable().runTaskTimer(TrilliumAPI.getInstance(), 100, 1);
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Broadcast.AUTO_ENABLED)) {
            new AutoBroadcastRunnable().runTaskTimer(TrilliumAPI.getInstance(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Broadcast.FREQUENCY)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            new AFKRunnable().runTaskTimer(TrilliumAPI.getInstance(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Afk.AUTO_AFK_TIME)));
        }

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), TrilliumAPI.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(new ServerListPing(), TrilliumAPI.getInstance());
    }

    public static void unload() {
        Bukkit.getScheduler().cancelAllTasks();
        TrilliumAPI.disposePlayers();
        TrilliumAPI.unregisterModules();
    }
}
