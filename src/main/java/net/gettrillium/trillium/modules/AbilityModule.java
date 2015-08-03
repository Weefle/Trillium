package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Configuration.Server;
import net.gettrillium.trillium.api.Permission.Ability;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.TrilliumPlayer;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.projectiles.ProjectileSource;

public class AbilityModule extends TrilliumModule {

    @Command(name = "Fly",
            command = "fly",
            description = "SOAR THROUGH THE AIR LIKE A MAJESTIC BUTTERFLY!",
            usage = "/fly",
            permissions = {Ability.FLY, Ability.FLY_OTHER})
    public void fly(CommandSender cs, String[] args) {
        String cmd = "fly";
        if (args.length == 0) {
            if (!(cs instanceof Player)) {
                new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
                return;
            }

            TrilliumPlayer player = player(cs.getName());

            if (player.hasPermission(Ability.FLY) || player.hasPermission(Ability.FLY_OTHER)) {
                if (player.isFlying()) {
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You are no longer in fly mode.").to(player);
                } else {
                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You are now in fly mode.").to(player);
                }
                player.setFlying(!player.isFlying());
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(player);
            }
        } else {
            if (cs.hasPermission(Ability.FLY_OTHER)) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {
                    if (target.isFlying()) {
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), target.getName() + " is no longer in fly mode.").to(cs);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), cs.getName() + " removed you from fly mode.").to(target);

