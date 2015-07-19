package net.gettrillium.trillium.api;

import net.gettrillium.trillium.api.events.PlayerAFKEvent;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HashMap<String, Location> homes = new HashMap<>();
    private boolean newUser;
    private boolean shadowBanned;
    private boolean shadowMuted;
    private boolean canBreakBlocks;
    private boolean canPlaceBlocks;
    private boolean canInteract;

    public TrilliumPlayer(Player proxy) {
        if (proxy != null) {
            this.proxy = proxy;
            nickname = proxy.getName();
            file = new File(TrilliumAPI.getPlayerFolder(), proxy.getUniqueId() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newUser = true;
            } else {
                newUser = false;
            }
            yml = YamlConfiguration.loadConfiguration(file);
            load();
        }
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
                new Message(Mood.NEUTRAL, "AFK", getName() + " is now AFK.").broadcast();
            } else {
                new Message(Mood.NEUTRAL, "AFK", getName() + " is no longer AFK.").broadcast();
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
        return lastActive;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean enabled) {
        isMuted = enabled;
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
        if (enabled) {
            proxy.setHealth(20.0);
            proxy.setFoodLevel(20);
            isGod = true;
        }
        isGod = enabled;
    }

    public String getDisplayName() {
        return nickname;
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

    public void setShadowBanned(boolean shadowBanned) {
        this.shadowBanned = shadowBanned;
        setVanished(shadowBanned);
        setMuted(shadowBanned);
        setCanBreakBlocks(!shadowBanned);
        setCanPlaceBlocks(!shadowBanned);
        setCanInteract(!shadowBanned);
        setGod(shadowBanned);
    }

    public boolean isShadowBanned() {
        return shadowBanned;
    }

    public void setShadowMuted(boolean shadowMuted) {
        this.shadowMuted = shadowMuted;
        setMuted(shadowMuted);
    }

    public boolean isShadowMuted() {
        return this.shadowMuted;
    }

    public void setCanBreakBlocks(boolean canBreakBlocks) {
        this.canBreakBlocks = canBreakBlocks;
    }

    public boolean getCanBreakBlocks() {
        return canBreakBlocks;
    }

    public void setCanPlaceBlocks(boolean canPlaceBlocks) {
        this.canPlaceBlocks = canPlaceBlocks;
    }

    public boolean getCanPlaceBlocks() {
        return this.canPlaceBlocks;
    }

    public void setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;
    }

    public boolean getCanInteract() {
        return this.canInteract;
    }

    public void dispose() {
        yml.set(Configuration.Player.NICKNAME, this.nickname);
        yml.set(Configuration.Player.LOCATION, Utils.locationSerializer(proxy.getLocation()));
        yml.set(Configuration.Player.MUTED, this.isMuted);
        yml.set(Configuration.Player.GOD, this.isGod);
        yml.set(Configuration.Player.VANISH, this.isVanished);

        ArrayList<String> serialized = new ArrayList<>();
        for (Map.Entry<String, Location> row : homes.entrySet()) {
            serialized.add(row.getKey() + ";" + Utils.locationToString(row.getValue()));
        }
        yml.set(Configuration.Player.HOMES, serialized);

        save();

        proxy = null;
    }

    public void load() {

        if (newUser) {
            yml.set(Configuration.Player.NICKNAME, getDisplayName());
            yml.set(Configuration.Player.LOCATION, Utils.locationSerializer(proxy.getLocation()));
            yml.set(Configuration.Player.MUTED, isMuted());
            yml.set(Configuration.Player.GOD, isGod());
            yml.set(Configuration.Player.PVP, canPvp());
            yml.set(Configuration.Player.VANISH, isVanished());
            yml.set(Configuration.Player.BAN_REASON, "");
            yml.set(Configuration.Player.HOMES, "");
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                yml.set(Configuration.Player.GROUP, "default");
            }
            save();
        } else {
            setDisplayName(yml.getString(Configuration.Player.NICKNAME));
            setLastLocation(Utils.locationDeserializer(yml.getString(Configuration.Player.LOCATION)));
            setMuted(yml.getBoolean(Configuration.Player.MUTED));
            setGod(yml.getBoolean(Configuration.Player.GOD));
            setPvp(yml.getBoolean(Configuration.Player.PVP));
            setVanished(yml.getBoolean(Configuration.Player.VANISH));
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                new GroupManager(getProxy()).setGroup(yml.getString(Configuration.Player.GROUP));
            }

            List<String> serialized = yml.getStringList(Configuration.Player.HOMES);
            if (serialized != null) {
                for (String deserialize : serialized) {
                    if (deserialize != null) {
                        homes.put(deserialize.split(";")[0], Utils.locationFromString(deserialize.split(";")[1]));
                    }
                }
            }
        }
    }

    public Location getLastLocation() {
        return previousLocation;
    }

    public void setLastLocation(Location loc) {
        previousLocation = loc;
    }

    public void setPvp(boolean b) {
        pvp = b;
    }

    public boolean canPvp() {
        return pvp;
    }

    public String getName() {
        return getProxy().getName();
    }

    public boolean homeIsNotNull(String name) {
        return homes.get(name) != null;
    }

    public void setHome(String name, Location loc) {
        homes.put(name, loc);
        save();
    }

    public void delHome(String name) {
        homes.remove(name);
        save();
    }

    public Location getHomeLocation(String name) {
        return homes.get(name);
    }

    public ArrayList<Message> getHomeList() {
        ArrayList<Message> format = new ArrayList<>();

        for (Map.Entry<String, Location> row : homes.entrySet()) {
            format.add(new Message(Mood.NEUTRAL, row.getKey(), Utils.locationToString(row.getValue())));
        }
        return format;
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

    public YamlConfiguration getConfig() {
        return yml;
    }

    public File getFile() {
        return file;
    }
}

