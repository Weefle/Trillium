package me.lordsaad.trillium.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.lordsaad.trillium.Trillium;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.command.TrilliumCommand;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.api.serializer.Serializer;

import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

public class TrilliumAPI {
    private static Trillium instance;
    private static Map<String, TrilliumPlayer> players;
    private static Map<Class<?>, Serializer<?>> serializers;
    
    public static void setInstance(Trillium instance) {
        String className = new Throwable().getStackTrace()[1].getClassName();
        if (className.equalsIgnoreCase(Trillium.class.getCanonicalName())) {
            TrilliumAPI.instance = instance;
            TrilliumAPI.players = new HashMap<>();
            TrilliumAPI.serializers = new HashMap<>();
        } else {
            throw new IllegalStateException("Cannot set instance of TrilliumAPI");
        }
    }

    public static Trillium getInstance() {
        return instance;
    }
    
    public static TrilliumPlayer getPlayer(String name) {
        TrilliumPlayer p = players.get(name);
        p.active();
        return p;
    }
    
    public static TrilliumPlayer createNewPlayer(Player proxy) {
        if (!players.containsKey(proxy.getName())) {
            TrilliumPlayer p = new TrilliumPlayer(proxy);
            players.put(proxy.getName(), p);
            return p;
        } else {
            throw new IllegalStateException(String.format("TrilliumPlayer %s already exists", proxy.getName()));
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Serializer<T> getSerializer(Class<T> clazz) {
        return (Serializer<T>) serializers.get(clazz);
    }
    
    public static <T> void registerSerializer(Class<T> clazz, Serializer<T> serializer) {
        serializers.put(clazz, serializer);
    }
    
    public static void registerModule(TrilliumModule module) {
        instance.getServer().getPluginManager().registerEvents(module, instance);
        registerCommand(module.getClass());
    }

    public static void registerCommand(Class<?> commandClass) {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) (field.get(getInstance().getServer().getPluginManager()));
            for (Method m : commandClass.getDeclaredMethods()) {
                Command command = m.getAnnotation(Command.class);
                if (command != null) {
                    TrilliumCommand c = new TrilliumCommand(command.command(), command.description(), command.usage(), command.aliases(), m);
                    commandMap.register(command.command(), c);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
