package me.lordsaad.trillium.messageutils;

public  enum Crit {
    P("You don't have permission to do that."),
    C("You can't do that."),
    A("Too few arguments. "),
    T(" is either not online or does not exist.");

    private String prefix;

    private Crit(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}