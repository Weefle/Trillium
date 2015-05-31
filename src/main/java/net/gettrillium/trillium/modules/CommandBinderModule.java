package net.gettrillium.trillium.modules;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.commandbinder.CommandBinder;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

    @Command(command = "commandbinder", description = "Bind a command to a block, an air block, or an item.", usage = "/cb <touch/walk/item> <console/player> <command>", aliases = {"cmdbinder", "cmdb", "cb", "cbinder", "cbind"})
    public void commandbinder(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (p.hasPermission(Permission.Admin.CMDBINDER)) {
                if (!setMode.contains(p.getUniqueId()) && !removeMode.contains(p.getUniqueId())) {
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args.length >= 3) {
                            if (args[1].equalsIgnoreCase("console")
                                    || args[1].equalsIgnoreCase("c")
                                    || args[1].equalsIgnoreCase("player")
                                    || args[1].equalsIgnoreCase("p")) {

                                String command = args[2].replace("/", "");
                                if (!command.equalsIgnoreCase("") && !command.equalsIgnoreCase(" ")) {
                                    boolean player;
                                    player = !(args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c"));
                                    setMode.add(p.getUniqueId());
                                    table.put(p.getUniqueId(), command, player);
                                    new Message(Mood.GENERIC, "CMD Binder", "You are now in command binder's edit mode.").to(p);
                                    new Message(Mood.BAD, "CMD Binder", ChatColor.RED + "" + ChatColor.BOLD + "DO NOT TOUCH ANYTHING AIMLESSLY").to(p);
                                    new Message(Mood.GENERIC, "CMD Binder", "The next block you PUNCH will bind the command you entered to that block " +
                                            "and any block you RIGHT CLICK will bind the block above it (air) to your entered command (as a walkable block)").to(p);

                                } else {
                                    new Message("CMD Binder", Error.WRONG_ARGUMENTS, "/cb set <console/player> <command>").to(p);
                                }
                            } else {
                                new Message("CMD Binder", Error.WRONG_ARGUMENTS, "/cb set <console/player> <command>").to(p);
                            }
                        } else {
                            new Message("CMD Binder", Error.TOO_FEW_ARGUMENTS, "/cb set <console/player> <command>").to(p);
                        }

                    } else if (args[0].equalsIgnoreCase("remove")) {
                        removeMode.add(p.getUniqueId());
                        new Message(Mood.GENERIC, "CMD Binder", "You are now in command binder's edit mode.").to(p);
                        new Message(Mood.BAD, "CMD Binder", ChatColor.RED + "" + ChatColor.BOLD + "DO NOT TOUCH ANYTHING AIMLESSLY").to(p);
                        new Message(Mood.GENERIC, "CMD Binder", "The next block you PUNCH will unbind any command bound to that block " +
                                "and any block you RIGHT CLICK will unbind any command bound to the block above it (air)").to(p);

                    } else {
                        new Message("CMD Binder", Error.WRONG_ARGUMENTS, "/cb set <console/player> <command>").to(p);
                    }
                } else {
                    new Message(Mood.BAD, "CMD Binder", "You are already in command binder's edit mode.").to(p);
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

        if (setMode.contains(p.getUniqueId())) {
            Map<UUID, Map<String, Boolean>> rows = table.rowMap();
            for (Map.Entry<UUID, Map<String, Boolean>> row : rows.entrySet()) {
                if (p.getUniqueId().equals(row.getKey())) {
                    for (Map.Entry<String, Boolean> column : row.getValue().entrySet()) {

                        String loc = null;
                        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            CommandBinder.add(column.getKey(), event.getClickedBlock().getLocation(), column.getValue());
                            loc = event.getClickedBlock().getLocation().getBlockX() + "; "
                                    + event.getClickedBlock().getLocation().getBlockY() + "; "
                                    + event.getClickedBlock().getLocation().getBlockZ();

                        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            CommandBinder.add(column.getKey(), event.getClickedBlock().getRelative(BlockFace.UP).getLocation(), column.getValue());
                            loc = event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockX() + "; "
                                    + event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockY() + "; "
                                    + event.getClickedBlock().getRelative(BlockFace.UP).getLocation().getBlockZ();
                        }

                        String sender;
                        if (column.getValue()) {
                            sender = "player";
                        } else {
                            sender = "console";
                        }

                        setMode.remove(p.getUniqueId());
                        table.remove(row.getKey(), column.getKey());

                        new Message(Mood.GOOD, "CMD Binder", "Command bound successfully bound to block.").to(p);
                        new Message(Mood.GOOD, "CMD Binder", "Command: " + column.getKey()).to(p);
                        new Message(Mood.GOOD, "CMD Binder", "Location: " + loc).to(p);
                        new Message(Mood.GOOD, "CMD Binder", "Command Sender: " + sender).to(p);
                    }
                }
            }

        } else if (removeMode.contains(p.getUniqueId())) {
            Map<String, Map<Location, Boolean>> rows = CommandBinder.getTable().rowMap();
            for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {
                    if (column.getKey().equals(p.getLocation())) {
                        CommandBinder.remove(row.getKey(), column.getKey());
                    }
                }
            }

        } else {
            Map<String, Map<Location, Boolean>> rows = CommandBinder.getTable().rowMap();
            for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {
                    if (column.getKey().equals(p.getLocation())) {
                        if (column.getValue()) {
                            Bukkit.dispatchCommand(event.getPlayer(), row.getKey());
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), row.getKey());
                        }
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

            if (!setMode.contains(p.getUniqueId())) {
                Map<String, Map<Location, Boolean>> rows = CommandBinder.getTable().rowMap();
                for (Map.Entry<String, Map<Location, Boolean>> row : rows.entrySet()) {
                    for (Map.Entry<Location, Boolean> column : row.getValue().entrySet()) {
                        if (column.getKey().equals(p.getLocation())) {
                            if (column.getValue()) {
                                Bukkit.dispatchCommand(event.getPlayer(), row.getKey());
                            } else {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), row.getKey());
                            }
                        }
                    }
                }
            }
        }
    }
}