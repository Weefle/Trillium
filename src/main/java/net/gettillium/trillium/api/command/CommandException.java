package net.gettillium.trillium.api.command;

public class CommandException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
