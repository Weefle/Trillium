package net.gettrillium.trillium.messageutils;

public enum Crit {
    P("You don't have permission to do that."),
    C("You can't do that.");

    private String prefix;

    private Crit(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}