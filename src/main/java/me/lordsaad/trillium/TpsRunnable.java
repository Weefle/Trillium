package me.lordsaad.trillium;

public class TpsRunnable implements Runnable {
    
    public static int TickCount = 0;
    public static long[] Ticks = new long[600];

    public static double getTPS() {
        return getTPS(100);
    }

    public static double getTPS(int ticks) {
        if (TickCount < ticks) {
            return 20.0D;
        }
        int target = (TickCount - 1 - ticks) % Ticks.length;
        long elapsed = System.currentTimeMillis() - Ticks[target];

        return ticks / (elapsed / 1000.0D);
    }

    public void run() {
        Ticks[(TickCount % Ticks.length)] = System.currentTimeMillis();

        TickCount += 1;
    }
}