package net.gettrillium.trillium.api.cooldown;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.TrilliumAPI;

public enum CooldownType {

    TELEPORTATION("teleportation");

    private String cooldown;

    CooldownType(String cooldown) {
        this.cooldown = cooldown;
    }

    public String getCooldownType() {
        return cooldown;
    }

    public int getTimeInTicks() {
        return Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString("player-settings.cooldown." + cooldown));
    }
}
