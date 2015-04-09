package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.particleeffect.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

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
                    int count = 30;

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
                            throwcats(cat.getLocation(), p.getProxy());
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

    public double randomV() {
        return Math.random() * 4 - 1;
    }

    public void throwcats(Location original, Player p) {

        for (int cats = 1; cats < 8; cats++) {
            final Ocelot cat = original.getWorld().spawn(original, Ocelot.class);
            cat.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
            Random random = new Random();
            int i = random.nextInt(Ocelot.Type.values().length);
            cat.setCatType(Ocelot.Type.values()[i]);
            cat.setTamed(true);
            cat.setBaby();
            cat.setOwner(p);
            cat.setBreed(false);
            cat.setSitting(true);
            cat.setAgeLock(true);
            cat.setAge(6000);
        }
    }
}