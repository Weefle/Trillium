package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.*;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AbilityModule extends TrilliumModule {

    @Command(command = "fly", description = "SOAR THROUGH THE AIR LIKE A MAJESTIC BUTTERFLY!", usage = "/fly")
    public void fly(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("Fly", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        TrilliumPlayer player = player(cs.getName());
        if (args.length == 0) {
            if (player.hasPermission(Permission.Ability.FLY) || player.hasPermission(Permission.Ability.FLY_OTHER)) {
                if (!player.isFlying()) {
                    new Message(Mood.GOOD, "Fly", "You are now in fly mode.").to(player);
                } else {
                    new Message(Mood.BAD, "Fly", "You are no longer in fly mode.").to(player);
                }
                player.setFlying(!player.isFlying());
            } else {
                new Message("Fly", Error.NO_PERMISSION).to(player);
            }
        } else {
            if (player.hasPermission(Permission.Ability.FLY_OTHER)) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {
                    if (target.isFlying()) {
                        new Message(Mood.BAD, "Fly", target.getName() + " is no longer in fly mode.").to(player);
                        new Message(Mood.BAD, "Fly", player.getName() + " removed you from fly mode.").to(target);

                        target.setFlying(false);
                    } else {
                        new Message(Mood.GOOD, "Fly", target.getName() + " is now in fly mode.").to(player);
                        new Message(Mood.GOOD, "Fly", player.getName() + " put you in fly mode.").to(target);

                        target.setFlying(true);
                    }
                } else {
                    new Message("Fly", Error.INVALID_PLAYER, args[0]).to(cs);
                }
            } else {
                new Message("Fly", Error.TOO_FEW_ARGUMENTS, "/fly [player]");
            }
        }
    }

    @Command(command = "god", description = "Become invincible to anything.", usage = "/god")
    public void god(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (args.length == 0) {
                if (player.hasPermission(Permission.Ability.GOD)) {
                    if (!player.isGod()) {
                        player.setGod(true);
                        new Message(Mood.GOOD, "God", "You are now in god mode.").to(player);
                    } else {
                        player.setGod(false);
                        new Message(Mood.BAD, "God", "You are no longer in god mode.").to(player);
                    }
                } else {
                    new Message("God", Error.NO_PERMISSION);
                }

            } else {
                if (player.hasPermission(Permission.Ability.GOD_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isGod()) {
                            new Message(Mood.BAD, "God", player.getName() + " removed you from god mode.").to(target);
                            new Message(Mood.BAD, "God", target.getName() + " is no longer in god mode.").to(player);
                            target.setGod(false);
                        } else {
                            new Message(Mood.GOOD, "God", player.getName() + " put you in god mode.").to(target);
                            new Message(Mood.GOOD, "God", target.getName() + " is now in god mode.").to(player);
                            target.setGod(true);
                        }
                    } else {
                        new Message("God", Error.INVALID_PLAYER, args[0]).to(player);
                    }
                } else {
                    new Message("God", Error.TOO_FEW_ARGUMENTS, "/god [player]").to(player);
                }
            }
        } else {
            new Message("God", Error.CONSOLE_NOT_ALLOWED);
        }
    }

    @Command(command = "vanish", description = "Turn completely invisible and roam the world undetected.!", usage = "/vanish [player]", aliases = "v")
    public void vanish(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (args.length == 0) {
                if (player.hasPermission(Permission.Ability.VANISH)) {
                    if (!player.isVanished()) {
                        player.setVanished(true);
                        new Message(Mood.GOOD, "Vanish", "You are now in vanish mode.").to(player);
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SPECTATOR);
                        }
                    } else {
                        player.setVanished(false);
                        new Message(Mood.BAD, "Vanish", "You are no longer in vanish mode.").to(player);
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SURVIVAL);
                        }
                    }
                } else {
                    new Message("Vanish", Error.NO_PERMISSION).to(player);
                }
            } else {
                if (player.hasPermission(Permission.Ability.VANISH_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isVanished()) {
                            new Message(Mood.BAD, "Vanish", player.getName() + " removed you from vanish mode.").to(target);
                            new Message(Mood.BAD, "Vanish", target.getName() + " is no longer in vanish mode.").to(player);
                            target.setVanished(false);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SURVIVAL);
                            }
                        } else {
                            new Message(Mood.GOOD, "Vanish", player.getName() + " put you in vanish mode.").to(target);
                            new Message(Mood.GOOD, "Vanish", target.getName() + " is now in vanish mode.").to(player);
                            target.setVanished(true);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SPECTATOR);
                            }
                        }
                    } else {
                        new Message("Vanish", Error.INVALID_PLAYER, args[0]).to(player);
                    }
                } else {
                    new Message("Vanish", Error.TOO_FEW_ARGUMENTS, "/vanish [player]").to(player);
                }
            }
        } else {
            new Message("Vanish", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "speed", description = "Change your speed without potion effects", usage = "/speed <fly/walk> <speed>")
    public void speed(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Ability.SPEED)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("fly")) {
                        double i;
                        if (args.length > 1) {
                            if (StringUtils.isNumeric(args[1])) {
                                i = Double.parseDouble(args[1]);
                                if (i <= 10 && i >= -10) {
                                    i = i * 0.1;
                                    p.getProxy().setFlySpeed((float) i);
                                    new Message(Mood.GOOD, "Speed", "Fly speed set to " + ChatColor.AQUA + args[1]).to(p);
                                } else {
                                    new Message(Mood.BAD, "Speed", args[1] + " is out of bounds. -10 -> 10 only.").to(p);
                                }
                            } else {
                                new Message(Mood.BAD, "Speed", args[1] + " is not a number.").to(p);
                            }
                        } else {
                            new Message(Mood.GOOD, "Speed", "Fly speed set to default: " + ChatColor.AQUA + "1").to(p);
                            p.getProxy().setFlySpeed((float) 0.1);
                        }
                    } else if (args[0].equalsIgnoreCase("walk")) {
                        double i;
                        if (args.length > 1) {
                            if (StringUtils.isNumeric(args[1])) {
                                i = Double.parseDouble(args[1]);
                                if (i <= 10 && i >= -10) {
                                    i = i * 0.1;
                                    p.getProxy().setWalkSpeed((float) i);
                                    new Message(Mood.GOOD, "Speed", "Walk speed set to " + ChatColor.AQUA + args[1]).to(p);
                                } else {
                                    new Message(Mood.BAD, "Speed", args[1] + " is out of bounds. -10 -> 10 only.").to(p);
                                }
                            } else {
                                new Message(Mood.BAD, "Speed", args[1] + " is not a number.").to(p);
                            }
                        } else {
                            new Message(Mood.GOOD, "Speed", "Walk speed set to default: " + ChatColor.AQUA + "2").to(p);
                            p.getProxy().setFlySpeed((float) 0.2);
                        }
                    } else {
                        new Message("Speed", Error.TOO_FEW_ARGUMENTS, "/speed <fly/walk> <speed>").to(p);
                    }
                } else {
                    new Message("Speed", Error.WRONG_ARGUMENTS, "/speed <fly/walk> <speed>").to(p);
                }
            } else {
                new Message("Speed", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("Speed", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "gamemode", description = "Change your gamemode.", usage = "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]", aliases = "gm")
    public void gamemode(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (p.hasPermission(Permission.Ability.GAMEMODE)) {

                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                        new Message(Mood.GOOD, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "creative").to(p);
                        p.getProxy().setGameMode(GameMode.CREATIVE);

                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                        new Message(Mood.GOOD, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "survival").to(p);
                        p.getProxy().setGameMode(GameMode.SURVIVAL);

                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                        new Message(Mood.GOOD, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "adventure").to(p);
                        p.getProxy().setGameMode(GameMode.ADVENTURE);

                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                        new Message(Mood.GOOD, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "spectator").to(p);
                        p.getProxy().setGameMode(GameMode.SPECTATOR);

                    } else {
                        new Message(Mood.BAD, "Gamemode", "Mojang didn't add that gamemode yet...").to(p);
                    }

                } else {
                    new Message("Gamemode", Error.NO_PERMISSION).to(p);
                }

            } else if (args.length > 1) {

                if (p.hasPermission(Permission.Ability.GAMEMODE_OTHER)) {
                    TrilliumPlayer pl = player(args[1]);
                    if (pl != null) {

                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            new Message(Mood.GOOD, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "creative").to(p);
                            new Message(Mood.GOOD, "Gamemode", p.getName() + " set your gamemode to " + ChatColor.AQUA + "creative").to(pl);
                            pl.getProxy().setGameMode(GameMode.CREATIVE);

                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            new Message(Mood.GOOD, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "survival").to(p);
                            new Message(Mood.GOOD, "Gamemode", p.getName() + " set your gamemode to " + ChatColor.AQUA + "survival").to(pl);
                            pl.getProxy().setGameMode(GameMode.SURVIVAL);

                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            new Message(Mood.GOOD, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "adventure").to(p);
                            new Message(Mood.GOOD, "Gamemode", p.getName() + " set your gamemode to " + ChatColor.AQUA + "adventure").to(pl);
                            pl.getProxy().setGameMode(GameMode.ADVENTURE);

                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                            new Message(Mood.GOOD, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "spectator").to(p);
                            new Message(Mood.GOOD, "Gamemode", p.getName() + " set your gamemode to " + ChatColor.AQUA + "spectator").to(pl);
                            pl.getProxy().setGameMode(GameMode.SPECTATOR);

                        } else {
                            new Message(Mood.BAD, "Gamemode", "Mojang didn't add that gamemode yet...").to(p);
                        }

                    } else {
                        new Message("Gamemode", Error.INVALID_PLAYER, args[1]).to(p);
                    }
                } else {
                    new Message("Gamemode", Error.TOO_FEW_ARGUMENTS, "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]").to(p);
                }

            } else {
                if (p.hasPermission(Permission.Ability.GAMEMODE)) {
                    if (p.getProxy().getGameMode() == GameMode.CREATIVE) {
                        p.getProxy().setGameMode(GameMode.SURVIVAL);
                        new Message(Mood.GOOD, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "survival").to(p);
                    } else {
                        p.getProxy().setGameMode(GameMode.CREATIVE);
                        new Message(Mood.GOOD, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "creative").to(p);
                    }
                } else {
                    new Message("Gamemode", Error.NO_PERMISSION).to(p);
                }
            }
        } else {
            new Message("Gamemode", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "pvp", description = "Toggle your pvp status whether you want to disable/enable pvp for yourself.", usage = "/pvp")
    public void pvp(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.Server.PVPENABLE)) {
            if (getConfig().getBoolean(Configuration.Server.TOGGLEPVP)) {
                if (cs instanceof Player) {
                    TrilliumPlayer p = player((Player) cs);
                    if (p.hasPermission(Permission.Ability.PVP)) {
                        if (!p.canPvp()) {
                            new Message(Mood.GOOD, "PVP", "Pvp has been enabled for you.").to(p);
                        } else {
                            new Message(Mood.BAD, "PVP", "Pvp has been disabled for you.").to(p);
                        }
                        p.setPvp(!p.canPvp());
                    } else {
                        new Message("PVP", Error.NO_PERMISSION).to(p);
                    }
                } else {
                    new Message("PVP", Error.CONSOLE_NOT_ALLOWED).to(cs);
                }
            } else {
                new Message(Mood.BAD, "PVP", "This feature is disabled.").to(cs);
            }
        } else {
            new Message(Mood.BAD, "PVP", "Pvp is completely disabled.").to(cs);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            TrilliumPlayer player = player((Player) event.getEntity());
            if (player.isGod()) {
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

        if (!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Projectile)) {
            return;
        }

        TrilliumPlayer p = player((Player) event.getEntity());
        TrilliumPlayer damager;

        if (event.getDamager() instanceof Player) {
            damager = player((Player) event.getDamager());
        } else {
            Projectile arrow = (Projectile) event.getDamager();
            damager = player((Player) arrow.getShooter());
        }

        if (getConfig().getBoolean(Configuration.Server.PVPENABLE)) {
            if (!getConfig().getBoolean(Configuration.Server.TOGGLEPVP)) {
                return;
            }

            if (!p.canPvp() || !damager.canPvp()) {
                if (p.getProxy().getUniqueId() != damager.getProxy().getUniqueId()) {
                    event.setCancelled(true);
                }
            }
        } else {
            if (!(event.getDamager() instanceof Player)) {
                return;
            }

            if (p.getProxy().getUniqueId() != damager.getProxy().getUniqueId()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        TrilliumPlayer player = player((Player) event.getEntity());
        if (player.isGod()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) {
            return;
        }

        TrilliumPlayer player = player(event.getTarget().getName());
        if (player.isGod()) {
            event.setCancelled(true);
        }

        if (player.isVanished()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isVanished() && !getConfig().getBoolean(Configuration.Ability.PICK_UP_ITEM)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        TrilliumPlayer player = player(event.getPlayer());
        if (player.isVanished() && !getConfig().getBoolean(Configuration.Ability.DROP_ITEM)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        TrilliumPlayer player = player(e.getPlayer());
        if (player.isVanished()) {
            e.setQuitMessage(null);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        TrilliumPlayer player = player(e.getPlayer());
        if (player.isVanished()) {
            e.setJoinMessage(null);

            new Message(Mood.BAD, "Vanish", "Remember! You are still in vanish mode!").to(player);
            for (TrilliumPlayer online : TrilliumAPI.getOnlinePlayers()) {
                online.getProxy().hidePlayer(player.getProxy());
            }
        }

        if (player.isGod()) {
            new Message(Mood.BAD, "God", "Remember! You are still in god mode!").to(player);
        }
    }
}
