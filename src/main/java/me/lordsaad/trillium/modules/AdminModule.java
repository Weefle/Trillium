package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminModule extends TrilliumModule {

    public AdminModule() {
        super("admin");
    }

    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast <message>")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.BROADCAST)) {
            if (args.length == 0) {
                Message.earg(cs, "Broadcast", "Too few arguments. /broadcast <message>");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String message = sb.toString().trim();
                
                List<String> format = getConfig().getStringList("Broadcast");

                for (String s : format) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    s = s.replace("[msg]", message);
                    Bukkit.broadcastMessage(s);
                }            }
        } else {
            Message.e(cs, "Broadcast", Crit.P);
        }
    }
}
