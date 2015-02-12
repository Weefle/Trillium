package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AbilityModule extends TrilliumModule {
    @Command(command = "back", description = "Teleport to your last active position", usage = "/back")
    public void back(CommandSender cs) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (player.getProxy().hasPermission(Permission.Ability.BACK)) {
                Message.m(MType.G, player.getProxy(), "Back", "You have been sent back to your last location.");
                player.getProxy().teleport(player.getLastLocation());
            } else {
                Message.e(player.getProxy(), "Back", Crit.P);
            }
        } else {
            Message.e(cs, "Back", Crit.C);
        }
    }

    @Command(command = "fly", description = "SOAR THROUGH THE AIR LIKE A MAJESTIC BUTTERFLY!", usage = "/fly")
    public void fly(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (args.length == 0) {
                if (player.hasPermission(Permission.Ability.FLY)) {
                    if (!player.isFlying()) {
                        player.setFlying(true);
                        Message.m(MType.G, player.getProxy(), "Fly", "You are now in fly mode.");
                    } else {
                        player.setFlying(false);
                        Message.m(MType.G, player.getProxy(), "Fly", "You are no longer in fly mode.");
                    }
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
                        Message.m(MType.G, player.getProxy(), "God", "You are now in vanish mode.");
                    } else {
                        player.setVanished(false);
                        Message.m(MType.G, player.getProxy(), "God", "You are no longer in vanish mode.");
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
                        } else {
                            Message.m(MType.G, target.getProxy(), "Vanish", player.getProxy().getName() + " put you in vanish mode.");
                            Message.m(MType.G, player.getProxy(), "Vanish", target.getProxy().getName() + " is now in vanish mode.");
                            target.setVanished(true);
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

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            TrilliumPlayer player = player(event.getEntity().getName());
            if (player.isGod()) {
                event.setCancelled(true);
            }
            if (player.isVanished() && getConfig().getBoolean(Configuration.Ability.GOD)) {
                event.setCancelled(true);
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
            TrilliumPlayer player = player(event.getEntity().getName());
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
}
