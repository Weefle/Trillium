package me.lordsaad.trillium.api.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TrilliumCommand extends Command {
    private final Method invoke;
    
    public TrilliumCommand(String name, String description, String usageMessage, String[] aliases, Method invoke) {
        super(name, description, usageMessage, Arrays.asList(aliases));
        this.invoke = invoke;
    }

    @Override
    public boolean execute(CommandSender paramCommandSender, String paramString, String[] paramArrayOfString) {
        try {
            invoke.invoke(null, paramCommandSender, paramArrayOfString);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

}
