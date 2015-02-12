package me.lordsaad.trillium.api.player;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class TrilliumPlayer extends TrilliumModule {
    private Player proxy;

    private Location previousLocation;

    private boolean afk;
    private long lastActive;

    private boolean isMuted = false;

    private boolean isGod = false;
    private boolean isVanished = false;

    public TrilliumPlayer(Player proxy) {
        this.proxy = proxy;
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Warn
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

    public void setMuted(Boolean enabled) {
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

    public boolean isVanished() {
        return this.isVanished;
    }

    public void setVanished(boolean enabled) {
        if (enabled) {
            this.isVanished = true;
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.getProxy().hidePlayer(getProxy());
            }
            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                getProxy().setGameMode(GameMode.SPECTATOR);
            }
        } else {
            this.isVanished = false;
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.getProxy().showPlayer(getProxy());
            }
            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                getProxy().setGameMode(GameMode.SURVIVAL);
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
        proxy = null;
        //TODO: Save data
    }

    private void load() throws IOException {
        boolean newUser = false;
        File dataStore = new File(TrilliumAPI.getInstance().getDataFolder() + File.separator + "players" + File.separator + proxy.getUniqueId() + ".yml");
        if (!dataStore.exists()) {
            dataStore.createNewFile();
            newUser = true;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataStore);

        if (newUser) {
            config.set(Configuration.Player.NICKNAME, proxy.getName());
            config.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
            config.set(Configuration.Player.MUTED, false);
            config.set(Configuration.Player.GOD, false);
            config.set(Configuration.Player.VANISH, false);
            config.set(Configuration.Player.BAN_REASON, "");
        }
    }

    public Location getLastLocation() {
        return this.previousLocation;
    }
}
