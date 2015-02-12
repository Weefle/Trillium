package me.lordsaad.trillium.api.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import me.lordsaad.trillium.api.TrilliumModule;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TrilliumCommand extends Command {
    private final Method invoke;
    private final TrilliumModule module;

    public TrilliumCommand(String name, String description, String usageMessage, String[] aliases, Method invoke, TrilliumModule module) {
        super(name, description, usageMessage, Arrays.asList(aliases));
        this.invoke = invoke;
        this.module = module;
    }

    @Override
    public boolean execute(CommandSender paramCommandSender, String paramString, String[] paramArrayOfString) {
        try {
            invoke.invoke(module, paramCommandSender, paramArrayOfString);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

}
