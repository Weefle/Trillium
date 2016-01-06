package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class FunModule extends TrilliumModule {

    @Command(name = "Smite",
            command = "smite",
            description = "Strike lightning somewhere or upon someone.",
            usage = "/smite [player]",
            aliases = "thor",
            permissions = {Permission.Fun.SMITE})
    public void smite(CommandSender cs, String[] args) {
        String cmd = "smite";
        if (!(cs instanceof Player)) {
            new Message(TrilliumAPI.getUsage(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        Player p = (Player) cs;

        if (!cs.hasPermission(Permission.Fun.SMITE)) {
            new Message(TrilliumAPI.getUsage(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            return;
        }

        if (args.length == 0) {

            Set<Material> set = new HashSet<>();
            set.add(Material.AIR);
            set.add(Material.WATER);
            set.add(Material.STATIONARY_WATER);
            set.add(Material.LAVA);
            set.add(Material.STATIONARY_LAVA);
            Location loc = p.getTargetBlock(set, 100).getLocation();
            p.getWorld().strikeLightning(loc);

        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                p.getWorld().strikeLightning(target.getLocation());
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), p.getName() + " has struck lightning upon you!").to(target);
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You struck lightning upon " + target.getName()).to(p);

            } else {
                new Message(TrilliumAPI.getUsage(cmd), Error.INVALID_PLAYER, args[0]).to(p);
            }
        }
    }

    @Command(name = "Sudo",
            command = "sudo",
            description = "Make a player forcefully run a certain command.",
            usage = "/sudo <player> <command>",
            aliases = {"pseudo"},
            permissions = {Permission.Fun.SUDO})
    public void sudo(CommandSender cs, String[] args) {
        String cmd = "sudo";
        if (cs.hasPermission(Permission.Fun.SUDO)) {
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String command = sb.toString().trim();

                    Bukkit.dispatchCommand(target, command);
                    new Message(Mood.GOOD, TrilliumAPI.getUsage(cmd), "Successfully ran '" + command + "' on " + target.getName()).to(cs);
                } else {
                    new Message(TrilliumAPI.getUsage(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message(TrilliumAPI.getUsage(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(cs);
            }
        } else {
            new Message(TrilliumAPI.getUsage(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
        }
    }
}