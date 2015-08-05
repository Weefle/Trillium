package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.Configuration.Economy;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.command.TrilliumCommand;
import net.gettrillium.trillium.modules.AFKModule;
import net.gettrillium.trillium.modules.AbilityModule;
import net.gettrillium.trillium.modules.AdminModule;
import net.gettrillium.trillium.modules.ChatModule;
import net.gettrillium.trillium.modules.CommandBinderModule;
import net.gettrillium.trillium.modules.CoreModule;
import net.gettrillium.trillium.modules.EconomyModule;
import net.gettrillium.trillium.modules.FunModule;
import net.gettrillium.trillium.modules.KitModule;
import net.gettrillium.trillium.modules.PunishModule;
import net.gettrillium.trillium.modules.TeleportModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

public class TrilliumAPI {
    private static Trillium instance;
    private static File playerFolder;
    private static Map<UUID, TrilliumPlayer> players;
    private static Map<Class<? extends TrilliumModule>, TrilliumModule> modules;
    private static Map<String, String[]> commands;
    public static Trillium getInstance() {
        return instance;
    }

    public static void setInstance(Trillium instance) {
        String className = new Throwable().getStackTrace()[1].getClassName();
        if (className.equalsIgnoreCase(Trillium.class.getCanonicalName())) {
            TrilliumAPI.instance = instance;
            players = new HashMap<>();
            modules = new HashMap<>();
            commands = new TreeMap<>();

            TrilliumAPI.instance.saveDefaultConfig();
            playerFolder = new File(getInstance().getDataFolder(), "Player Database");
            playerFolder.mkdirs();
        } else {
            throw new IllegalStateException("Cannot set instance of TrilliumAPI");
        }
    }

    public static TrilliumPlayer getPlayer(String name) {
        Player p = Bukkit.getPlayer(name);
        return getPlayer(p);
    }

    public static TrilliumPlayer getPlayer(Player player) {
        if (player == null) {
            return null;
        }

        return getPlayer(player.getUniqueId());
    }

    public static TrilliumPlayer getPlayer(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        return players.get(uuid);
    }

    public static void loadPlayer(Player proxy) {
        if (!players.containsKey(proxy.getUniqueId())) {
            TrilliumPlayer p = new TrilliumPlayer(proxy);
            p.load();
            players.put(proxy.getUniqueId(), p);
        }
    }

    public static void disposePlayer(Player proxy) {
        if (isLoaded(proxy)) {
            TrilliumPlayer player = players.remove(proxy.getUniqueId());
            player.dispose();
        }
    }

    public static void disposePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isLoaded(player)) {
                TrilliumPlayer p = players.remove(player.getUniqueId());
                p.dispose();
            }
        }
    }

    public static void loadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!players.containsKey(player.getUniqueId())) {
                TrilliumPlayer p = new TrilliumPlayer(player);
                p.load();
                players.put(player.getUniqueId(), p);
            }
        }
    }

    public static File getPlayerFolder() {
        return playerFolder;
    }

    public static boolean isLoaded(Player p) {
        return players.containsKey(p.getUniqueId());
    }

    public static void registerModule(TrilliumModule module) {
        instance.getServer().getPluginManager().registerEvents(module, instance);
        registerCommands(module);
        module.register();
        modules.put(module.getClass(), module);
    }

    public static void unregisterModules() {
        unregisterCommands();
        Iterator<Entry<Class<? extends TrilliumModule>, TrilliumModule>> it = modules.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Class<? extends TrilliumModule>, TrilliumModule> item = it.next();
            item.getValue().unregister();
            it.remove();
        }
    }

    public static void registerModules() {
        registerModule(new AFKModule());
        registerModule(new PunishModule());
        registerModule(new AbilityModule());
        registerModule(new AdminModule());
        registerModule(new CoreModule());
        registerModule(new TeleportModule());
        registerModule(new ChatModule());
        registerModule(new FunModule());
        registerModule(new CommandBinderModule());
        if (getInstance().getConfig().getBoolean(Configuration.Kit.ENABLED))
            registerModule(new KitModule());
        if (getInstance().getConfig().getBoolean(Economy.ENABLED))
            registerModule(new EconomyModule());
    }

    public static Collection<? extends TrilliumPlayer> getOnlinePlayers() {
        return players.values();
    }

    public static Map<String, String[]> getCommands() {
        return commands;
    }

    public static String getName(String command) {
        if (commands.containsKey(command)) {
            return commands.get(command)[0];
        } else {
            return "null";
        }
    }

    public static String getDescription(String command) {
        if (commands.containsKey(command)) {
            return commands.get(command)[1];
        } else {
            return "null";
        }
    }

    public static String[] getPermissions(String command) {
        if (commands.containsKey(command)) {
            return Utils.arrayFromString(commands.get(command)[2]);
        } else {
            return new String[]{"null"};
        }
    }

    public static String getUsage(String command) {
        if (commands.containsKey(command)) {
            return commands.get(command)[3];
        } else {
            return "null";
        }
    }

    public static String[] getAliases(String command) {
        if (commands.containsKey(command)) {
            return Utils.arrayFromString(commands.get(command)[4]);
        } else {
            return new String[]{"null"};
        }
    }

    public static void registerCommands(TrilliumModule module) {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) (field.get(getInstance().getServer().getPluginManager()));
            for (Method m : module.getClass().getDeclaredMethods()) {
                Command command = m.getAnnotation(Command.class);
                if (command != null) {
                    TrilliumCommand c = new TrilliumCommand(command.command()
                            , command.description()
                            , command.usage()
                            , command.aliases()
                            , m
                            , module);
                    commandMap.register("trillium", c);

                    commands.put(command.command(),
                            new String[]{command.name()
                                    , command.description()
                                    , Utils.arrayToString(command.permissions())
                                    , command.usage()
                                    , Utils.arrayToString(command.aliases())});
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterCommands() {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) (field.get(getInstance().getServer().getPluginManager()));
            if (commandMap.getCommand("trillium").isRegistered()) {
                commandMap.getCommand("trillium").unregister(commandMap);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
