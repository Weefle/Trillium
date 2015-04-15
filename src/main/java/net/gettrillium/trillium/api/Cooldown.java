package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Utils;

public class Cooldown {

    private long time;
    private int length;

    public Cooldown(int length) {
        this.time = System.currentTimeMillis();
        this.length = length;
    }

    public Cooldown() {
        this.time = System.currentTimeMillis();
        this.length = Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Teleport.TELEPORTATION_COOLDOWN_TIME));
    }

    public int getTimeLeft() {
        return (int) (System.currentTimeMillis() - time);
    }

    public boolean isDone() {
        return getTimeLeft() >= length;
    }
}
