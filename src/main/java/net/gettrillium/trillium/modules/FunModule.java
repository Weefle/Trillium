package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.messageutils.Type;
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

    public FunModule() {
        super("fun");
    }

    @Command(command = "smite", description = "Strike lightning somewhere or upon someone.", usage = "/smite [player]", aliases = "thor")
    public void smite(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (cs.hasPermission(Permission.Fun.SMITE)) {
                if (args.length == 0) {
                    Location loc = p.getProxy().getTargetBlock(null, 100).getLocation();
                    p.getProxy().getWorld().strikeLightning(loc);
                } else {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        target.getProxy().getWorld().strikeLightning(target.getProxy().getLocation());
                        Message.message(Type.GENERIC, target.getProxy(), "Smite", true, p.getProxy().getName() + " stuck lightning upon you!");
                        Message.message(Type.GENERIC, p.getProxy(), "Smite", true, "You struck lightning upon " + target.getProxy().getName());

                    } else {
                        Message.error("Smite", cs, args[0]);
                    }
                }
            } else {
                Message.error("Smite", cs);
            }
        } else {
            Message.error("Smite", cs);
        }
    }

    //TODO: MASS compress kittybomb.

    @Command(command = "kittybomb", description = "KITTEHBAMB!!", usage = "/kb", aliases = "kittehbamb, kittehbomb, kittybamb, kittyb, kbomb, kb")
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
                Message.error("Kitty Bomb", cs);
            }
        } else {
            Message.error("Kitty Bomb", cs);
        }
    }

    public double randomV() {
        return Math.random() * 4 - 1;
    }

    public void throwcats(Location original, Player p) {
        final Ocelot cat1 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat2 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat3 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat4 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat5 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat6 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat7 = original.getWorld().spawn(original, Ocelot.class);
        final Ocelot cat8 = original.getWorld().spawn(original, Ocelot.class);

        cat1.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat2.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat3.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat4.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat5.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat6.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat7.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));
        cat8.setVelocity(new Vector(randomV(), randomV() + 1, randomV()));

        Random random = new Random();
        final int i = random.nextInt(Ocelot.Type.values().length);
        cat1.setCatType(Ocelot.Type.values()[i]);
        cat2.setCatType(Ocelot.Type.values()[i]);
        cat3.setCatType(Ocelot.Type.values()[i]);
        cat4.setCatType(Ocelot.Type.values()[i]);
        cat5.setCatType(Ocelot.Type.values()[i]);
        cat6.setCatType(Ocelot.Type.values()[i]);
        cat7.setCatType(Ocelot.Type.values()[i]);
        cat8.setCatType(Ocelot.Type.values()[i]);

        cat1.setTamed(true);
        cat2.setTamed(true);
        cat3.setTamed(true);
        cat4.setTamed(true);
        cat5.setTamed(true);
        cat6.setTamed(true);
        cat7.setTamed(true);
        cat8.setTamed(true);

        cat1.setBaby();
        cat2.setBaby();
        cat3.setBaby();
        cat4.setBaby();
        cat5.setBaby();
        cat6.setBaby();
        cat7.setBaby();
        cat8.setBaby();

        cat1.setOwner(p);
        cat2.setOwner(p);
        cat3.setOwner(p);
        cat4.setOwner(p);
        cat5.setOwner(p);
        cat6.setOwner(p);
        cat7.setOwner(p);
        cat8.setOwner(p);

        cat1.setBreed(false);
        cat2.setBreed(false);
        cat3.setBreed(false);
        cat4.setBreed(false);
        cat5.setBreed(false);
        cat6.setBreed(false);
        cat7.setBreed(false);
        cat8.setBreed(false);

        cat1.setSitting(true);
        cat2.setSitting(true);
        cat3.setSitting(true);
        cat4.setSitting(true);
        cat5.setSitting(true);
        cat6.setSitting(true);
        cat7.setSitting(true);
        cat8.setSitting(true);

        cat1.setAgeLock(true);
        cat2.setAgeLock(true);
        cat3.setAgeLock(true);
        cat4.setAgeLock(true);
        cat5.setAgeLock(true);
        cat6.setAgeLock(true);
        cat7.setAgeLock(true);
        cat8.setAgeLock(true);

        cat1.setAge(6000);
        cat2.setAge(6000);
        cat3.setAge(6000);
        cat4.setAge(6000);
        cat5.setAge(6000);
        cat6.setAge(6000);
        cat7.setAge(6000);
        cat8.setAge(6000);


        new BukkitRunnable() {
            int count = 20;

            public void run() {
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat1.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat2.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat3.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat4.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat5.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat6.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat7.getLocation(), 30);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 3, cat8.getLocation(), 30);

                if (count != 0) {
                    count--;
                } else {
                    cancel();
                    cat1.setHealth(0.0);
                    cat2.setHealth(0.0);
                    cat3.setHealth(0.0);
                    cat4.setHealth(0.0);
                    cat5.setHealth(0.0);
                    cat6.setHealth(0.0);
                    cat7.setHealth(0.0);
                    cat8.setHealth(0.0);
                }
            }
        }.runTaskTimer(TrilliumAPI.getInstance(), 1, 1);
    }
}