package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.PlayerDatabase;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CommandGodMode implements CommandExecutor {

    public static ArrayList<UUID> godmodeusers = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("god")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    if (p.hasPermission("tr.god")) {

                        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));

                        if (godmodeusers.contains(p.getUniqueId())) {

                            godmodeusers.remove(p.getUniqueId());
                            Message.m(MType.G, p, "God Mode", "You are no longer in god mode.");
                            yml.set("God Mode", false);

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            godmodeusers.add(p.getUniqueId());

                            Message.m(MType.G, p, "God Mode", "You are now in god mode.");
                            yml.set("God Mode", true);

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Message.e(p, "God Mode", Crit.P);
                    }

                } else {
                    if (p.hasPermission("tr.god")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        if (pl != null) {

                            YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));

                            if (godmodeusers.contains(p.getUniqueId())) {

                                godmodeusers.remove(p.getUniqueId());
                                Message.m(MType.G, p, "God Mode", "You are no longer in god mode.");
                                yml.set("God Mode", false);

                                try {
                                    yml.save(PlayerDatabase.db(p));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                godmodeusers.add(p.getUniqueId());

                                Message.m(MType.G, p, "God Mode", "You are now in god mode.");
                                yml.set("God Mode", true);

                                try {
                                    yml.save(PlayerDatabase.db(p));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Message.eplayer(p, "God Mode", args[0]);
                        }
                    } else {
                        Message.earg(p, "God Mode", "/god [player]");
                    }
                }
            } else {
                Message.e(sender, "God Mode", Crit.C);
            }
        }

        return true;
    }
}