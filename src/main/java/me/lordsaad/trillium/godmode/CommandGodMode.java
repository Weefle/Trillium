package me.lordsaad.trillium.godmode;

import me.lordsaad.trillium.PlayerDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CommandGodMode implements CommandExecutor {

    public static ArrayList<UUID> godmodeusers = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("god")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length != 0) {
                    if (p.hasPermission("tr.god")) {

                        YamlConfiguration yml = null;
                        try {
                            yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (godmodeusers.contains(p.getUniqueId())) {

                            godmodeusers.remove(p.getUniqueId());
                            p.sendMessage(ChatColor.BLUE + "You are no longer in god mode.");
                            if (yml != null) {
                                yml.set("God Mode", false);
                            }

                            try {
                                if (yml != null) {
                                    yml.save(PlayerDatabase.db(p));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            godmodeusers.add(p.getUniqueId());

                            p.sendMessage(ChatColor.BLUE + "You are now in god mode.");
                            if (yml != null) {
                                yml.set("God Mode", true);
                            }

                            try {
                                if (yml != null) {
                                    yml.save(PlayerDatabase.db(p));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }

                } else {
                    if (p.hasPermission("tr.god")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        if (pl != null) {

                            YamlConfiguration yml = null;
                            try {
                                yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (godmodeusers.contains(p.getUniqueId())) {

                                godmodeusers.remove(p.getUniqueId());
                                p.sendMessage(ChatColor.BLUE + "You are no longer in god mode.");
                                if (yml != null) {
                                    yml.set("God Mode", false);
                                }

                                try {
                                    if (yml != null) {
                                        yml.save(PlayerDatabase.db(p));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                godmodeusers.add(p.getUniqueId());

                                p.sendMessage(ChatColor.BLUE + "You are now in god mode.");
                                if (yml != null) {
                                    yml.set("God Mode", true);
                                }

                                try {
                                    if (yml != null) {
                                        yml.save(PlayerDatabase.db(p));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + args[0] + " is either not online or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }

                }
            } else {
                sender.sendMessage(ChatColor.RED + "You can't do that.");
            }
        }

        return true;
    }
}