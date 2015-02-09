package me.lordsaad.trillium.api.command;

public class CommandException extends Exception {
    private static final long serialVersionUID = 1L;
    
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
