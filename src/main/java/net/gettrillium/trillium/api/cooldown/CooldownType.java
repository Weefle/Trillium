package net.gettrillium.trillium.api.cooldown;

import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.Utils;

public enum CooldownType {

    // TODO: multiple kit cooldowns.

    TELEPORTATION("teleportation"),
    KIT("kit"),
    CHAT("chat");

    private String cooldown;

    CooldownType(String cooldown) {
        this.cooldown = cooldown;
    }

    public String getType() {
        return cooldown;
    }

    public int getTimeInTicks() {
        return Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString("server.cooldown." + cooldown));
    }
}
