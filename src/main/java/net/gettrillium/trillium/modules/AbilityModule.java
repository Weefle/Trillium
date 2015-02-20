package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
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
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (args.length == 0) {
                if (player.hasPermission(Permission.Ability.FLY)) {
                    if (!player.isFlying()) {
                        Message.m(MType.G, player.getProxy(), "Fly", "You are now in fly mode.");
                    } else {
                        Message.m(MType.G, player.getProxy(), "Fly", "You are no longer in fly mode.");
                    }
                    player.setFlying(!player.isFlying());
                } else {
                    Message.e(player.getProxy(), "Fly", Crit.P);
                }

            } else {
                if (player.hasPermission(Permission.Ability.FLY_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isFlying()) {
                            Message.m(MType.G, target.getProxy(), "Fly", player.getProxy().getName() + " removed you from fly mode.");
                            Message.m(MType.G, player.getProxy(), "Fly", target.getProxy().getName() + " is no longer in fly mode.");
                            target.setFlying(false);
                        } else {
                            Message.m(MType.G, target.getProxy(), "Fly", player.getProxy().getName() + " put you in fly mode.");
                            Message.m(MType.G, player.getProxy(), "Fly", target.getProxy().getName() + " is now in fly mode.");
                            target.setFlying(true);
                        }
                    } else {
                        Message.eplayer(player.getProxy(), "Fly", args[0]);
                    }
                } else {
                    Message.earg(player.getProxy(), "Fly", "/fly [player]");
                }
            }
        } else {
            Message.e(cs, "Fly", Crit.C);
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
                        Message.m(MType.G, player.getProxy(), "God", "You are now in god mode.");
                    } else {
                        player.setGod(false);
                        Message.m(MType.G, player.getProxy(), "God", "You are no longer in god mode.");
                    }
                } else {
                    Message.e(player.getProxy(), "God", Crit.P);
                }

            } else {
                if (player.hasPermission(Permission.Ability.GOD_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isGod()) {
                            Message.m(MType.G, target.getProxy(), "God", player.getProxy().getName() + " removed you from god mode.");
                            Message.m(MType.G, player.getProxy(), "God", target.getProxy().getName() + " is no longer in god mode.");
                            target.setGod(false);
                        } else {
                            Message.m(MType.G, target.getProxy(), "God", player.getProxy().getName() + " put you in god mode.");
                            Message.m(MType.G, player.getProxy(), "God", target.getProxy().getName() + " is now in god mode.");
                            target.setGod(true);
                        }
                    } else {
                        Message.eplayer(player.getProxy(), "God", args[0]);
                    }
                } else {
                    Message.earg(player.getProxy(), "God", "/god [player]");
                }
            }
        } else {
            Message.e(cs, "God", Crit.C);
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
                        Message.m(MType.G, player.getProxy(), "Vanish", "You are now in vanish mode.");
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SPECTATOR);
                        }
                    } else {
                        player.setVanished(false);
                        Message.m(MType.G, player.getProxy(), "Vanish", "You are no longer in vanish mode.");
                        if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                            player.getProxy().setGameMode(GameMode.SURVIVAL);
                        }
                    }
                } else {
                    Message.e(player.getProxy(), "Vanish", Crit.P);
                }
            } else {
                if (player.hasPermission(Permission.Ability.VANISH_OTHER)) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        if (target.isVanished()) {
                            Message.m(MType.G, target.getProxy(), "Vanish", player.getProxy().getName() + " removed you from vanish mode.");
                            Message.m(MType.G, player.getProxy(), "Vanish", target.getProxy().getName() + " is no longer in vanish mode.");
                            target.setVanished(false);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SURVIVAL);
                            }
                        } else {
                            Message.m(MType.G, target.getProxy(), "Vanish", player.getProxy().getName() + " put you in vanish mode.");
                            Message.m(MType.G, player.getProxy(), "Vanish", target.getProxy().getName() + " is now in vanish mode.");
                            target.setVanished(true);
                            if (getConfig().getBoolean(Configuration.Ability.SPECTATOR)) {
                                target.getProxy().setGameMode(GameMode.SPECTATOR);
                            }
                        }
                    } else {
                        Message.eplayer(player.getProxy(), "Vanish", args[0]);
                    }
                } else {
                    Message.earg(player.getProxy(), "Vanish", "/vanish [player]");
                }
            }
        } else {
            Message.e(cs, "Vanish", Crit.C);
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
                            if (Utils.isNumeric(args[1])) {
                                i = Double.parseDouble(args[1]);
                                if (i <= 10 && i >= -10) {
                                    i = i * 0.1;
                                    p.getProxy().setFlySpeed((float) i);
                                    Message.m(MType.G, p.getProxy(), "Speed", "Fly speed set to " + ChatColor.AQUA + args[1]);
                                } else {
                                    Message.m(MType.W, p.getProxy(), "Speed", args[1] + " is out of bounds. -10 -> 10 only");
                                }
                            } else {
                                Message.m(MType.W, p.getProxy(), "Speed", args[1] + " is not a number.");
                            }
                        } else {
                            Message.m(MType.G, p.getProxy(), "Speed", "Fly speed set to default: " + ChatColor.AQUA + "1");
                            p.getProxy().setFlySpeed((float) 0.1);
                        }
                    } else if (args[0].equalsIgnoreCase("walk")) {
                        double i;
                        if (args.length > 1) {
                            if (Utils.isNumeric(args[1])) {
                                i = Double.parseDouble(args[1]);
                                if (i <= 10 && i >= -10) {
                                    i = i * 0.1;
                                    p.getProxy().setWalkSpeed((float) i);
                                    Message.m(MType.G, p.getProxy(), "Speed", "Walk speed set to " + ChatColor.AQUA + args[1]);
                                } else {
                                    Message.m(MType.W, p.getProxy(), "Speed", args[1] + " is out of bounds. -10 -> 10 only");
                                }
                            } else {
                                Message.m(MType.W, p.getProxy(), "Speed", args[1] + " is not a number.");
                            }
                        } else {
                            Message.m(MType.G, p.getProxy(), "Speed", "Walk speed set to default: " + ChatColor.AQUA + "2");
                            p.getProxy().setFlySpeed((float) 0.2);
                        }
                    } else {
                        Message.earg2(p.getProxy(), "Speed", "/speed <fly/walk> <speed>");
                    }
                } else {
                    Message.earg2(p.getProxy(), "Speed", "/speed <fly/walk> <speed>");
                }
            } else {
                Message.e(cs, "Speed", Crit.P);
            }
        } else {
            Message.e(cs, "Speed", Crit.C);
        }
    }

    @Command(command = "gamemode", description = "Change your gamemode.", usage = "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]", aliases = "gm")
    public void gamemode(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (args.length == 1) {
                if (p.hasPermission(Permission.Ability.GAMEMODE)) {

                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                        Message.m(MType.G, p.getProxy(), "Gamemode", "Gamemode set to " + ChatColor.AQUA + "creative");
                        p.getProxy().setGameMode(GameMode.CREATIVE);

                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                        Message.m(MType.G, p.getProxy(), "Gamemode", "Gamemode set to " + ChatColor.AQUA + "survival");
                        p.getProxy().setGameMode(GameMode.SURVIVAL);

                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                        Message.m(MType.G, p.getProxy(), "Gamemode", "Gamemode set to " + ChatColor.AQUA + "adventure");
                        p.getProxy().setGameMode(GameMode.ADVENTURE);

                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                        Message.m(MType.G, p.getProxy(), "Gamemode", "Gamemode set to " + ChatColor.AQUA + "spectator");
                        p.getProxy().setGameMode(GameMode.SPECTATOR);

                    } else {
                        Message.m(MType.W, p.getProxy(), "Gamemode", "Mojang didn't add that gamemode yet...");
                    }

                } else {
                    Message.e(p.getProxy(), "Gamemode", Crit.P);
                }

            } else if (args.length > 1) {

                if (p.hasPermission(Permission.Ability.GAMEMODE_OTHER)) {
                    TrilliumPlayer pl = player(args[1]);
                    if (pl != null) {

                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            Message.m(MType.G, p.getProxy(), "Gamemode", pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "creative");
                            Message.m(MType.G, pl.getProxy(), "Gamemode", p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "creative");
                            pl.getProxy().setGameMode(GameMode.CREATIVE);

                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            Message.m(MType.G, p.getProxy(), "Gamemode", pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "survival");
                            Message.m(MType.G, pl.getProxy(), "Gamemode", p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "survival");
                            pl.getProxy().setGameMode(GameMode.SURVIVAL);

                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            Message.m(MType.G, p.getProxy(), "Gamemode", pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "adventure");
                            Message.m(MType.G, pl.getProxy(), "Gamemode", p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "adventure");
                            pl.getProxy().setGameMode(GameMode.ADVENTURE);

                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                            Message.m(MType.G, p.getProxy(), "Gamemode", pl.getProxy().getName() + "'s gamemode set to " + ChatColor.AQUA + "spectator");
                            Message.m(MType.G, pl.getProxy(), "Gamemode", p.getProxy().getName() + "set your gamemode to " + ChatColor.AQUA + "spectator");
                            pl.getProxy().setGameMode(GameMode.SPECTATOR);

                        } else {
                            Message.m(MType.W, p.getProxy(), "Gamemode", "Mojang didn't add that gamemode yet...");
                        }

                    } else {
                        Message.eplayer(p.getProxy(), "Gamemode", args[1]);
                    }
                } else {
                    Message.earg(p.getProxy(), "Gamemode", "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]");

                }

            } else {
                if (p.hasPermission(Permission.Ability.GAMEMODE)) {
                    if (p.getProxy().getGameMode() == GameMode.CREATIVE) {
                        p.getProxy().setGameMode(GameMode.SURVIVAL);
                        Message.m(MType.G, p.getProxy(), "Gamemode", "Gamemode set to " + ChatColor.AQUA + "survival.");
                    } else {
                        p.getProxy().setGameMode(GameMode.CREATIVE);
                        Message.m(MType.G, p.getProxy(), "Gamemode", "Gamemode set to " + ChatColor.AQUA + "creative.");
                    }
                } else {
                    Message.e(p.getProxy(), "Gamemode", Crit.P);
                }
            }
        } else {
            Message.e(cs, "Gamemode", Crit.C);
        }
    }

    @Command(command = "pvp", description = "Toggle your pvp status whether you want to disable/enable pvp for yourself.", usage = "/pvp")
    public void pvp(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.Server.PVPENABLE)) {
            if (getConfig().getBoolean(Configuration.Server.TOGGLEPVP)) {
                if (cs instanceof Player) {
                    TrilliumPlayer p = player((Player) cs);
                    if (p.hasPermission(Permission.Ability.PVP)) {
                        if (p.canPvp()) {
                            Message.m(MType.R, p.getProxy(), "PVP", "Pvp has been enabled for you.");
                        } else {
                            Message.m(MType.R, p.getProxy(), "PVP", "Pvp has been disabled for you.");
                        }
                        p.setPvp(!p.canPvp());

                    }
                } else {
                    Message.e(cs, "PVP", Crit.C);
                }
            } else {
                Message.m(MType.W, cs, "PVP", "This feature is disabled.");
            }
        } else {
            Message.m(MType.W, cs, "PVP", "PVP is completely disabled.");
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
        if (getConfig().getBoolean(Configuration.Server.PVPENABLE)) {
            if (getConfig().getBoolean(Configuration.Server.TOGGLEPVP)) {
                if (event.getEntity() instanceof Player) {
                    TrilliumPlayer p = player((Player) event.getEntity());
                    if (event.getDamager() instanceof Player) {
                        TrilliumPlayer pl = player((Player) event.getDamager());
                        if (!p.canPvp() || !pl.canPvp()) {
                            if (p.getProxy().getUniqueId() != pl.getProxy().getUniqueId()) {
                                event.setCancelled(true);
                            }
                        }
                    } else if (event.getDamager() instanceof Projectile) {
                        Projectile arrow = (Projectile) event.getDamager();
                        if (arrow.getShooter() instanceof Player) {
                            TrilliumPlayer pl = player((Player) arrow.getShooter());
                            if (!p.canPvp() || !pl.canPvp()) {
                                if (p.getProxy().getUniqueId() != pl.getProxy().getUniqueId()) {
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (event.getEntity() instanceof Player) {
                TrilliumPlayer p = player((Player) event.getEntity());
                if (event.getDamager() instanceof Player) {
                    TrilliumPlayer pl = player((Player) event.getDamager());
                    if (p.getProxy().getUniqueId() != pl.getProxy().getUniqueId()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        TrilliumPlayer player = player(event.getEntity().getName());
        if (player.isGod()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            TrilliumPlayer player = player(event.getTarget().getName());
            if (player.isGod()) {
                event.setCancelled(true);
            }
            if (player.isVanished()) {
                event.setCancelled(true);
            }
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
            e.setQuitMessage("");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        TrilliumPlayer player = player(e.getPlayer());
        if (player.isVanished()) {
            e.setJoinMessage("");

            Message.m(MType.W, player.getProxy(), "Vanish Mode", "Remember! You are still in vanish mode!");
            for (TrilliumPlayer online : TrilliumAPI.getOnlinePlayers()) {
                online.getProxy().hidePlayer(player.getProxy());
            }
        }

        if (player.isGod()) {
            Message.m(MType.W, player.getProxy(), "God Mode", "Remember! You are still in god mode!");
        }
    }
}
