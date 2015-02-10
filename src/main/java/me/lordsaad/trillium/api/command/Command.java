package me.lordsaad.trillium.api.command;

public @interface Command {
    String command();
    String description();
    String usage();
    String[] aliases() default {};
}
