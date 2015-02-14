package me.lordsaad.trillium.modules;

import me.lordsaad.trillium.api.Permission;
import me.lordsaad.trillium.api.TrilliumModule;
import me.lordsaad.trillium.api.command.Command;
import me.lordsaad.trillium.api.player.TrilliumPlayer;
import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunModule extends TrilliumModule {

    public FunModule() {
        super("fun");
    }

    @Command(command = "smite", description = "Strike lightning somewhere or upon someone.", usage = "/smite [player]", aliases = "thor")
    public void smite(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (cs.hasPermission(Permission.Fun.SMITE)) {
                if (args.length == 0) {
                    Location loc = p.getProxy().getTargetBlock(null, 100).getLocation();
                    p.getProxy().getWorld().strikeLightning(loc);
                } else {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {
                        target.getProxy().getWorld().strikeLightning(target.getProxy().getLocation());
                        Message.m(MType.R, target.getProxy(), "Smite", p.getProxy().getName() + " stuck lightning upon you!");
                        Message.m(MType.R, p.getProxy(), "Smite", "You struck lightning upon " + target.getProxy().getName());

                    } else {
                        Message.eplayer(p.getProxy(), "Smite", args[0]);
                    }
                }
            } else {
                Message.e(p.getProxy(), "Smite", Crit.P);
            }
        } else {
            Message.e(cs, "Smite", Crit.C);
        }
    }
}