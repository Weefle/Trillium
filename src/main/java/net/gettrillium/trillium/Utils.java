package net.gettrillium.trillium;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.runnables.AFKRunnable;
import net.gettrillium.trillium.runnables.AutoBroadcastRunnable;
import net.gettrillium.trillium.runnables.GroupManagerRunnable;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static void printCurrentMemory(CommandSender sender) {
        int free = (int) Runtime.getRuntime().freeMemory() / 1000000;
        int max = (int) Runtime.getRuntime().maxMemory() / 1000000;
        int used = max - free;
        int i = (int) (100L * used / max);

        new Message(Mood.GENERIC, "Max Memory", max + "MB").to(sender);
        new Message(Mood.GENERIC, "Used Memory", used + "MB").to(sender);
        new Message(Mood.GENERIC, "Free Memory", free + "MB").to(sender);
        new Message(Mood.GENERIC, "Used Memory", asciibar(i)).to(sender);
        new Message(Mood.GENERIC, "TPS", "" + TpsRunnable.getTPS()).to(sender);
        new Message(Mood.GENERIC, "Lag Rate", asciibar((int) Math.round((1.0D - TpsRunnable.getTPS() / 20.0D)))).to(sender);
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
            p.sendMessage("");
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

    public static String TimeToString(int ticks) {
        int millis = ticks / 20 * 1000;

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
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
        for (int i = 0; i < 35; i++) {
            p.getInventory().setItem(i, null);
        }
        p.getInventory().setArmorContents(null);
    }

    public static String readFile(File f) {

        if (!f.exists()) {
            return "";
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder b = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                b.append(line);
                b.append(System.getProperty("line.separator"));
            }
            return b.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<String> readFileLines(File f) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));

            String str;

            while ((str = in.readLine()) != null) {
                list.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
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
            @Override
            public void run() {
                for (Ocelot cat : catList) {
                    cat.setHealth(0);
                }
            }
        }.runTaskLater(TrilliumAPI.getInstance(), 50);
    }

    public static ArrayList<String> getEncapsulations(String message, String symbol) {
        ArrayList<String> encapsulations = new ArrayList<>();
        if (message.contains(symbol)) {
            ArrayList<Integer> symbols = new ArrayList<>();

            for (int index = message.indexOf(symbol); index >= 0; index = message.indexOf(symbol, index + 1)) {
                symbols.add(index);
            }

            HashMap<Integer, Integer> patterns = new HashMap<>();
            for (int start : symbols) {
                for (int end : symbols) {
                    if (start < end) {
                        if ((start + 1) != end) {
                            if (!patterns.containsKey(start) && !patterns.containsValue(end)) {
                                encapsulations.add(message.substring(start, end).replace(symbol, ""));
                                patterns.put(start, end);
                            }
                        }
                    }
                }
            }

        }
        return encapsulations;
    }

    public static ArrayList<String> getWordAfterSymbol(String message, String symbol) {
        ArrayList<String> words = new ArrayList<>();
        if (message.contains(symbol)) {
            String[] slices = message.split(" ");
            for (String word : slices) {
                if (word.startsWith(symbol)) {
                    words.add(word);
                }
            }
        }
        return words;
    }

    public static void reload() {
        Bukkit.getScheduler().cancelAllTasks();
        new BukkitRunnable() {
            @Override
            public void run() {
                TrilliumAPI.getInstance().reloadConfig();

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
        }.runTaskLater(TrilliumAPI.getInstance(), 5);
    }
}
