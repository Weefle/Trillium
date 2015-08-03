package net.gettrillium.trillium.api;

import net.gettrillium.trillium.api.Configuration.Afk;
import net.gettrillium.trillium.api.Configuration.Broadcast;
import net.gettrillium.trillium.api.commandbinder.CommandBinder.Blocks;
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

import static net.gettrillium.trillium.api.commandbinder.CommandBinder.Items;

public class Utils {

    private static final Pattern COMMANDBLOCK_REGEX = Pattern.compile("@p", Pattern.LITERAL);
    private static final Pattern COMMA_SPACE = Pattern.compile(", ");

    public static void printCurrentMemory(CommandSender sender) {
        int free = (int) Runtime.getRuntime().freeMemory() / 1000000;
        int max = (int) Runtime.getRuntime().maxMemory() / 1000000;
        int used = max - free;
        int i = (int) ((100L * (long) used) / (long) max);

        new Message(Mood.NEUTRAL, "Max Memory", max + "MB").to(sender);
        new Message(Mood.NEUTRAL, "Used Memory", used + "MB").to(sender);
        new Message(Mood.NEUTRAL, "Free Memory", free + "MB").to(sender);
        new Message(Mood.NEUTRAL, "Used Memory", asciibar(i)).to(sender);
        new Message(Mood.NEUTRAL, "TPS", String.valueOf(TpsRunnable.getTPS())).to(sender);
        new Message(Mood.NEUTRAL, "Lag Rate", asciibar((int) Math.round((1.0D - (TpsRunnable.getTPS() / 20.0D))))).to(sender);
    }

    public static String asciibar(int percent) {
        StringBuilder bar = new StringBuilder();
        bar.append(ChatColor.GRAY);
        bar.append('[');

        for (int i = 0; i < 25; i++) {
            if (i < (percent / 4)) {
                bar.append(ChatColor.AQUA);
                bar.append('#');
            } else {
                bar.append(ChatColor.DARK_GRAY);
                bar.append('-');
            }
        }

        bar.append(ChatColor.GRAY);
        bar.append("]  ");
        bar.append(ChatColor.AQUA);
        bar.append(percent);
        bar.append('%');
        return bar.toString();
    }

    // TODO - unit test this
    public static List<String> centerText(String input) {
        String desturated = ChatColor.stripColor(input);
        String[] s = stringSplitter(desturated, 40);
        List<String> centered = new ArrayList<>(s.length);
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

    // TODO - unit test this, make more efficient
    // http://stackoverflow.com/a/12297231/4327834
    // #efficiency
    public static String[] stringSplitter(String s, int interval) {
        int arrayLength = (int) Math.ceil((((double) s.length() / (double) interval)));
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

    // TODO - unit test this
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

    // TODO - unit test this
    /**
     * Compares two version numbers
     * @param version1 first version
     * @param version2 another version
     * @return true iff version1 is newer than version2
     */
    public static boolean compareVersion(String version1, String version2) {
        if ((version1 == null) || (version2 == null)) {
            return false;
        }

        String[] a = version1.split("\\.");
        String[] b = version2.split("\\.");

        // for the parts of the version number that both share,
        for (int i = 0; (i < a.length) && (i < b.length); ++i) {
            if (Integer.parseInt(a[i]) > Integer.parseInt(b[i])) {
                return true;
            }
        }

        // return true if the version we're comparing to is longer
        return (a.length > b.length);
    }

    public static int timeToTickConverter(String time) {
        int seconds = 0;

        if (time.contains("s")) {
            seconds = endOfStringInt(time.split("s")[0]);
        }
        if (time.contains("m")) {
            seconds += endOfStringInt(time.split("m")[0]) * 60;
        }
        if (time.contains("h")) {
            seconds += endOfStringInt(time.split("h")[0]) * 3600;
        }
        if (time.contains("d")) {
            seconds += endOfStringInt(time.split("d")[0]) * 86400;
        }

        return seconds * 20;
    }

    public static String arrayToString(String[] array) {
        String toString = null;
        for (String content : array) {
            toString += ";" + content;
        }
        return toString;
    }

    public static String[] arrayFromString(String fromString) {
        return fromString.split(";");
    }

    public static String timeToString(int ticks) {
        int millis = (ticks / 20) * 1000;

        return String.format("%02d:%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toDays((long) millis),
                TimeUnit.MILLISECONDS.toHours((long) millis) % TimeUnit.DAYS.toHours(1L),
                TimeUnit.MILLISECONDS.toMinutes((long) millis) % TimeUnit.HOURS.toMinutes(1L),
                TimeUnit.MILLISECONDS.toSeconds((long) millis) % TimeUnit.MINUTES.toSeconds(1L));
    }

    public static void broadcastImportantMessage() {
        List<String> list = TrilliumAPI.getInstance().getConfig().getStringList(Broadcast.IMP_BROADCAST2);
        for (String s : list) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public static void clearInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
    }

    public static List<String> convertFileToBookPages(File book) {
        List<String> pages = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        try {
            for (Object o : FileUtils.readLines(book)) {
                String line = o.toString();
                line = ChatColor.translateAlternateColorCodes('&', line);

                if (line.equals("<-NEXT->")) {
                    pages.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(line);
                    sb.append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pages;
    }

    // TODO - unit test this
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
        int x = Integer.parseInt(COMMA_SPACE.split(s)[1]);
        int y = Integer.parseInt(s.split(", ")[2]);
        int z = Integer.parseInt(s.split(", ")[3]);
        return new Location(world, (double) x, (double) y, (double) z);
    }

    // TODO - unit test this
    public static String locationSerializer(Location loc) {
        return loc.getWorld() + "%" + loc.getX() + '%' + loc.getY() + '%' + loc.getZ() + '%' + loc.getPitch() + '%' + loc.getYaw();
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

    // TODO - unit test this
    public static String commandBlockify(String command, Player p) {
        return COMMANDBLOCK_REGEX.matcher(command).replaceAll(p.getName());
    }

    public static void load() {
        TrilliumAPI.getInstance().saveDefaultConfig();

        TrilliumAPI.registerModules();
        TrilliumAPI.loadPlayers();

        Blocks.setTable();
        Items.setTable();
        Warp.setWarps();
        Reports.setReports();

        new TpsRunnable().runTaskTimer(TrilliumAPI.getInstance(), 100L, 1L);
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Broadcast.AUTO_ENABLED)) {
            new AutoBroadcastRunnable().runTaskTimer(TrilliumAPI.getInstance(), 1L, (long) timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Broadcast.FREQUENCY)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Afk.AUTO_AFK_ENABLED)) {
            new AFKRunnable().runTaskTimer(TrilliumAPI.getInstance(), 1L, (long) timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Afk.AUTO_AFK_TIME)));
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
