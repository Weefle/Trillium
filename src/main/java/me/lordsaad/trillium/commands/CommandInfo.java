package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CommandInfo implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("information")) {
            if (sender.hasPermission("tr.info")) {
                if (args.length == 0) {
                    Message.earg(sender, "Info", "/info <player>");
                } else {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        Message.m(MType.G, sender, "Info", "Displaying Information on: " + p.getName());
                        Message.m(MType.G, sender, "Info", "Online: " + online(p));
                        Message.m(MType.G, sender, "Info", "NickName: " + nickname(p));
                        Message.m(MType.G, sender, "Info", "Banned: " + banned(p));
                        if (p.isBanned()) {
                            Message.m(MType.G, sender, "Info", "Ban Reason: potato");
                        }
                        Message.m(MType.G, sender, "Info", "Muted: " + muted(p));
                        Message.m(MType.G, sender, "Info", "Location: " + location(p));
                        Message.m(MType.G, sender, "Info", "Last found at: " + lastlocation(p));
                        Message.m(MType.G, sender, "Info", "Ping:" + ping(p));
                        Message.m(MType.G, sender, "Info", "Lag Rate: " + lag(p));
                        Message.m(MType.G, sender, "Info", "Food level: " + food(p));
                        Message.m(MType.G, sender, "Info", "Health level: " + health(p));
                        Message.m(MType.G, sender, "Info", "Time Played: hours: " + (timeplayed(p) / 60) / 60);
                        Message.m(MType.G, sender, "Info", "Time Played: days: " + ((timeplayed(p) / 60) / 60) / 24);
                    } else {
                        Message.eplayer(sender, "Info", args[0]);
                    }
                }
            } else {
                Message.e(sender, "Info", Crit.P);
            }
        }
        return true;
    }

    public String online(Player p) {
        if (p.isOnline()) {
            return ChatColor.GREEN + "true";
        } else {
            return ChatColor.RED + "false";
        }
    }

    public String banned(Player p) {
        if (p.isBanned()) {
            return ChatColor.GREEN + "true";
        } else {
            return ChatColor.RED + "false";
        }
    }

    public String location(Player p) {
        if (p.isOnline()) {
            Location loc = p.getLocation();
            return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + p.getWorld().getName();
        } else {
            return "null";
        }
    }

    public String ping(Player p) {
        if (p.isOnline()) {
            int ping = ((CraftPlayer) p).getHandle().ping;
            return " " + ping;
        } else {
            return "null";
        }
    }

    public String lag(Player p) {
        if (p.isOnline()) {
            int ping = ((CraftPlayer) p).getHandle().ping;
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.DARK_GRAY + "[");

            if (ping < 50) {
                sb.append(ChatColor.GREEN + "");
            } else if (ping < 250) {
                sb.append(ChatColor.YELLOW + "");
            } else {
                sb.append(ChatColor.RED + "");
            }
            for (int i = 0; i < 30; i++) {
                if (i * 20 < ping) {
                    sb.append("|");
                } else {
                    if (i * 20 < ping + 20) {
                        sb.append(ChatColor.DARK_GRAY);
                    }

                    sb.append("|");
                }
            }
            sb.append("]");
            return sb.toString();
        } else {
            return "null";
        }
    }

    public String food(Player p) {
        if (p.isOnline()) {
            int food = p.getFoodLevel();
            return "" + food;
        } else {
            return "null";
        }
    }

    public String health(Player p) {
        if (p.isOnline()) {
            double health = p.getHealthScale();
            return "" + health;
        } else {
            return "null";
        }
    }

    public String gm(Player p) {
        if (p.isOnline()) {
            if (p.getGameMode() == GameMode.SURVIVAL) {
                return "Survival mode";
            } else if (p.getGameMode() == GameMode.CREATIVE) {
                return "Creative mode";
            } else if (p.getGameMode() == GameMode.ADVENTURE) {
                return "Adventure mode";
            } else {
                return "Spectator mode";
            }
        } else {
            return "null";
        }
    }

    public int timeplayed(Player p) {
        return p.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
    }

    public String lastlocation(Player p) {
        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        int x = pdb.getInt("Last Location.x");
        int y = pdb.getInt("Last Location.y");
        int z = pdb.getInt("Last Location.z");
        String world = pdb.getString("Last Location.world");
        return world + ", " + x + ", " + y + ", " + z;
    }

    public String nickname(Player p) {
        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        return pdb.getString("Nickname");
    }

    public String muted(Player p) {
        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
        if (pdb.getBoolean("Muted")) {
            return ChatColor.GREEN + "True";
        } else {
            return ChatColor.RED + "False";
        }
    }
}