                        target.setFlying(false);
                    } else {
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " is now in fly mode.").to(cs);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), cs.getName() + " put you in fly mode.").to(target);

                        target.setFlying(true);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[1]).to(cs);
            }
        }
    }

    @Command(name = "God",
            command = "god",
            description = "Become invincible to anything.",
            usage = "/god",
            permissions = {Ability.GOD, Ability.GOD_OTHER})
    public void god(CommandSender cs, String[] args) {
        String cmd = "god";
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (args.length == 0) {
                if (player.hasPermission(Ability.GOD)) {
                    if (player.isGod()) {
                        player.setGod(false);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You are no longer in god mode.").to(player);
                    } else {
                        player.setGod(true);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You are now in god mode.").to(player);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(cs);
                }
            } else {
                if (player.hasPermission(Ability.GOD_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isGod()) {
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), player.getName() + " removed you from god mode.").to(target);
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), target.getName() + " is no longer in god mode.").to(player);
                            target.setGod(false);
                        } else {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), player.getName() + " put you in god mode.").to(target);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " is now in god mode.").to(player);
                            target.setGod(true);
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(player);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[1]).to(player);
                }
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Vanish",
            command = "vanish",
            description = "Turn completely invisible and roam the world undetected.!",
            usage = "/vanish [player]",
            aliases = "v",
            permissions = {Ability.VANISH, Ability.VANISH_OTHER})
    public void vanish(CommandSender cs, String[] args) {
        String cmd = "vanish";
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (args.length == 0) {
                if (player.hasPermission(Ability.VANISH)) {
                    if (player.isVanished()) {
                        player.setVanished(false);
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You are no longer in vanish mode.").to(player);
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SURVIVAL);
                        }
                    } else {
                        player.setVanished(true);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You are now in vanish mode.").to(player);
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SPECTATOR);
                        }
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(player);
                }
            } else {
                if (player.hasPermission(Ability.VANISH_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isVanished()) {
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), player.getName() + " removed you from vanish mode.").to(target);
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), target.getName() + " is no longer in vanish mode.").to(player);
                            target.setVanished(false);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SURVIVAL);
                            }
                        } else {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), player.getName() + " put you in vanish mode.").to(target);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), target.getName() + " is now in vanish mode.").to(player);
                            target.setVanished(true);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SPECTATOR);
                            }
                        }
                    } else {
                        new Message("Vanish", Error.INVALID_PLAYER, args[0]).to(player);
                    }
                } else {
                    new Message("Vanish", Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[1]).to(player);
                }
            }
        } else {
            new Message("Vanish", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Speed",
            command = "speed",
            description = "Change your speed without potion effects",
            usage = "/speed <fly/walk> <speed>",
            permissions = {Ability.SPEED})
    public void speed(CommandSender cs, String[] args) {
        String cmd = "speed";
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Ability.SPEED)) {
                if (args.length == 0) {
                    new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                } else {
                    if (args[0].equalsIgnoreCase("fly")) {
                        double i;
                        if (args.length > 1) {
                            if (StringUtils.isNumeric(args[1])) {
                                i = Double.parseDouble(args[1]);
                                if ((i <= 10) && (i >= -10)) {
                                    i *= 0.1;
                                    p.getProxy().setFlySpeed((float) i);
                                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Fly speed set to " + args[1]).to(p);
                                } else {
                                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[1] + " is out of bounds. -10 -> 10 only.").to(p);
                                }
                            } else {
                                new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[1] + " is not a number.").to(p);
                            }
                        } else {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Fly speed set to default: 1").to(p);
                            p.getProxy().setFlySpeed(0.1f);
                        }
                    } else if (args[0].equalsIgnoreCase("walk")) {
                        double i;
                        if (args.length > 1) {
                            if (StringUtils.isNumeric(args[1])) {
                                i = Double.parseDouble(args[1]);
                                if ((i <= 10) && (i >= -10)) {
                                    i *= 0.1;
                                    p.getProxy().setWalkSpeed((float) i);
                                    new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Walk speed set to " + args[1]).to(p);
                                } else {
                                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[1] + " is out of bounds. -10 -> 10 only.").to(p);
                                }
                            } else {
                                new Message(Mood.BAD, TrilliumAPI.getName(cmd), args[1] + " is not a number.").to(p);
                            }
                        } else {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Walk speed set to default: 2").to(p);
                            p.getProxy().setFlySpeed(0.2f);
                        }
                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                    }
                }
            } else {
                new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "Gamemode",
            command = "gamemode",
            description = "Change your gamemode.",
            usage = "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]",
            aliases = "gm",
            permissions = {Ability.GAMEMODE, Ability.GAMEMODE_OTHER})
    public void gamemode(CommandSender cs, String[] args) {
        String cmd = "gamemode";
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (p.hasPermission(Ability.GAMEMODE)) {

                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Gamemode set to " + ChatColor.AQUA + "creative").to(p);
                        p.getProxy().setGameMode(GameMode.CREATIVE);

                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Gamemode set to " + ChatColor.AQUA + "survival").to(p);
                        p.getProxy().setGameMode(GameMode.SURVIVAL);

                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Gamemode set to " + ChatColor.AQUA + "adventure").to(p);
                        p.getProxy().setGameMode(GameMode.ADVENTURE);

                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Gamemode set to " + ChatColor.AQUA + "spectator").to(p);
                        p.getProxy().setGameMode(GameMode.SPECTATOR);

                    } else {
                        new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Mojang didn't add that gamemode yet...").to(p);
                    }

                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
                }

            } else if (args.length > 1) {

                if (p.hasPermission(Ability.GAMEMODE_OTHER)) {
                    TrilliumPlayer pl = player(args[1]);
                    if (pl != null) {

                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "creative").to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), p.getName() + " set your gamemode to " + ChatColor.AQUA + "creative").to(pl);
                            pl.getProxy().setGameMode(GameMode.CREATIVE);

                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "survival").to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), p.getName() + " set your gamemode to " + ChatColor.AQUA + "survival").to(pl);
                            pl.getProxy().setGameMode(GameMode.SURVIVAL);

                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "adventure").to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), p.getName() + " set your gamemode to " + ChatColor.AQUA + "adventure").to(pl);
                            pl.getProxy().setGameMode(GameMode.ADVENTURE);

                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "spectator").to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), p.getName() + " set your gamemode to " + ChatColor.AQUA + "spectator").to(pl);
                            pl.getProxy().setGameMode(GameMode.SPECTATOR);

                        } else {
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Mojang didn't add that gamemode yet...").to(p);
                        }

                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[1]).to(p);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[1]).to(p);
                }

            } else {
                if (p.hasPermission(Ability.GAMEMODE)) {
                    if (p.getProxy().getGameMode() == GameMode.CREATIVE) {
                        p.getProxy().setGameMode(GameMode.SURVIVAL);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Gamemode set to " + ChatColor.AQUA + "survival").to(p);
                    } else {
                        p.getProxy().setGameMode(GameMode.CREATIVE);
                        new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Gamemode set to " + ChatColor.AQUA + "creative").to(p);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
                }
            }
        } else {
            new Message("Gamemode", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(name = "PVP",
            command = "pvp",
            description = "Toggle your pvp status whether you want to disable/enable pvp for yourself.",
            usage = "/pvp",
            permissions = {Ability.PVP})
    public void pvp(CommandSender cs, String[] args) {
        String cmd = "pvp";
        if (getConfig().getBoolean(Server.PVPENABLE)) {
            if (getConfig().getBoolean(Server.TOGGLEPVP)) {
                if (cs instanceof Player) {
                    TrilliumPlayer p = player((Player) cs);
                    if (p.hasPermission(Ability.PVP)) {
                        if (p.canPvp()) {
                            new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Pvp has been disabled for you.").to(p);
                        } else {
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Pvp has been enabled for you.").to(p);
                        }
                        p.setPvp(!p.canPvp());

                    } else {
                        new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
                    }
                } else {
                    new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
                }
            } else {
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), "This feature is disabled.").to(cs);
            }
        } else {
            new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Pvp is completely disabled.").to(cs);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            TrilliumPlayer player = player((Player) event.getEntity());
            if (player.isGod() || player.isShadowBanned()) {
                event.setCancelled(true);
            }
            if (player.isVanished() && getConfig().getBoolean(Configuration.Ability.GOD)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        TrilliumPlayer damaged = player((Player) event.getEntity());

        Entity e = event.getDamager();
        TrilliumPlayer damager;

        if (e instanceof Player) {
            damager = (TrilliumPlayer) event.getDamager();
        } else if (e instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) e).getShooter();
            if (!(shooter instanceof Player)) {
                return;
            }

            damager = ((TrilliumPlayer) shooter);
        } else {
            return;
        }

        if (getConfig().getBoolean(Server.PVPENABLE)) {
            if (!getConfig().getBoolean(Server.TOGGLEPVP)) {
                return;
            }

            if (!damaged.canPvp() || !damager.canPvp()) {
                if (!damaged.getProxy().getUniqueId().equals(damager.getProxy().getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        } else {
            if (!(event.getDamager() instanceof Player)) {
                return;
            }

            if (!damaged.getProxy().getUniqueId().equals(damager.getProxy().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        TrilliumPlayer player = player((Player) event.getEntity());
        if (player.isGod() || player.isShadowBanned()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) {
            return;
        }

        TrilliumPlayer player = player(event.getTarget().getName());
        if (player.isGod() || player.isVanished() || player.isShadowBanned()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isVanished() && !getConfig().getBoolean(Configuration.Ability.PICK_UP_ITEM)) {
            event.setCancelled(true);
        }

        if (player.isShadowBanned()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isVanished() && !getConfig().getBoolean(Configuration.Ability.DROP_ITEM)) {
            event.setCancelled(true);
        }
        if (player.isShadowBanned()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (p.isShadowBanned()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (p.isShadowBanned()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (p.isShadowBanned()) {
            event.setCancelled(true);
        }
    }
}
