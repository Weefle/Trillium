package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.GroupManagerRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    // http://bukkit.org/threads/text-alignment-in-chat.83895/#post-2291644
    public static String tablizer(String s, int size) {

        String ret = s.toUpperCase();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'I' || s.charAt(i) == ' ') {
                ret += " ";
            }
        }

        int subtract = size - s.length();
        subtract = (subtract * 2);

        for (int i = 0; i < subtract; i++) {
            ret += " ";
        }

        return (ret);
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

    private static double randomV() {
        return Math.random() * 4 - 1;
    }

    public static void throwCats(Location original, Player p) {

        final ArrayList<Ocelot> catList = new ArrayList<>();

        for (int cats = 1; cats < 8; cats++) {

            final Ocelot cat = original.getWorld().spawn(original, Ocelot.class);
            cat.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
            Random random = new Random();
            int i = random.nextInt(Ocelot.Type.values().length);
            cat.setCatType(Ocelot.Type.values()[i]);
            cat.setTamed(true);
            cat.setBaby();
            cat.setOwner(p);
            cat.setBreed(false);
            cat.setSitting(true);
            cat.setAgeLock(true);
            cat.setAge(6000);

            catList.add(cat);
        }

        new BukkitRunnable() {
            int countdown = 50;

            public void run() {
                if (countdown != 0) {
                    for (Ocelot cat : catList) {
                        ParticleEffect.DRIP_LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, 0, 5, cat.getLocation(), 500);
                        countdown--;
                    }
                } else {
                    for (Ocelot cat : catList) {
                        cat.setHealth(0);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(TrilliumAPI.getInstance(), 1, 1);
    }


    public static void reload() {
        // STOP
        Bukkit.getScheduler().cancelAllTasks();
        TrilliumAPI.disposePlayers();
        TrilliumAPI.unregisterModules();

        // START
        TrilliumAPI.getInstance().saveDefaultConfig();
        TrilliumAPI.getInstance().reloadConfig();

        TrilliumAPI.registerModules();
        TrilliumAPI.loadPlayers();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new TpsRunnable(), 100, 1);
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Broadcast.AUTO_ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AutoBroadcastRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Broadcast.FREQUENCY)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.Afk.AUTO_AFK_ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new AFKRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Afk.AUTO_AFK_TIME)));
        }
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TrilliumAPI.getInstance(), new GroupManagerRunnable(), 1, Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.GM.RELOAD)));
        }
    }
}
