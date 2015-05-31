package net.gettrillium.trillium.api.cooldown;

public enum CooldownType {

    TP("tp");

    private String cooldown;

    CooldownType(String error) {
        this.cooldown = error;
    }

    public String getCooldownType() {
        return cooldown;
    }
}
