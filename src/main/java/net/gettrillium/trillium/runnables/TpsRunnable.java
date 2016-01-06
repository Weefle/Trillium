package net.gettrillium.trillium.runnables;

import org.bukkit.scheduler.BukkitRunnable;

public class TpsRunnable extends BukkitRunnable {

    // 0 to ticks.length - 1
    // this is the count of the LAST tick
    private static int currentTick;
    private static long[] ticks = new long[600];

    public static double getTPS() {
        return getTPS(100);
    }

    private static double getTPS(int tickCount) {
        // prevent wraparounds
        if (tickCount > ticks.length) {
            tickCount = ticks.length;
        }

        // if the array isn't filled up until this point yet
        if (ticks[tickCount] == 0.0) {
            tickCount = currentTick;
        }

        int index = ((currentTick - tickCount) + ticks.length) % ticks.length;
        long elapsed = ticks[currentTick] - ticks[index];

        return (double) tickCount / ((double) elapsed / 1000.0D);
    }

    @Override
    public void run() {
        currentTick = ++currentTick % ticks.length;

        ticks[currentTick] = System.currentTimeMillis();
    }
}
