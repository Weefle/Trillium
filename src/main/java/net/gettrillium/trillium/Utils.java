package net.gettrillium.trillium;

import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.runnables.TpsRunnable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Utils {

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
        Message.m(MType.R, sender, "Lag", "Lag Rate: " + bar((int) Math.round((1.0D - TpsRunnable.getTPS() / 20.0D) * 100.0D)));
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

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
