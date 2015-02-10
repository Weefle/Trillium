package me.lordsaad.trillium.commands;

import java.io.IOException;

import me.lordsaad.trillium.databases.PlayerDatabase;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class CommandUnban implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("unban")) {
            if (sender.hasPermission("tr.ban")) {
                if (args.length == 0) {
                    Message.earg(sender, "Unban", "/unban <player>");
                } else {

                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                    Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                    Message.b(MType.G, "Unban", target.getName() + " got unbanned.");
                    YamlConfiguration pdb = YamlConfiguration.loadConfiguration(PlayerDatabase.db(target));
                    pdb.set("Ban Reason", "");
                    try {
                        pdb.save(PlayerDatabase.db(target));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Message.e(sender, "Unban", Crit.P);
            }
        }
        return true;
    }
}