package net.gettrillium.trillium.runnables;

import org.bukkit.scheduler.BukkitRunnable;

public class TpsRunnable extends BukkitRunnable {

    public static int tickCount = 0;
    public static long[] ticks = new long[600];

    public static double getTPS() {
        return getTPS(100);
    }

    public static double getTPS(int ticks) {
        if (tickCount <= ticks) {
            return 20.0D;
        }

        int target = (tickCount - 1 - ticks) % TpsRunnable.ticks.length;
        long elapsed = System.currentTimeMillis() - TpsRunnable.ticks[target];

        return ticks / (elapsed / 1000.0D);
    }

    public void run() {
        ticks[(tickCount % ticks.length)] = System.currentTimeMillis();

        tickCount += 1;
    }
}
