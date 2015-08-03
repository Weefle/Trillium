package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.cooldown.Cooldown;
import net.gettrillium.trillium.api.cooldown.CooldownType;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitModule extends TrilliumModule {

    @Command(name = "Kit",
            command = "kit",
            description = "Get a certain kit.",
            usage = "/kit [kit name]",
            permissions = {Permission.Kit.USE, Permission.Kit.COOLDOWN_EXEMPT, Permission.Kit.GIVE})
    public void kit(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 0) {
                new Message(Mood.NEUTRAL, "Kit", "Available kits:").to(p);
                for (String s : getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER).getKeys(false)) {
                    new Message(Mood.NEUTRAL, "Kit", s).to(p);
                }
            } else if (args.length == 1) {
                if (getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER).contains(args[0])) {
                    if (p.getProxy().hasPermission(Permission.Kit.USE + args[0])) {
                        if (!Cooldown.hasCooldown(p.getProxy(), CooldownType.KIT)) {
                            if (!p.getProxy().isOp()
                                    && !p.hasPermission(Permission.Kit.COOLDOWN_EXEMPT)
                                    && !p.hasPermission(Permission.Kit.GIVE)) {
                                Cooldown.setCooldown(p.getProxy(), CooldownType.KIT, true);
                            }

                            new Kit(args[0]).giveTo(p.getProxy());
                            new Message(Mood.GOOD, "Kit", "You successfully received kit " + args[0]).to(p);

                        } else {
                            new Message(Mood.BAD, "Kit", "Cooldown is still active: " + ChatColor.AQUA + Cooldown.getTime(p.getProxy(), CooldownType.KIT)).to(p);
                        }
                    } else {
                        new Message(Mood.BAD, "Kit", "You don't have permission to use that kit.").to(p);
                    }
                } else {
                    new Message(Mood.BAD, "Kit", args[0] + " is not a valid kit.").to(p);
                }
            } else {
                if (p.getProxy().hasPermission(Permission.Kit.GIVE)) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        new Kit(args[0]).giveTo(target);
                        new Message(Mood.GOOD, "Kit", target.getName() + " has successfully received kit " + args[1]).to(p);
                        new Message(Mood.GOOD, "Kit", p.getName() + " gave you kit " + args[1]).to(target);
                    } else {
                        new Message("Kit", Error.INVALID_PLAYER).to(p);
                    }
                } else {
                    new Message(Mood.BAD, "Kit", "Your have reached your /kit limits.").to(p);
                }
            }
        } else {
            new Message("Kit", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }
}
