package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.command.TrilliumCommand;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.api.serializer.Serializer;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TrilliumAPI {
    private static Trillium instance;
    private static File playerFolder;
    private static Map<String, TrilliumPlayer> players;
    private static Map<Class<?>, Serializer<?>> serializers;
    private static Map<Class<? extends TrilliumModule>, TrilliumModule> modules;

    public static Trillium getInstance() {
        return instance;
    }

    public static void setInstance(Trillium instance) {
        String className = new Throwable().getStackTrace()[1].getClassName();
        if (className.equalsIgnoreCase(Trillium.class.getCanonicalName())) {
            TrilliumAPI.instance = instance;
            TrilliumAPI.players = new HashMap<>();
            TrilliumAPI.serializers = new HashMap<>();
            TrilliumAPI.modules = new HashMap<>();

            TrilliumAPI.instance.saveDefaultConfig();
            TrilliumAPI.playerFolder = new File(getInstance().getDataFolder(), "players");
            playerFolder.mkdirs();
        } else {
            throw new IllegalStateException("Cannot set instance of TrilliumAPI");
        }
    }

    public static TrilliumPlayer getPlayer(String name) {
        System.out.println("Getting TrilliumPlayer " + name + " and got " + players.get(name));
        return players.get(name);
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

    public static void disposePlayer(Player proxy) {
        if (players.containsKey(proxy.getName())) {
            TrilliumPlayer player = players.remove(proxy.getName());
            player.dispose();
        } else {
            throw new IllegalStateException(String.format("TrilliumPlayer %s does not exist", proxy.getName()));
        }
    }

    public static File getPlayerFolder() {
        return playerFolder;
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
        registerCommand(module);
        module.register();
        modules.put(module.getClass(), module);
    }

    public static boolean isModuleEnabled(Class<? extends TrilliumModule> module) {
        return modules.containsKey(module);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getModule(Class<T> module) {
        return (T) modules.get(module);
    }

    public static Collection<? extends TrilliumPlayer> getOnlinePlayers() {
        return players.values();
    }

    public static void registerCommand(TrilliumModule module) {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) (field.get(getInstance().getServer().getPluginManager()));
            for (Method m : module.getClass().getDeclaredMethods()) {
                Command command = m.getAnnotation(Command.class);
                if (command != null) {
                    TrilliumCommand c = new TrilliumCommand(command.command(), command.description(), command.usage(), command.aliases(), m, module);
                    commandMap.register("trillium", c);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
