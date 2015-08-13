package net.gettrillium.trillium.api.command;

public class CommandException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
