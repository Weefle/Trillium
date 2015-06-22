package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FunModule extends TrilliumModule {

    @Command(command = "kittybomb", description = "KITTEHBAMB!!", usage = "/kb", aliases = {"kittehbamb", "kittehbomb", "kittybamb", "kittyb", "kbomb", "kb"})
    public void kittybomb(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            final TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Fun.KITTYBOMB)) {
                final Ocelot cat = p.getProxy().getWorld().spawn(p.getProxy().getEyeLocation(), Ocelot.class);
                cat.setVelocity(p.getProxy().getLocation().getDirection().multiply(3));
                cat.setCatType(Ocelot.Type.BLACK_CAT);
                cat.setTamed(true);
                cat.setBaby();
                cat.setOwner(p.getProxy());
                cat.setSitting(true);
                cat.setBreed(false);
                cat.setAgeLock(true);
                cat.setAge(6000);
                p.getProxy().getWorld().playSound(p.getProxy().getLocation(), Sound.CAT_MEOW, 10, 1);

                new BukkitRunnable() {
                    int count = 50;

                    public void run() {
                        ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat.getLocation(), 30);
                        if (count != 0) {
                            count--;
                        } else {
                            cancel();
                            cat.setHealth(0.0);
                            ParticleEffect.EXPLOSION_LARGE.display((float) 1.5, (float) 1.5, (float) 1.5, (float) 0, 3, cat.getLocation(), 30);
                            ParticleEffect.SMOKE_LARGE.display((float) 1.5, (float) 1.5, (float) 1.5, (float) 0, 3, cat.getLocation(), 30);
                            p.getProxy().playSound(p.getProxy().getLocation(), Sound.EXPLODE, 10, 1);
                            Utils.throwCats(cat.getLocation(), p.getProxy());
                        }
                    }
                }.runTaskTimer(TrilliumAPI.getInstance(), 1, 1);

            } else {
                new Message("Kitty Bomb", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Kitty Bomb", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "sudo", description = "Make a player forcefully run a certain command.", usage = "/sudo <player> <command>", aliases = {"pseudo"})
    public void sudo(CommandSender cs, String[] args) {
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
                    new Message(Mood.GOOD, "Sudo", "Successfully ran '" + command + "' on " + target.getName()).to(cs);
                } else {
                    new Message("Sudo", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message("Sudo", Error.TOO_FEW_ARGUMENTS, "/sudo <player> <command>").to(cs);
            }
        } else {
            new Message("Sudo", Error.NO_PERMISSION).to(cs);
        }
    }
}