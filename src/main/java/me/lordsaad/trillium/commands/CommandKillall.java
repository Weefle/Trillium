package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.List;

public class CommandKillall implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("killall")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.killall")) {
                    if (args.length <= 1) {
                        Message.earg(p, "Killall", "/killall <radius> <mobs/players/animals/monsters/items/everything>");
                    } else {
                        if (API.isDouble(args[0]) || API.isInt(args[0])) {
                            List<Entity> entities = p.getNearbyEntities(Double.parseDouble(args[0]), Double.parseDouble(args[0]), Double.parseDouble(args[0]));
                            if (args[1].equalsIgnoreCase("mobs")
                                    || args[1].equalsIgnoreCase("animals")
                                    || args[1].equalsIgnoreCase("players")
                                    || args[1].equalsIgnoreCase("monsters")) {
                                Message.m(MType.G, p, "Killall", "Successfully murdered all " + args[1] + " in a radius of " + args[0]);
                            } else if (args[1].equalsIgnoreCase("items")) {
                                Message.m(MType.G, p, "Killall", "Successfully destroyed all " + args[1] + " in a radius of " + args[0]);
                            } else if (args[1].equalsIgnoreCase("everything")) {
                                Message.m(MType.G, p, "Killall", "Successfully destroyed and murdered everything... you monster...");
                            } else {
                                p.sendMessage("");
                            }
                            for (Entity e : entities) {
                                if (args[1].equalsIgnoreCase("mobs")) {
                                    if (e instanceof Monster || e instanceof Animals) {
                                        ((LivingEntity) e).setHealth(0D);
                                    }
                                } else if (args[1].equalsIgnoreCase("monsters")) {
                                    if (e instanceof Monster) {
                                        ((LivingEntity) e).setHealth(0D);
                                    }
                                } else if (args[1].equalsIgnoreCase("animals")) {
                                    if (e instanceof Animals) {
                                        ((LivingEntity) e).setHealth(0D);
                                    }
                                } else if (args[1].equalsIgnoreCase("players")) {
                                    if (e instanceof Player) {
                                        ((LivingEntity) e).setHealth(0D);
                                    }
                                } else if (args[1].equalsIgnoreCase("items")) {
                                    if (e instanceof Item) {
                                        e.remove();
                                    }
                                } else if (args[1].equalsIgnoreCase("everything")) {
                                    if (e instanceof Damageable) {
                                        ((Damageable) e).setHealth(0D);
                                    } else {
                                        e.remove();
                                    }
                                } else {
                                    Message.m(MType.W, p, "Killall", "Unknown argument: " + args[1]);
                                }
                            }
                        } else {
                            Message.m(MType.W, p, "Killall", args[0] + " is not a number.");
                        }
                    }
                } else {
                    Message.e(p, "Killall", Crit.P);
                }
            } else {
                Message.e(sender, "Killall", Crit.C);
            }
        }
        return true;
    }
}