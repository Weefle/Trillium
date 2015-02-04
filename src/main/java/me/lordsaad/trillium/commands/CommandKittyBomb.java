package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.Main;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;
import me.lordsaad.trillium.particleeffect.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class CommandKittyBomb implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kittybomb")) {
            if (sender instanceof Player) {
                final Player p = (Player) sender;
                if (p.hasPermission("saad.kittybomb")) {
                    final Ocelot cat = p.getWorld().spawn(p.getEyeLocation(), Ocelot.class);
                    cat.setVelocity(p.getLocation().getDirection().multiply(3));
                    cat.setCatType(Ocelot.Type.BLACK_CAT);
                    cat.setTamed(true);
                    cat.setBaby();
                    cat.setOwner(p);
                    cat.setSitting(true);
                    cat.setBreed(false);
                    cat.setAgeLock(true);
                    cat.setAge(6000);
                    p.getWorld().playSound(p.getLocation(), Sound.CAT_MEOW, 10, 1);
                    
                    new BukkitRunnable() {
                        int count = 30;

                        public void run() {
                            ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat.getLocation(), 10);
                            if (count != 0) {
                                count--;
                            } else {
                                cancel();
                                cat.setHealth(0.0);
                                ParticleEffect.EXPLOSION_LARGE.display((float) 1.5, (float) 1.5, (float) 1.5, (float) 0, 10, cat.getLocation(), 10);
                                ParticleEffect.SMOKE_LARGE.display((float) 1.5, (float) 1.5, (float) 1.5, (float) 0, 10, cat.getLocation(), 10);
                                p.playSound(p.getLocation(), Sound.EXPLODE, 10, 1);
                                throwcats(cat.getLocation(), p);
                            }
                        }
                    }.runTaskTimer(Main.plugin, 1, 1);
                    
                } else {
                    Message.e(p, "Kitty Bomb", Crit.P);
                }
            } else {
                Message.e(sender, "Kitty Bomb", Crit.C);
            }
        }
        return true;
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
        cat1.setTamed(true);
        cat1.setBaby();
        cat1.setOwner(p);
        cat1.setSitting(true);
        cat1.setBreed(false);
        cat2.setCatType(Ocelot.Type.values()[i]);
        cat2.setTamed(true);
        cat2.setBaby();
        cat2.setOwner(p);
        cat2.setSitting(true);
        cat2.setBreed(false);
        cat3.setCatType(Ocelot.Type.values()[i]);
        cat3.setTamed(true);
        cat3.setBaby();
        cat3.setOwner(p);
        cat3.setSitting(true);
        cat3.setBreed(false);
        cat4.setCatType(Ocelot.Type.values()[i]);
        cat4.setTamed(true);
        cat4.setBaby();
        cat4.setOwner(p);
        cat4.setSitting(true);
        cat4.setBreed(false);
        cat5.setCatType(Ocelot.Type.values()[i]);
        cat5.setTamed(true);
        cat5.setBaby();
        cat5.setOwner(p);
        cat5.setSitting(true);
        cat5.setBreed(false);
        cat6.setCatType(Ocelot.Type.values()[i]);
        cat6.setTamed(true);
        cat6.setBaby();
        cat6.setOwner(p);
        cat6.setSitting(true);
        cat6.setBreed(false);
        cat7.setCatType(Ocelot.Type.values()[i]);
        cat7.setTamed(true);
        cat7.setBaby();
        cat7.setOwner(p);
        cat7.setSitting(true);
        cat7.setBreed(false);
        cat8.setCatType(Ocelot.Type.values()[i]);
        cat8.setTamed(true);
        cat8.setBaby();
        cat8.setOwner(p);
        cat8.setSitting(true);
        cat8.setBreed(false);
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
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat1.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat2.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat3.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat4.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat5.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat6.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat7.getLocation(), 10);
                ParticleEffect.LAVA.display((float) 0.5, (float) 0.5, (float) 0.5, (float) 0, 10, cat8.getLocation(), 10);
                
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
        }.runTaskTimer(Main.plugin, 1, 1);
    }
}