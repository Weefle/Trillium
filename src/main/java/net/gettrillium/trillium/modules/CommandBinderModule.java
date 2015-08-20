package net.gettrillium.trillium.modules;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class CommandBinderModule extends TrilliumModule {

    private ArrayList<UUID> setMode = new ArrayList<>();
    private ArrayList<UUID> removeMode = new ArrayList<>();
    private Table<UUID, String, Boolean> table = HashBasedTable.create();

    @Command(name = "Cmd Binder",
            command = "commandbinder",
            description = "Bind a command to a block, an air block, or an item.",
            usage = "/cb <set <item/block> <console/player> <command>/remove <item/block>>",
            aliases = {"cmdbinder", "cmdb", "cb", "cbinder", "cbind"},
            permissions = {Permission.Admin.CMDBINDER})
    public void commandbinder(CommandSender cs, String[] args) {
        String cmd = "commandbinder";
        if (!(cs instanceof Player)) {
            new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        Player p = (Player) cs;

        if (!p.hasPermission(Permission.Admin.CMDBINDER)) {
            new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(p);
            return;
        }

        if (setMode.contains(p.getUniqueId()) && removeMode.contains(p.getUniqueId())) {
            new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You are already in command binder's edit mode.").to(p);
            return;
        }

        if (args.length == 0) {
            new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {

            if (args.length < 4) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                return;
            }

            if (!args[2].equalsIgnoreCase("console")
                    && !args[2].equalsIgnoreCase("c")
                    && !args[2].equalsIgnoreCase("player")
                    && !args[2].equalsIgnoreCase("p")) {
                new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                return;
            }

            boolean player = !args[2].equalsIgnoreCase("console") || !args[1].equalsIgnoreCase("c");
            Bukkit.broadcastMessage(player + "");

            StringBuilder sb = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String command = sb.toString().trim();

            if (command.equalsIgnoreCase("") && command.equalsIgnoreCase(" ")) {
                new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
                return;
            }

            if (args[1].equalsIgnoreCase("block") || args[1].equalsIgnoreCase("b")) {

                setMode.add(p.getUniqueId());
                table.put(p.getUniqueId(), command, player);
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "You are now in command binder's edit mode.").to(p);
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), ChatColor.RED + "" + ChatColor.BOLD + "DO NOT TOUCH ANYTHING AIMLESSLY").to(p);
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "The next block you PUNCH will bind the command you entered to that block " +
                        "and any block you RIGHT CLICK will bind the block above it (air) to your entered command (as a walkable block)").to(p);

            } else if (args[1].equalsIgnoreCase("item") || args[1].equalsIgnoreCase("i")) {

                if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You aren't holding an item to bind.").to(p);
                    return;
                }

                CommandBinder.Items.add(p, command, p.getItemInHand().getType(), player);

                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command bound to item.").to(p);
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "Item: " + p.getItemInHand().getType().name().replace("_", " ").toLowerCase()).to(p);
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "Command: " + command).to(p);

            } else {
                new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
            }

        } else if (args[0].equalsIgnoreCase("remove")) {

            if (args.length < 2) {
                new Message(TrilliumAPI.getName(cmd), Error.TOO_FEW_ARGUMENTS, "/cb <set <item/block> <console/player> <command>/remove <item/block>>").to(p);
                return;
            }

            if (args[1].equalsIgnoreCase("block")) {

                removeMode.add(p.getUniqueId());
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "You are now in command binder's edit mode.").to(p);
                new Message(Mood.BAD, TrilliumAPI.getName(cmd), ChatColor.RED + "" + ChatColor.BOLD + "DO NOT TOUCH ANYTHING AIMLESSLY").to(p);
                new Message(Mood.NEUTRAL, TrilliumAPI.getName(cmd), "The next block you PUNCH will unbind any command bound to that block " +
                        "and any block you RIGHT CLICK will unbind any command bound to the block above it (air)").to(p);

            } else if (args[1].equalsIgnoreCase("item")) {
                if (p.getItemInHand().getType() != null) {
                    new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You aren't holding an item to unbind.").to(p);
                    return;
                }

                CommandBinder.Items.remove(p);
                new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "All commands unbound from the item you're holding.").to(p);

            } else {
                new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
            }
        } else {
            new Message(TrilliumAPI.getName(cmd), Error.WRONG_ARGUMENTS, TrilliumAPI.getUsage(cmd)).to(p);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        String cmd = "Cmd Binder";
        Player p = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (setMode.contains(p.getUniqueId())) {
                Map<UUID, Map<String, Boolean>> rows = table.rowMap();
                for (Map.Entry<UUID, Map<String, Boolean>> row : rows.entrySet()) {
                    if (p.getUniqueId().equals(row.getKey())) {
                        for (Map.Entry<String, Boolean> column : row.getValue().entrySet()) {

                            Location loc = null;
                            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                CommandBinder.Blocks.add(column.getKey(), event.getClickedBlock().getLocation(), column.getValue());

                                loc = event.getClickedBlock().getLocation();

                            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                CommandBinder.Blocks.add(column.getKey(), event.getClickedBlock().getRelative(BlockFace.UP).getLocation(), column.getValue());

                                loc = new Location(p.getWorld(), event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockX(),
                                        event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockY(),
                                        event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockZ());
                            }

                            String sender;
                            if (column.getValue()) {
                                sender = "player";
                            } else {
                                sender = "console";
                            }

                            setMode.remove(p.getUniqueId());
                            table.remove(row.getKey(), column.getKey());

                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command bound successfully bound to block.").to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command: " + column.getKey()).to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Location: " + Utils.locationToString(loc)).to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command Sender: " + sender).to(p);
                            event.setCancelled(true);
                        }
                    }
                }

            } else if (removeMode.contains(p.getUniqueId())) {
                Map<String, Map<Location, Boolean>> rows = CommandBinder.Blocks.getTable().rowMap();
                for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                    for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {

                        Location loc = null;
                        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            loc = event.getClickedBlock().getLocation();

                        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            loc = new Location(p.getWorld(), event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockX(),
                                    event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockY(),
                                    event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockZ());
                        }

                        if (column.getKey().equals(loc)) {

                            CommandBinder.Blocks.remove(row.getKey(), column.getKey());
                            removeMode.remove(p.getUniqueId());

                            String sender;
                            if (column.getValue()) {
                                sender = "player";
                            } else {
                                sender = "console";
                            }
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command unbound successfully from block.").to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command: " + row.getKey()).to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Location: " + Utils.locationToString(loc)).to(p);
                            new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "Command Sender: " + sender).to(p);
                            event.setCancelled(true);
                        }

                    }
                }

            } else {
                Map<String, Map<Location, Boolean>> rows = CommandBinder.Blocks.getTable().rowMap();
                for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                    for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {
                        if (column.getKey().equals(event.getClickedBlock().getLocation())) {

                            if (column.getValue()) {
                                Bukkit.dispatchCommand(event.getPlayer(), Utils.commandBlockify(row.getKey(), p));
                            } else {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Utils.commandBlockify(row.getKey(), p));
                            }
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

        for (String command : CommandBinder.Items.getCommands(p, p.getItemInHand().getType())) {
            Bukkit.dispatchCommand(CommandBinder.Items.getSender(p, command), command);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getZ() != event.getTo().getZ()
                || event.getFrom().getY() != event.getTo().getY()) {
            Player p = event.getPlayer();

            if (!setMode.contains(p.getUniqueId()) && !removeMode.contains(p.getUniqueId())) {
                Map<String, Map<Location, Boolean>> rows = CommandBinder.Blocks.getTable().rowMap();
                for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                    for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {
                        if (column.getKey() != null) {

                            Location blockLoc = new Location(p.getWorld(),
                                    p.getLocation().getBlockX(),
                                    p.getLocation().getBlockY(),
                                    p.getLocation().getBlockZ());

                            if (column.getKey().equals(blockLoc)) {
                                if (column.getValue()) {
                                    Bukkit.dispatchCommand(event.getPlayer(), Utils.commandBlockify(row.getKey(), p));
                                } else {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Utils.commandBlockify(row.getKey(), p));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}