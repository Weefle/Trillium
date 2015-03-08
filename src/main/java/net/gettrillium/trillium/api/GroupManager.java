package net.gettrillium.trillium.api;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GroupManager {

    private HashMap<UUID, PermissionAttachment> attachmentlist = new HashMap<>();
    private String group = "default";
    private Player p;

    public GroupManager(Player p) {
        this.p = p;
    }

    public void addAttachment() {
        if (!hasAttachment()) {
            PermissionAttachment attachment = p.addAttachment(TrilliumAPI.getInstance());
            this.attachmentlist.put(p.getUniqueId(), attachment);
        }
    }

    public void removeAttachment() {
        if (hasAttachment()) {
            this.attachmentlist.remove(p.getUniqueId());
        }
    }

    public boolean hasAttachment() {
        return this.attachmentlist.containsKey(p.getUniqueId());
    }

    public void addPermission(String perm) {
        if (hasAttachment()) {
            PermissionAttachment attachment = this.attachmentlist.get(p.getUniqueId());
            attachment.setPermission(perm, true);
        }
    }

    public void addPermissionPlayer(String perm) {
        if (hasAttachment()) {
            File player = new File(TrilliumAPI.getInstance().getDataFolder() + "/Trillium Group Manager/players/" + p.getName() + ".yml");
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

            File group = new File(TrilliumAPI.getInstance().getDataFolder(), "/Trillium Group Manager/worlds/" + p.getWorld().getName() + ".yml");
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
            PermissionAttachment attachment = this.attachmentlist.get(p.getUniqueId());
            attachment.unsetPermission(perm);
        }
    }

    public void removePermissionPlayer(String perm) {
        if (hasAttachment()) {
            File player = new File(TrilliumAPI.getInstance().getDataFolder() + "/Trillium Group Manager/players/" + p.getName() + ".yml");
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

            File group = new File(TrilliumAPI.getInstance().getDataFolder(), "/Trillium Group Manager/worlds/" + p.getWorld().getName() + ".yml");
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
                    if (f.getName().equalsIgnoreCase(p.getName())) {
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
                    if (f.getName().equals(p.getWorld().getName())) {
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
        for (String key : yml.getStringList(group)) {
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
}
