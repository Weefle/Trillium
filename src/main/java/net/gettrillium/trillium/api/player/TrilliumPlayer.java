package net.gettrillium.trillium.api.player;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.GroupManager;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.events.PlayerAFKEvent;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.serializer.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TrilliumPlayer {

    private Player proxy;
    private Location previousLocation;
    private String nickname;

    private long lastActive;
    private boolean afk;
    private boolean isMuted = false;
    private boolean isGod = false;
    private boolean isVanished = false;
    private boolean hasNickname = false;
    private boolean pvp;
    private File file;
    private YamlConfiguration yml;

    public TrilliumPlayer(Player proxy) {
        this.proxy = proxy;
        nickname = proxy.getName();
        file = new File(TrilliumAPI.getPlayerFolder(), proxy.getUniqueId() + ".yml");
        yml = YamlConfiguration.loadConfiguration(file);
        load();
    }

    private void save() {
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getProxy() {
        return proxy;
    }

    public boolean isAfk() {
        return afk;
    }

    public void toggleAfk() {
        PlayerAFKEvent event = new PlayerAFKEvent(this.proxy, !this.afk);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            this.afk = !this.afk;
            if (this.afk) {
                new Message(Mood.GENERIC, "AFK", getName() + " is now AFK.").broadcast();
            } else {
                new Message(Mood.GENERIC, "AFK", getName() + " is no longer AFK.").broadcast();
            }
        }
    }

    public void active() {
        if (isAfk() && !isVanished()) {
            toggleAfk();
        }

        lastActive = System.currentTimeMillis();
    }

    public long getLastActive() {
        return this.lastActive;
    }

    public boolean isMuted() {
        return this.isMuted;
    }

    public void setMuted(boolean enabled) {
        this.isMuted = enabled;
    }

    public boolean isFlying() {
        return proxy.getAllowFlight() || getProxy().isFlying();
    }

    public void setFlying(boolean enabled) {
        proxy.setAllowFlight(enabled);
    }

    public boolean isGod() {
        return this.isGod;
    }

    public void setGod(boolean enabled) {
        this.isGod = enabled;
    }

    public String getDisplayName() {
        return this.nickname;
    }

    public void setDisplayName(String nickname) {
        this.nickname = nickname;
        this.hasNickname = !nickname.equalsIgnoreCase(proxy.getName());
        getProxy().setDisplayName(nickname);
    }

    public boolean hasNickname() {
        return this.hasNickname;
    }

    public boolean hasPermission(String permission) {
        return this.getProxy().hasPermission(permission);
    }

    public boolean isVanished() {
        return this.isVanished;
    }

    public void setVanished(boolean enabled) {
        if (enabled) {
            this.isVanished = true;
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.getProxy().hidePlayer(getProxy());
            }
        } else {
            this.isVanished = false;
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.getProxy().showPlayer(getProxy());
            }
        }
    }

    public void dispose() {
        yml.set(Configuration.Player.NICKNAME, this.nickname);
        yml.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
        yml.set(Configuration.Player.MUTED, this.isMuted);
        yml.getBoolean(Configuration.Player.GOD, this.isGod);
        yml.getBoolean(Configuration.Player.VANISH, this.isVanished);
        save();

        proxy = null;
    }

    public void load() {
        boolean newUser = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            newUser = true;
        }

        if (newUser) {
            yml.set(Configuration.Player.NICKNAME, nickname);
            yml.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
            yml.set(Configuration.Player.MUTED, isMuted());
            yml.set(Configuration.Player.GOD, isGod());
            yml.set(Configuration.Player.PVP, canPvp());
            yml.set(Configuration.Player.VANISH, isVanished());
            yml.set(Configuration.Player.BAN_REASON, "");
            yml.set(Configuration.Player.HOMES, "");
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                yml.set(Configuration.Player.GROUP, "default");
            }
        } else {
            setDisplayName(yml.getString(Configuration.Player.NICKNAME));
            setLastLocation(TrilliumAPI.getSerializer(Location.class).deserialize(yml.getString(Configuration.Player.LOCATION)));
            setMuted(yml.getBoolean(Configuration.Player.MUTED));
            setGod(yml.getBoolean(Configuration.Player.GOD));
            setPvp(yml.getBoolean(Configuration.Player.PVP));
            setVanished(yml.getBoolean(Configuration.Player.VANISH));
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                new GroupManager(getProxy()).setGroup(yml.getString(Configuration.Player.GROUP));
            }
        }
        save();
    }

    public Location getLastLocation() {
        return this.previousLocation;
    }

    public void setLastLocation(Location loc) {
        this.previousLocation = loc;
    }

    public void setPvp(boolean b) {
        this.pvp = b;
    }

    public boolean canPvp() {
        return this.pvp;
    }

    public String getName() {
        return getProxy().getName();
    }

    public void setHome(String name, Location loc) {
        List<String> homes = yml.getStringList(Configuration.Player.HOMES);
        homes.add(name + "~" + new LocationSerializer().serialize(loc));
        yml.set(Configuration.Player.HOMES, homes);
        save();
    }

    public void delHome(String name) {
        List<String> homes = yml.getStringList(Configuration.Player.HOMES);
        Iterator<String> iterator = homes.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.split("~")[0].equalsIgnoreCase(name)) {
                iterator.remove();
            }
        }

        yml.set(Configuration.Player.HOMES, iterator);
        save();
    }

    public HashMap<String, Location> getHomes() {
        List<String> homes = yml.getStringList(Configuration.Player.HOMES);
        HashMap<String, Location> deserialized = new HashMap<>();

        for (String home : homes) {
            String[] splits = home.split("~");

            String name = splits[0];
            Location loc = new LocationSerializer().deserialize(splits[1]);
            deserialized.put(name, loc);
        }
        return deserialized;
    }

    public void setCooldown(String cooldownName) {
        yml.set(cooldownName, System.currentTimeMillis());
        save();
    }

    public boolean hasCooldown(String cooldownName) {
        return yml.get(cooldownName) != null;
    }

    public long getCooldown(String cooldownName) {
        return yml.getLong(cooldownName);
    }

    public void removeCooldown(String cooldownName) {
        if (yml.get(cooldownName) != null) {
            yml.set(cooldownName, null);
            save();
        }
    }
}

