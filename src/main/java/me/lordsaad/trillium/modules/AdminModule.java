package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.command.CommandSender;

public class AdminModule extends TrilliumModule {
    @Command(command = "broadcast", description = "Broadcast a message to the world", usage = "/broadcast [message]")
    public void broadcast(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Admin.BROADCAST)) {
            if (args.length == 0) {
                Message.earg(cs, "Broadcast", "Too few arguments. /broadcast <message>");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                TrilliumAPI.broadcast(sb.toString().trim());
            }
        } else {
            Message.e(cs, "Broadcast", Crit.P);
        }
    }
}
