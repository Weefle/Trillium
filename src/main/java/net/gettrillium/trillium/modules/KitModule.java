package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Kit;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Crit;
import net.gettrillium.trillium.messageutils.MType;
import net.gettrillium.trillium.messageutils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitModule extends TrilliumModule {

    public KitModule() {
        super("kits");
    }

    @Command(command = "kit", description = "Get a certain kit.", usage = "/kit [kit name]")
    public void kit(CommandSender sender, String[] args) {
        if (getConfig().getBoolean(Configuration.Kit.ENABLED)) {
            if (sender instanceof Player) {
                TrilliumPlayer p = player((Player) sender);
                if (args.length == 0) {
                    Message.m(MType.R, p.getProxy(), "Kit", "Available kits:");
                    for (String s : getConfig().getStringList(Configuration.Kit.KIT_MAKER)) {
                        Message.m(MType.R, p.getProxy(), "Kit", s);
                    }
                } else {
                    if (getConfig().getStringList(Configuration.Kit.KIT_MAKER).contains(args[0])) {
                        if (p.getProxy().hasPermission(Permission.Kit.USE + args[0])) {

                            Kit kit = new Kit(args[0]);
                            kit.giveTo(p.getProxy());

                            Message.m(MType.G, p.getProxy(), "Kit", "You successfully received kit: " + args[0]);

                        } else {
                            Message.m(MType.W, p.getProxy(), "KIT", "You don't have permission to use that kit.");
                        }
                    } else {
                        Message.m(MType.W, p.getProxy(), "KIT", args[0] + " is not a valid kit.");
                    }
                }
            } else {
                Message.e(sender, "KIT", Crit.C);
            }
        } else {
            Message.m(MType.W, sender, "Kit", "This feature is disabled.");
        }
    }
}
