package net.gettrillium.trillium.api;

import net.gettrillium.trillium.api.SQL.SQL;
import net.gettrillium.trillium.api.events.PlayerAFKEvent;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    private Map<String, Location> homes = new HashMap<>();
    private boolean newUser;
    private boolean shadowBanned = false;
    private boolean shadowMuted = false;
    private boolean canBreakBlocks = true;
    private boolean canPlaceBlocks = true;
    private boolean canInteract = true;
    private boolean isFrozen = false;

    public TrilliumPlayer(Player proxy) {
        if (proxy != null) {
            this.proxy = proxy;
            nickname = proxy.getName();
            file = new File(TrilliumAPI.getPlayerFolder(), proxy.getUniqueId() + ".yml");
            if (file.exists()) {
                newUser = false;
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newUser = true;
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
        PlayerAFKEvent event = new PlayerAFKEvent(proxy, !afk);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            afk = !afk;
            if (afk) {
                new Message(Mood.NEUTRAL, "AFK", getName() + " is now AFK.").broadcast();
            } else {
                new Message(Mood.NEUTRAL, "AFK", getName() + " is no longer AFK.").broadcast();
            }
        }
    }

    public void active() {
        if (afk && !isVanished) {
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
        return proxy.getAllowFlight() || proxy.isFlying();
    }

    public void setFlying(boolean enabled) {
        proxy.setAllowFlight(enabled);
    }

    public boolean isGod() {
        return isGod;
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
        hasNickname = !nickname.equalsIgnoreCase(proxy.getName());
        proxy.setDisplayName(nickname);
    }

    public boolean hasNickname() {
        return hasNickname;
    }

    public boolean hasPermission(String permission) {
        return proxy.hasPermission(permission);
    }

    public boolean isVanished() {
        return isVanished;
    }

    public void setVanished(boolean enabled) {
        if (enabled) {
            isVanished = true;
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.proxy.hidePlayer(proxy);
            }
        } else {
            isVanished = false;
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.proxy.showPlayer(proxy);
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
        return shadowMuted;
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
        return canPlaceBlocks;
    }

    public void setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;
    }

    public boolean getCanInteract() {
        return canInteract;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
        if (frozen) {
            PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 9999, 9999, true, false);
            PotionEffect slow_break = new PotionEffect(PotionEffectType.SLOW_DIGGING, 9999, 9999, true, false);
            PotionEffect no_jump = new PotionEffect(PotionEffectType.JUMP, 9999, 9999, true, false);
            PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 9999, 9999, true, false);
            proxy.addPotionEffect(slow);
            proxy.addPotionEffect(slow_break);
            proxy.addPotionEffect(no_jump);
            proxy.addPotionEffect(blind);
        } else {
            proxy.removePotionEffect(PotionEffectType.BLINDNESS);
            proxy.removePotionEffect(PotionEffectType.SLOW_DIGGING);
            proxy.removePotionEffect(PotionEffectType.SLOW);
            proxy.removePotionEffect(PotionEffectType.JUMP);
        }
    }

    public void dispose() {
        if (!SQL.sqlEnabled()) {
            yml.set(Configuration.Player.NICKNAME, nickname);
            yml.set(Configuration.Player.LOCATION, LocationHandler.serialize(proxy.getLocation()));
            yml.set(Configuration.Player.MUTED, isMuted);
            yml.set(Configuration.Player.GOD, isGod);
            yml.set(Configuration.Player.VANISH, isVanished);

            ArrayList<String> serialized = new ArrayList<>();
            for (Entry<String, Location> row : homes.entrySet()) {
                serialized.add(row.getKey() + ";" + LocationHandler.serialize(row.getValue()));
            }
            yml.set(Configuration.Player.HOMES, serialized);

            save();

            proxy = null;
        } else {
            // TODO - proper try-with-resources here
            try {
                PreparedStatement ps = SQL.prepareStatement("UPDATE players SET " +
                        "(nick, loc-x, loc-y, loc-z, loc-world, muted, god, vanish)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?) WHERE uuid=?;");
                if (ps != null) {
                    ps.setString(1, nickname);
                    ps.setInt(2, proxy.getLocation().getBlockX());
                    ps.setInt(3, proxy.getLocation().getBlockY());
                    ps.setInt(4, proxy.getLocation().getBlockZ());
                    ps.setString(5, proxy.getLocation().getWorld().getName());
                    ps.setBoolean(6, isMuted());
                    ps.setBoolean(7, isGod());
                    ps.setBoolean(8, isVanished());
                    ps.setString(9, proxy.getUniqueId().toString());
                    ps.executeUpdate();
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {
        if (!SQL.sqlEnabled()) {
            if (newUser) {
                yml.set(Configuration.Player.NICKNAME, nickname);
                yml.set(Configuration.Player.LOCATION, LocationHandler.serialize(proxy.getLocation()));
                yml.set(Configuration.Player.MUTED, isMuted);
                yml.set(Configuration.Player.GOD, isGod);
                yml.set(Configuration.Player.PVP, pvp);
                yml.set(Configuration.Player.VANISH, isVanished);
                yml.set(Configuration.Player.BAN_REASON, "");
                yml.set(Configuration.Player.HOMES, "");
                save();
            } else {
                setDisplayName(yml.getString(Configuration.Player.NICKNAME));
                setLastLocation(LocationHandler.deserialize(yml.getString(Configuration.Player.LOCATION)));
                setMuted(yml.getBoolean(Configuration.Player.MUTED));
                setGod(yml.getBoolean(Configuration.Player.GOD));
                setPvp(yml.getBoolean(Configuration.Player.PVP));
                setVanished(yml.getBoolean(Configuration.Player.VANISH));

                List<String> serialized = yml.getStringList(Configuration.Player.HOMES);
                if (serialized != null) {
                    for (String deserialize : serialized) {
                        if (deserialize != null) {
                            homes.put(deserialize.split(";")[0], LocationHandler.deserialize(deserialize.split(";")[1]));
                        }
                    }
                }
            }
        } else {
            if (newUser) {
                try {
                    PreparedStatement ps = SQL.prepareStatement("INSERT INTO players " +
                            "(nick, loc-x, loc-y, loc-z, loc-world, muted, god, vanish)" +
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                    if (ps != null) {
                        ps.setString(1, nickname);
                        ps.setInt(2, proxy.getLocation().getBlockX());
                        ps.setInt(3, proxy.getLocation().getBlockY());
                        ps.setInt(4, proxy.getLocation().getBlockZ());
                        ps.setString(5, proxy.getLocation().getWorld().getName());
                        ps.setBoolean(6, isMuted());
                        ps.setBoolean(7, isGod());
                        ps.setBoolean(8, isVanished());
                        ps.setString(9, proxy.getUniqueId().toString());
                        ps.executeUpdate();
                        ps.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                List<String> serialized = yml.getStringList(Configuration.Player.HOMES);
                if (serialized != null) {
                    for (String deserialize : serialized) {
                        if (deserialize != null) {
                            homes.put(deserialize.split(";")[0], LocationHandler.deserialize(deserialize.split(";")[1]));
                        }
                    }
                }
            } else {
                try {
                    ResultSet result = SQL.executeQuery("SELECT * FROM players WHERE uuid=" + proxy.getUniqueId());
                    if (result != null) {
                        Location loc = new Location(Bukkit.getWorld(result.getString("loc-world")),
                                result.getInt("loc-x"), result.getInt("loc-y"), result.getInt("loc-z"));

                        setLastLocation(loc);
                        setDisplayName(result.getString("nick"));
                        setMuted(result.getBoolean("muted"));
                        setGod(result.getBoolean("god"));
                        setVanished(result.getBoolean("vanish"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                List<String> serialized = yml.getStringList(Configuration.Player.HOMES);
                if (serialized != null) {
                    for (String deserialize : serialized) {
                        if (deserialize != null) {
                            homes.put(deserialize.split(";")[0], LocationHandler.deserialize(deserialize.split(";")[1]));
                        }
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
        return proxy.getName();
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

    public List<Message> getHomeList() {
        ArrayList<Message> format = new ArrayList<>(homes.size());

        for (Entry<String, Location> row : homes.entrySet()) {
            format.add(new Message(Mood.NEUTRAL, row.getKey(), LocationHandler.toString(row.getValue())));
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

    public boolean isOnline() {
        return proxy.isOnline() && !isVanished();
    }

    public YamlConfiguration getConfig() {
        return yml;
    }

    public File getFile() {
        return file;
    }
}
