package me.lordsaad.trillium.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGamemode implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    if (p.hasPermission("tr.gamemode")) {

                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            p.sendMessage(ChatColor.BLUE + "Gamemode set to " + ChatColor.AQUA + "creative");

                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            p.sendMessage(ChatColor.BLUE + "Gamemode set to " + ChatColor.AQUA + "survival");

                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            p.sendMessage(ChatColor.BLUE + "Gamemode set to " + ChatColor.AQUA + "adventure");

                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                            p.sendMessage(ChatColor.BLUE + "Gamemode set to " + ChatColor.AQUA + "spectator");
                        } else {
                            p.sendMessage(ChatColor.RED + "Mojang didn't add that gamemode yet...");
                        }

                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }

                } else if (args.length > 1) {

                    if (p.hasPermission("tr.gamemode.other")) {
                        Player pl = Bukkit.getPlayer(args[1]);
                        if (pl != null) {

                            if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                                p.sendMessage(ChatColor.BLUE + pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "creative");
                                pl.sendMessage(ChatColor.BLUE + p.getName() + " set your gamemode to " + ChatColor.AQUA + "creative");

                            } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                                p.sendMessage(ChatColor.BLUE + pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "survival");
                                pl.sendMessage(ChatColor.BLUE + p.getName() + " set your gamemode to " + ChatColor.AQUA + "survival");

                            } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                                p.sendMessage(ChatColor.BLUE + pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "adventure");
                                pl.sendMessage(ChatColor.BLUE + p.getName() + " set your gamemode to " + ChatColor.AQUA + "adventure");

                            } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                                p.sendMessage(ChatColor.BLUE + pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "spectator");
                                pl.sendMessage(ChatColor.BLUE + p.getName() + " set your gamemode to " + ChatColor.AQUA + "spectator");

                            } else {
                                p.sendMessage(ChatColor.RED + "Mojang didn't add that gamemode yet...");
                            }

                        } else {
                            p.sendMessage(ChatColor.RED + args[1] + " is either not online or does not exist.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }

                } else {
                    if (p.hasPermission("tr.gamemode")) {
                        if (p.getGameMode() == GameMode.CREATIVE) {
                            p.setGameMode(GameMode.SURVIVAL);
                        } else {
                            p.setGameMode(GameMode.CREATIVE);
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