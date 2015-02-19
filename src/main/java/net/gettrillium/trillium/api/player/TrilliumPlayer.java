package net.gettrillium.trillium.api.player;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TrilliumPlayer {

    private HashMap<UUID, PermissionAttachment> attachmentlist = new HashMap<>();

    private Player proxy;
    private Location previousLocation;
    private String nickname = proxy.getName();

    private long lastActive;
    private boolean afk;
    private boolean isMuted = false;
    private boolean isGod = false;
    private boolean isVanished = false;
    private boolean hasnickname = false;
    private String group = "default";
    private boolean pvp;

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
            config.set(Configuration.Player.PVP, canPvp());
            config.set(Configuration.Player.VANISH, isVanished);
            config.set(Configuration.Player.BAN_REASON, "");
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                config.set(Configuration.Player.GROUP, "default");
            }
        } else {
            setNickname(config.getString(Configuration.Player.NICKNAME));
            setLastLocation(TrilliumAPI.getSerializer(Location.class).deserialize(config.getString(Configuration.Player.LOCATION)));
            setMuted(config.getBoolean(Configuration.Player.MUTED));
            setGod(config.getBoolean(Configuration.Player.GOD));
            setPvp(config.getBoolean(Configuration.Player.PVP));
            setVanished(config.getBoolean(Configuration.Player.VANISH));
            if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
                setGroup(config.getString(Configuration.Player.GROUP));
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

    public void addAttachment() {
        if (!hasAttachment()) {
            PermissionAttachment attachment = proxy.addAttachment(TrilliumAPI.getInstance());
            this.attachmentlist.put(this.proxy.getUniqueId(), attachment);
        }
    }

    public void removeAttachment() {
        if (hasAttachment()) {
            this.attachmentlist.remove(this.proxy.getUniqueId());
        }
    }

    public boolean hasAttachment() {
        return this.attachmentlist.containsKey(this.proxy.getUniqueId());
    }

    public void addPermission(String perm) {
        if (hasAttachment()) {
            PermissionAttachment attachment = this.attachmentlist.get(this.proxy.getUniqueId());
            attachment.setPermission(perm, true);
        }
    }

    public void addPermissionPlayer(String perm) {
        if (hasAttachment()) {
            File player = new File(TrilliumAPI.getInstance().getDataFolder() + "/Trillium Group Manager/players/" + getProxy().getName() + ".yml");
            if (!player.exists()) {
                try {
                    player.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(player);
            List<String> perms = yml.getStringList("permissions");
            perms.add(perm);
            yml.set("permissions", perms);

        }
    }

    public void addPermissionGroup(String perm) {
        if (hasAttachment()) {

            File group = new File(TrilliumAPI.getInstance().getDataFolder(), "/Trillium Group Manager/worlds/" + getProxy().getWorld().getName() + ".yml");
            if (!group.exists()) {
                try {
                    group.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(group);
            List<String> perms = yml.getStringList(getGroup() + "permissions");
            perms.add(perm);
            yml.set("permissions", perms);
        }
    }

    public void removePermission(String perm) {
        if (hasAttachment()) {
            PermissionAttachment attachment = this.attachmentlist.get(this.proxy.getUniqueId());
            attachment.unsetPermission(perm);
        }
    }

    public void removePermissionPlayer(String perm) {
        if (hasAttachment()) {
            File player = new File(TrilliumAPI.getInstance().getDataFolder() + "/Trillium Group Manager/players/" + getProxy().getName() + ".yml");
            if (!player.exists()) {
                try {
                    player.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            YamlConfiguration yml = YamlConfiguration.loadConfiguration(player);
            List<String> perms = yml.getStringList("permissions");
            if (perms.contains(perm)) {
                perms.remove(perm);
                yml.set("permissions", perms);
            } else {
                perms.add("-" + perm);
                yml.set("permissions", perms);
            }
        }
    }

    public void removePermissionGroup(String perm) {
        if (hasAttachment()) {

            File group = new File(TrilliumAPI.getInstance().getDataFolder(), "/Trillium Group Manager/worlds/" + getProxy().getWorld().getName() + ".yml");
            if (!group.exists()) {
                try {
                    group.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            YamlConfiguration yml = YamlConfiguration.loadConfiguration(group);
            List<String> perms = yml.getStringList("permissions");
            if (perms.contains(perm)) {
                perms.remove(perm);
                yml.set("permissions", perms);
            } else {
                perms.add("-" + perm);
                yml.set("permissions", perms);
            }
        }
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void refreshPermissions() {
        if (hasAttachment()) {

            removeAttachment();

            addAttachment();

            File players = new File(TrilliumAPI.getInstance().getDataFolder() + "/Trillium Group Manager/players");
            File worlds = new File(TrilliumAPI.getInstance().getDataFolder() + "/Trillium Group Manager/worlds");

            for (File f : players.listFiles()) {
                if (f != null && f.isFile()) {
                    if (f.getName().equalsIgnoreCase(getProxy().getName())) {
                        YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
                        for (String perms : yml.getStringList("permissions")) {
                            if (!perms.contains("-")) {
                                addPermission(perms);
                            }
                        }
                    }
                }
            }

            for (File f : worlds.listFiles()) {
                if (f != null && f.isFile()) {
                    if (f.getName().equals(this.proxy.getWorld().getName())) {
                        for (String perms : getPermissions(f)) {
                            if (!perms.contains("-")) {
                                addPermission(perms);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<String> getheritage(YamlConfiguration yml, String group) {
        List<String> perms = new ArrayList<>();
        for (String key : yml.getConfigurationSection(group).getKeys(false)) {
            if (key != null) {
                if (key.equals("inherit")) {
                    for (String perm : getheritage(yml, key)) {
                        if (!perms.contains("-")) {
                            perms.add(perm);
                        }
                    }
                } else if (key.equals("permissions")) {
                    for (String perm : yml.getStringList(group + ".permissions")) {
                        if (!perms.contains("-")) {
                            perms.add(perm);
                        }
                    }
                }
            }
        }
        return perms;
    }

    public List<String> getPermissions(File f) {
        List<String> perms = new ArrayList<>();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        for (String key : yml.getConfigurationSection(getGroup()).getKeys(false)) {
            if (key != null) {
                if (key.equals("inherit")) {
                    for (String perm : getheritage(yml, key)) {
                        if (!perms.contains("-")) {
                            perms.add(perm);
                        }
                    }
                } else if (key.equals("permissions")) {
                    for (String perm : yml.getStringList(group + ".permissions")) {
                        if (!perms.contains("-")) {
                            perms.add(perm);
                        }
                    }
                }
            }
        }
        return perms;
    }

    public void setPvp(boolean b) {
        this.pvp = b;
    }

    public boolean canPvp() {
        return this.pvp;
    }
}

