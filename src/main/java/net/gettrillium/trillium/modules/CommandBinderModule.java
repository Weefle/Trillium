package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CommandBinderModule extends TrilliumModule {

    private HashMap<UUID, Material> item = new HashMap<>();
    private HashMap<UUID, String> itemCommand = new HashMap<>();
    private HashMap<UUID, String> command = new HashMap<>();
    private ArrayList<UUID> set = new ArrayList<>();
    private ArrayList<UUID> walkset = new ArrayList<>();
    private ArrayList<UUID> touchset = new ArrayList<>();


    @Command(command = "commandbinder", description = "Bind a command to a block, an air block, or an item.", usage = "/cb <touch/walk/item> <console/player> <command>", aliases = {"cmdbinder", "cmdb", "cb", "cbinder", "cbind"})
    public void commandbinder(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.CMDBINDER)) {

                if (args.length < 3) {
                    new Message("CMD Binder", Error.TOO_FEW_ARGUMENTS, "/cb <touch/walk/item> <console/player> <command>").to(p);
                    new Message(Mood.BAD, "CMD Binder", "or /cb <t/w/i> <c/p> <command>").to(p);
                    new Message(Mood.BAD, "CMD Binder", "Example: /cb t c tp [p] 110 45 247").to(p);
                } else {

                    if (args[0].equalsIgnoreCase("touch") || args[0].equalsIgnoreCase("t")) {
                        if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            command.put(p.getProxy().getUniqueId(), sb.toString().trim() + "#~#false");
                            set.add(p.getProxy().getUniqueId());
                            touchset.add(p.getProxy().getUniqueId());

                            new Message(Mood.GENERIC, "CMD Binder", "Punch a block to bind the command to it.").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "WARNING: Do not touch anything aimlessly!").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "The first thing you touch now will bind the command you want").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "to the block you punch next. Be careful where you punch!").to(p);

                        } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            command.put(p.getProxy().getUniqueId(), sb.toString().trim() + "#~#true");
                            set.add(p.getProxy().getUniqueId());
                            touchset.add(p.getProxy().getUniqueId());

                            new Message(Mood.GENERIC, "CMD Binder", "Punch a block to bind the command to it.").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "WARNING: Do not touch anything aimlessly!").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "The first thing you touch now will bind the command you want").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "to the block you punch next. Be careful where you punch!").to(p);

                        }
                    } else if (args[0].equalsIgnoreCase("walk") || args[0].equalsIgnoreCase("w")) {

                        if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            command.put(p.getProxy().getUniqueId(), sb.toString().trim() + "#~#false");
                            set.add(p.getProxy().getUniqueId());
                            walkset.add(p.getProxy().getUniqueId());

                            new Message(Mood.GENERIC, "CMD Binder", "Right click a block to bind the command to it.").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "WARNING: Do not touch anything aimlessly!").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "The first block you right click will bind the command to the adjacent empty side").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "so walking through that empty air block adjacent to the block you right clicked").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "will make the command get run.").to(p);

                        } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            command.put(p.getProxy().getUniqueId(), sb.toString().trim() + "#~#true");
                            set.add(p.getProxy().getUniqueId());
                            walkset.add(p.getProxy().getUniqueId());

                            new Message(Mood.GENERIC, "CMD Binder", "Right click a block to bind the command to it.").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "WARNING: Do not touch anything aimlessly!").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "The first block you right click will bind the command to the adjacent empty side").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "so walking through that empty air block adjacent to the block you right clicked").to(p);
                            new Message(Mood.GENERIC, "CMD Binder", "will make the command get run.").to(p);

                        }
                    } else if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("i")) {

                        if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                            if (p.getProxy().getItemInHand().getType() != null) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }

                                itemCommand.put(p.getProxy().getUniqueId(), sb.toString().trim() + "#~#false");
                                item.put(p.getProxy().getUniqueId(), p.getProxy().getItemInHand().getType());

                                new Message(Mood.GOOD, "CMD Binder", "Command: "
                                        + ChatColor.YELLOW + "'"
                                        + ChatColor.AQUA
                                        + sb.toString().trim()
                                        + ChatColor.YELLOW + "'").to(p);
                                new Message(Mood.GOOD, "CMD Binder", "was successfully bound to item: " + ChatColor.YELLOW + p.getProxy().getItemInHand().getItemMeta().getDisplayName()).to(p);
                            }

                        } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                            if (p.getProxy().getItemInHand().getType() != null) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }

                                itemCommand.put(p.getProxy().getUniqueId(), sb.toString().trim() + "#~#true");
                                item.put(p.getProxy().getUniqueId(), p.getProxy().getItemInHand().getType());

                                new Message(Mood.GOOD, "CMD Binder", "Command: "
                                        + ChatColor.YELLOW + "'"
                                        + ChatColor.AQUA
                                        + sb.toString().trim()
                                        + ChatColor.YELLOW + "'").to(p);
                                new Message(Mood.GOOD, "CMD Binder", "was successfully bound to item: " + ChatColor.YELLOW + p.getProxy().getItemInHand().getItemMeta().getDisplayName()).to(p);
                            }
                        }
                    } else {
                        new Message("Cmd Binder", Error.TOO_FEW_ARGUMENTS, "/cb <touch/walk/item> <console/player> <command>").to(p);
                    }
                }
            } else {
                new Message("CMD Binder", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("CMD Binder", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getClickedBlock() != null) {
            Location loc = event.getClickedBlock().getLocation();

            if (set.contains(p.getUniqueId())) {

                String command = this.command.get(p.getUniqueId()).split("#~#")[0];
                Boolean player = Boolean.parseBoolean(this.command.get(p.getUniqueId()).split("#~#")[1]);

                if (touchset.contains(p.getUniqueId())) {
                    set.remove(p.getUniqueId());
                    touchset.remove(p.getUniqueId());
                    this.command.remove(p.getUniqueId());
                    event.setCancelled(true);
                    new CommandBinder(command, player, loc).setToBlock();
                    new Message(Mood.GOOD, "CMD Binder", "Command: " + ChatColor.YELLOW + "'" + ChatColor.AQUA + command + ChatColor.YELLOW + "'").to(p);
                    new Message(Mood.GOOD, "CMD Binder", "was set to block: " + ChatColor.AQUA + event.getClickedBlock().getType().toString()).to(p);

                }

                if (walkset.contains(p.getUniqueId())) {
                    Location relative = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
                    set.remove(p.getUniqueId());
                    walkset.remove(p.getUniqueId());
                    this.command.remove(p.getUniqueId());
                    event.setCancelled(true);
                    new CommandBinder(command, player, relative).setToWalkableBlock();
                    new Message(Mood.GOOD, "CMD Binder", "Command: " + ChatColor.YELLOW + "'" + ChatColor.AQUA + this.command + ChatColor.YELLOW + "'").to(p);
                    new Message(Mood.GOOD, "CMD Binder", "was set to air block at: "
                            + ChatColor.AQUA + relative.getBlockX()
                            + ChatColor.GRAY + ","
                            + ChatColor.AQUA + relative.getBlockY()
                            + ChatColor.GRAY + ","
                            + ChatColor.AQUA + relative.getBlockZ()).to(p);
                }
            } else {

                if (new CommandBinder(loc).hasCommand()) {
                    if (new CommandBinder(loc).isPlayer()) {
                        Bukkit.dispatchCommand(p, new CommandBinder(loc).getCommand().replace("[p]", p.getName()));
                    } else {
                        Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), new CommandBinder(loc).getCommand().replace("[p]", p.getName()));
                    }
                    event.setCancelled(true);

                }
            }
        }
        if (p.getItemInHand().getType() != null || p.getItemInHand().getType() != Material.AIR) {
            if (item.containsKey(p.getUniqueId()) && itemCommand.containsKey(p.getUniqueId())) {
                Material mat = item.get(p.getUniqueId());
                String command = itemCommand.get(p.getUniqueId()).split("#~#")[0];
                Boolean player = Boolean.parseBoolean(itemCommand.get(p.getUniqueId()).split("#~#")[1]);
                if (mat == p.getItemInHand().getType()) {
                    if (player) {
                        Bukkit.dispatchCommand(p, command);
                    } else {
                        Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), command);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ()
                || event.getFrom().getBlockY() != event.getTo().getBlockY()) {
            Player p = event.getPlayer();
            Location loc = p.getLocation();
            CommandBinder cb = new CommandBinder(loc);
            String command = cb.getCommand().replace("[p]", p.getName());

            if (cb.hasCommand()) {
                if (cb.isPlayer()) {
                    Bukkit.dispatchCommand(p, command);
                } else {
                    Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), command);
                }
            }
        }
    }
}