package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.databases.PlayerDatabase;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandBan implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (sender.hasPermission("tr.ban")) {
                if (args.length == 0) {
                    Message.earg(sender, "Ban", "/ban <player> [reason]");
                } else {

                    Player target = Bukkit.getPlayer(args[0]);
                    String reason;

                    if (args.length > 1) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        reason = sb.toString().trim();
                    } else {
                        reason = "You are the weakest link. Good bye.";
                    }

                    if (target != null) {

                        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, null, sender.getName());
                        target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                        Message.b(MType.W, "Ban", target.getName() + " got banned with reason:");
                        Message.b(MType.W, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(target));
                        pdb.set("Ban Reason", reason);
                        try {
                            pdb.save(PlayerDatabase.db(target));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        OfflinePlayer offlinetarget = Bukkit.getOfflinePlayer(args[0]);

                        Bukkit.getBanList(BanList.Type.NAME).addBan(offlinetarget.getName(), reason, null, sender.getName());
                        Message.b(MType.W, "Ban", offlinetarget.getName() + " got banned with reason:");
                        Message.b(MType.W, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                        YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(offlinetarget));
                        pdb.set("Ban Reason", reason);
                        try {
                            pdb.save(PlayerDatabase.db(offlinetarget));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Message.e(sender, "Ban", Crit.P);
            }
        }
        return true;
    }
}
