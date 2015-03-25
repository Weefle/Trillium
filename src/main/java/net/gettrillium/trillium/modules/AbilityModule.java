package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Type;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
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

    public AbilityModule() {
        super("ability");
    }

    @Command(command = "fly", description = "SOAR THROUGH THE AIR LIKE A MAJESTIC BUTTERFLY!", usage = "/fly")
    public void fly(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            Message.error("Fly", cs);
            return;
        }

        TrilliumPlayer player = player(cs.getName());
        if (args.length == 0) {
            if (player.hasPermission(Permission.Ability.FLY) || player.hasPermission(Permission.Ability.FLY_OTHER)) {
                if (!player.isFlying()) {
                    Message.message(Type.GOOD, player.getProxy(), "Fly", true, "You are now in fly mode.");
                } else {
                    Message.message(Type.GOOD, player.getProxy(), "Fly", true, "You are no longer in fly mode.");
                }
                player.setFlying(!player.isFlying());
            } else {
                Message.error("Fly", player.getProxy());
            }
        } else {
            if (player.hasPermission(Permission.Ability.FLY_OTHER)) {
                TrilliumPlayer target = player(args[0]);
                if (target != null) {
                    if (target.isFlying()) {
                        Message.message(Type.GOOD, target.getProxy(), "Fly", true, player.getProxy().getName() + " removed you from fly mode.");
                        Message.message(Type.GOOD, player.getProxy(), "Fly", true, target.getProxy().getName() + " is no longer in fly mode.");
                        target.setFlying(false);
                    } else {
                        Message.message(Type.GOOD, target.getProxy(), "Fly", true, player.getProxy().getName() + " put you in fly mode.");
                        Message.message(Type.GOOD, player.getProxy(), "Fly", true, target.getProxy().getName() + " is now in fly mode.");
                        target.setFlying(true);
                    }
                } else {
                    Message.error("Fly", cs, args[0]);
                }
            } else {
                Message.error(player.getProxy(), "Fly", true, "/fly [player]");
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
                        Message.message(Type.GOOD, player.getProxy(), "God", true, "You are now in god mode.");
                    } else {
                        player.setGod(false);
                        Message.message(Type.GOOD, player.getProxy(), "God", true, "You are no longer in god mode.");
                    }
                } else {
                    Message.error("God", player.getProxy());
                }

            } else {
                if (player.hasPermission(Permission.Ability.GOD_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isGod()) {
                            Message.message(Type.GOOD, target.getProxy(), "God", true, player.getProxy().getName() + " removed you from god mode.");
                            Message.message(Type.GOOD, player.getProxy(), "God", true, target.getProxy().getName() + " is no longer in god mode.");
                            target.setGod(false);
                        } else {
                            Message.message(Type.GOOD, target.getProxy(), "God", true, player.getProxy().getName() + " put you in god mode.");
                            Message.message(Type.GOOD, player.getProxy(), "God", true, target.getProxy().getName() + " is now in god mode.");
                            target.setGod(true);
                        }
                    } else {
                        Message.error("God", cs, args[0]);
                    }
                } else {
                    Message.error(player.getProxy(), "God", true, "/god [player]");
                }
            }
        } else {
            Message.error("God", cs);
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
                        Message.message(Type.GOOD, player.getProxy(), "Vanish", true, "You are now in vanish mode.");
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SPECTATOR);
                        }
                    } else {
                        player.setVanished(false);
                        Message.message(Type.GOOD, player.getProxy(), "Vanish", true, "You are no longer in vanish mode.");
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SURVIVAL);
                        }
                    }
                } else {
                    Message.error("Vanish", player.getProxy());
                }
            } else {
                if (player.hasPermission(Permission.Ability.VANISH_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isVanished()) {
                            Message.message(Type.GOOD, target.getProxy(), "Vanish", true, player.getProxy().getName() + " removed you from vanish mode.");
                            Message.message(Type.GOOD, player.getProxy(), "Vanish", true, target.getProxy().getName() + " is no longer in vanish mode.");
                            target.setVanished(false);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SURVIVAL);
                            }
                        } else {
                            Message.message(Type.GOOD, target.getProxy(), "Vanish", true, player.getProxy().getName() + " put you in vanish mode.");
                            Message.message(Type.GOOD, player.getProxy(), "Vanish", true, target.getProxy().getName() + " is now in vanish mode.");
                            target.setVanished(true);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SPECTATOR);
                            }
                        }
                    } else {
                        Message.error("Vanish", cs, args[0]);
                    }
                } else {
                    Message.error(player.getProxy(), "Vanish", true, "/vanish [player]");
                }
            }
        } else {
            Message.error("Vanish", cs);
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
                                    Message.message(Type.GOOD, p.getProxy(), "Speed", true, "Fly speed set to " + ChatColor.AQUA + args[1]);
                                } else {
                                    Message.message(Type.WARNING, p.getProxy(), "Speed", true, args[1] + " is out of bounds. -10 -> 10 only");
                                }
                            } else {
                                Message.message(Type.WARNING, p.getProxy(), "Speed", true, args[1] + " is not a number.");
                            }
                        } else {
                            Message.message(Type.GOOD, p.getProxy(), "Speed", true, "Fly speed set to default: " + ChatColor.AQUA + "1");
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
                                    Message.message(Type.GOOD, p.getProxy(), "Speed", true, "Walk speed set to " + ChatColor.AQUA + args[1]);
                                } else {
                                    Message.message(Type.WARNING, p.getProxy(), "Speed", true, args[1] + " is out of bounds. -10 -> 10 only");
                                }
                            } else {
                                Message.message(Type.WARNING, p.getProxy(), "Speed", true, args[1] + " is not a number.");
                            }
                        } else {
                            Message.message(Type.GOOD, p.getProxy(), "Speed", true, "Walk speed set to default: " + ChatColor.AQUA + "2");
                            p.getProxy().setFlySpeed((float) 0.2);
                        }
                    } else {
                        Message.error(p.getProxy(), "Speed", false, "/speed <fly/walk> <speed>");
                    }
                } else {
                    Message.error(p.getProxy(), "Speed", false, "/speed <fly/walk> <speed>");
                }
            } else {
                Message.error("Speed", cs);
            }
        } else {
            Message.error("Speed", cs);
        }
    }

    @Command(command = "gamemode", description = "Change your gamemode.", usage = "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]", aliases = "gm")
    public void gamemode(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (p.hasPermission(Permission.Ability.GAMEMODE)) {

                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                        Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, "Gamemode set to " + ChatColor.AQUA + "creative");
                        p.getProxy().setGameMode(GameMode.CREATIVE);

                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                        Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, "Gamemode set to " + ChatColor.AQUA + "survival");
                        p.getProxy().setGameMode(GameMode.SURVIVAL);

                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                        Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, "Gamemode set to " + ChatColor.AQUA + "adventure");
                        p.getProxy().setGameMode(GameMode.ADVENTURE);

                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                        Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, "Gamemode set to " + ChatColor.AQUA + "spectator");
                        p.getProxy().setGameMode(GameMode.SPECTATOR);

                    } else {
                        Message.message(Type.WARNING, p.getProxy(), "Gamemode", true, "Mojang didn't add that gamemode yet...");
                    }

                } else {
                    Message.error("Gamemode", cs);
                }

            } else if (args.length > 1) {

                if (p.hasPermission(Permission.Ability.GAMEMODE_OTHER)) {
                    TrilliumPlayer pl = player(args[1]);
                    if (pl != null) {

                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "creative");
                            Message.message(Type.GOOD, pl.getProxy(), "Gamemode", true, p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "creative");
                            pl.getProxy().setGameMode(GameMode.CREATIVE);

                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "survival");
                            Message.message(Type.GOOD, pl.getProxy(), "Gamemode", true, p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "survival");
                            pl.getProxy().setGameMode(GameMode.SURVIVAL);

                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "adventure");
                            Message.message(Type.GOOD, pl.getProxy(), "Gamemode", true, p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "adventure");
                            pl.getProxy().setGameMode(GameMode.ADVENTURE);

                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                            Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "spectator");
                            Message.message(Type.GOOD, pl.getProxy(), "Gamemode", true, p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "spectator");
                            pl.getProxy().setGameMode(GameMode.SPECTATOR);

                        } else {
                            Message.message(Type.WARNING, p.getProxy(), "Gamemode", true, "Mojang didn't add that gamemode yet...");
                        }

                    } else {
                        Message.error("Gamemode", cs, args[1]);
                    }
                } else {
                    Message.error(p.getProxy(), "Gamemode", true, "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]");

                }

            } else {
                if (p.hasPermission(Permission.Ability.GAMEMODE)) {
                    if (p.getProxy().getGameMode() == GameMode.CREATIVE) {
                        p.getProxy().setGameMode(GameMode.SURVIVAL);
                        Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, "Gamemode set to " + ChatColor.AQUA + "survival.");
                    } else {
                        p.getProxy().setGameMode(GameMode.CREATIVE);
                        Message.message(Type.GOOD, p.getProxy(), "Gamemode", true, "Gamemode set to " + ChatColor.AQUA + "creative.");
                    }
                } else {
                    Message.error("Gamemode", cs);
                }
            }
        } else {
            Message.error("Gamemode", cs);
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
                            Message.message(Type.GENERIC, p.getProxy(), "PVP", true, "Pvp has been enabled for you.");
                        } else {
                            Message.message(Type.GENERIC, p.getProxy(), "PVP", true, "Pvp has been disabled for you.");
                        }
                        p.setPvp(!p.canPvp());
                    }
                } else {
                    Message.error("PVP", cs);
                }
            } else {
                Message.message(Type.WARNING, cs, "PVP", true, "This feature is disabled.");
            }
        } else {
            Message.message(Type.WARNING, cs, "PVP", true, "PVP is completely disabled.");
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

            Message.message(Type.WARNING, player.getProxy(), "Vanish", true, "Remember! You are still in vanish mode!");
            for (TrilliumPlayer online : TrilliumAPI.getOnlinePlayers()) {
                online.getProxy().hidePlayer(player.getProxy());
            }
        }

        if (player.isGod()) {
            Message.message(Type.WARNING, player.getProxy(), "God", true, "Remember! You are still in god mode!");
        }
    }
}
