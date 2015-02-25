package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BroadcastModule extends TrilliumModule {

    public BroadcastModule() {
        super("broadcast");
    }

    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast <message>", aliases = "bc")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Chat.BROADCAST)) {
            if (args.length == 0) {
                Message.earg(cs, "Broadcast", "Too few arguments. /broadcast <message>");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString().trim();

                if (getConfig().getBoolean(Configuration.Chat.CENTRALIZE_BROADCAST)) {

                    List<String> centered = Utils.centerText(message);
                    List<String> format = getConfig().getStringList(Configuration.Chat.BROADCASTFORMAT);
                    for (String s : format) {
                        if (s.contains("[msg]")) {
                            for (String slices : centered) {
                                s = s.replace("[msg]", "");
                                s = ChatColor.translateAlternateColorCodes('&', s);
                                Bukkit.broadcastMessage(slices);
                            }
                        } else {
                            s = ChatColor.translateAlternateColorCodes('&', s);
                            Bukkit.broadcastMessage(s);
                        }
                    }
                } else {

                    List<String> format = getConfig().getStringList(Configuration.Chat.BROADCASTFORMAT);
                    for (String s : format) {
                        s = s.replace("[msg]", ChatColor.translateAlternateColorCodes('&', getConfig().getString(Configuration.Chat.COLORIZE_BROADCAST) + message));
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        Bukkit.broadcastMessage(s);
                    }
                }
            }
        } else {
            Message.e(cs, "Broadcast", Crit.P);
        }
    }
}
