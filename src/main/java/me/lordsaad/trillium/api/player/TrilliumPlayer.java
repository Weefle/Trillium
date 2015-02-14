package me.lordsaad.trillium.api.player;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class TrilliumPlayer {
    private Player proxy;
    private Location previousLocation;
    private String nickname = proxy.getName();

    private long lastActive;
    private boolean afk;
    private boolean isMuted = false;
    private boolean isGod = false;
    private boolean isVanished = false;
    private boolean hasnickname = false;

    public TrilliumPlayer(Player proxy) {
        this.proxy = proxy;
        try {
            load();
        } catch (IOException e) {
            System.out.println("Failed to generate player: " + proxy.getName() + "'s files! Something went wrong.");
        }
    }

    public Player getProxy() {
        return proxy;
    }

    public boolean isAfk() {
        return afk;
    }

    public long getInactiveTime() {
        return System.currentTimeMillis() - lastActive;
    }

    public void toggleAfk() {
        this.afk = !this.afk;
        if (this.afk) {
            Message.b(MType.G, "AFK", getProxy().getName() + " is now AFK.");
        } else {
            Message.b(MType.G, "AFK", getProxy().getName() + " is no longer AFK.");
        }
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

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        getProxy().setDisplayName(proxy.getName());
        this.hasnickname = !nickname.equals(proxy.getName());
    }

    public boolean hasNickname() {
        return this.hasnickname;
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

    public boolean hasPermission(String permission) {
        return getProxy().hasPermission(permission);
    }

    public void active() {
        lastActive = System.currentTimeMillis();
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

    private void load() throws IOException {
        boolean newUser = false;
        File dataStore = new File(TrilliumAPI.getPlayerFolder(), proxy.getUniqueId() + ".yml");
        if (!dataStore.exists()) {
            dataStore.createNewFile();
            newUser = true;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataStore);

        if (newUser) {
            config.set(Configuration.Player.NICKNAME, nickname);
            config.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
            config.set(Configuration.Player.MUTED, isMuted());
            config.set(Configuration.Player.GOD, isGod());
            config.set(Configuration.Player.VANISH, isVanished);
            config.set(Configuration.Player.BAN_REASON, "");
        } else {
            setNickname(config.getString(Configuration.Player.NICKNAME));
            setLastLocation(TrilliumAPI.getSerializer(Location.class).deserialize(config.getString(Configuration.Player.LOCATION)));
            setMuted(config.getBoolean(Configuration.Player.MUTED));
            setGod(config.getBoolean(Configuration.Player.GOD));
            setVanished(config.getBoolean(Configuration.Player.VANISH));
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
}
