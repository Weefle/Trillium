package net.gettrillium.trillium.api.player;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.GroupManager;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

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

    public TrilliumPlayer(Player proxy) {
        this.proxy = proxy;
        this.nickname = proxy.getName();
        load();
    }

    public Player getProxy() {
        return proxy;
    }

    public boolean isAfk() {
        return afk;
    }

    public void toggleAfk() {
        this.afk = !this.afk;
        if (this.afk) {
            new Message(Mood.GENERIC, "AFK", getName() + " is now AFK.").broadcast();
        } else {
            new Message(Mood.GENERIC, "AFK", getName() + " is no longer AFK.").broadcast();
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
        return getProxy().getAllowFlight() || getProxy().isFlying();
    }

    public void setFlying(boolean enabled) {
        getProxy().setAllowFlight(enabled);
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
        this.nickname = nickname + "§f";
        this.hasNickname = !nickname.equalsIgnoreCase(proxy.getName());
        getProxy().setDisplayName(nickname + "§f");
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
        File dataStore = new File(TrilliumAPI.getPlayerFolder(), proxy.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(dataStore);

        config.set(Configuration.Player.NICKNAME, this.nickname);
        config.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
        config.set(Configuration.Player.MUTED, this.isMuted);
        config.getBoolean(Configuration.Player.GOD, this.isGod);
        config.getBoolean(Configuration.Player.VANISH, this.isVanished);
        try {
            config.save(dataStore);
        } catch (IOException e) {
            e.printStackTrace();
        }

        proxy = null;
    }

    public void load() {
        boolean newUser = false;
        File dataStore = new File(TrilliumAPI.getPlayerFolder(), proxy.getUniqueId() + ".yml");
        if (!dataStore.exists()) {
            try {
                dataStore.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            newUser = true;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataStore);

        if (newUser) {
            config.set(Configuration.Player.NICKNAME, nickname);
            config.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
            config.set(Configuration.Player.MUTED, isMuted());
            config.set(Configuration.Player.GOD, isGod());
            config.set(Configuration.Player.PVP, canPvp());
            config.set(Configuration.Player.VANISH, isVanished);
            config.set(Configuration.Player.BAN_REASON, "");
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                config.set(Configuration.Player.GROUP, "default");
            }
        } else {
            setDisplayName(config.getString(Configuration.Player.NICKNAME));
            setLastLocation(TrilliumAPI.getSerializer(Location.class).deserialize(config.getString(Configuration.Player.LOCATION)));
            setMuted(config.getBoolean(Configuration.Player.MUTED));
            setGod(config.getBoolean(Configuration.Player.GOD));
            setPvp(config.getBoolean(Configuration.Player.PVP));
            setVanished(config.getBoolean(Configuration.Player.VANISH));
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                new GroupManager(getProxy()).setGroup(config.getString(Configuration.Player.GROUP));
            }
        }
        try {
            config.save(dataStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

