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

public class CommandVanish implements CommandExecutor {

    public static ArrayList<UUID> vanishedusers = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("vanish")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    if (p.hasPermission("tr.vanish")) {

                        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));

                        if (vanishedusers.contains(p.getUniqueId())) {

                            vanishedusers.remove(p.getUniqueId());
                            Message.m(MType.G, p, "Vanish Mode", "You are no longer in vanish mode.");
                            yml.set("Vanish Mode", false);

                            for (Player online : Bukkit.getOnlinePlayers()) {
                                online.showPlayer(p);
                            }

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            vanishedusers.add(p.getUniqueId());

                            Message.m(MType.G, p, "Vanish Mode", "You are now in vanish mode.");
                            yml.set("Vanish Mode", true);

                            for (Player online : Bukkit.getOnlinePlayers()) {
                                online.hidePlayer(p);
                            }

                            try {
                                yml.save(PlayerDatabase.db(p));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Message.e(p, "Vanish Mode", Crit.P);
                    }

                } else {
                    if (p.hasPermission("tr.vanish")) {
                        Player pl = Bukkit.getPlayer(args[0]);
                        if (pl != null) {

                            YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(pl));

                            if (vanishedusers.contains(pl.getUniqueId())) {

                                vanishedusers.remove(pl.getUniqueId());
                                Message.m(MType.G, pl, "Vanish Mode", p.getName() + " removed you from vanish mode.");
                                Message.m(MType.G, p, "Vanish Mode", pl.getName() + " is no longer in vanish mode.");
                                yml.set("Vanish Mode", false);

                                for (Player online : Bukkit.getOnlinePlayers()) {
                                    online.showPlayer(pl);
                                }
                                
                                try {
                                    yml.save(PlayerDatabase.db(p));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                vanishedusers.add(pl.getUniqueId());

                                Message.m(MType.G, pl, "Vanish Mode", p.getName() + " put you in vanish mode.");
                                Message.m(MType.G, p, "Vanish Mode", pl.getName() + " is now in vanish mode.");
                                yml.set("vanish Mode", true);

                                for (Player online : Bukkit.getOnlinePlayers()) {
                                    online.hidePlayer(pl);
                                }
                                
                                try {
                                    yml.save(PlayerDatabase.db(pl));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Message.eplayer(p, "Vanish Mode", args[0]);
                        }
                    } else {
                        Message.earg(p, "Vanish Mode", "/vanish [player]");
                    }
                }
            } else {
                Message.e(sender, "Vanish Mode", Crit.C);
            }
        }

        return true;
    }
}
