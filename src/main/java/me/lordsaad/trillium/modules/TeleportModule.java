package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportModule extends TrilliumModule {

    public TeleportModule() {
        super("teleport");
    }

    @Command(command = "spawn", description = "Teleport to the server's spawn.", usage = "/spawn")
    public void spawn(CommandSender cs) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.SPAWN)) {
                p.getProxy().teleport(p.getProxy().getWorld().getSpawnLocation());
            } else {
                Message.e(p.getProxy(), "Spawn", Crit.P);
            }
        } else {
            Message.e(cs, "Spawn", Crit.C);
        }
    }

}
