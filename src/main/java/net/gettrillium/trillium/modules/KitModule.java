package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Kit;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.messageutils.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitModule extends TrilliumModule {

    public KitModule() {
        super("kits");
    }

    @Command(command = "kit", description = "Get a certain kit.", usage = "/kit [kit name]")
    public void kit(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.Kit.ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);
                if (args.length == 0) {
                    Message.m(Type.R, p.getProxy(), "Kit", true, "Available kits:");
                    for (String s : getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER).getKeys(false)) {
                        Message.m(Type.R, p.getProxy(), "Kit", true, s);
                    }
                } else {
                    if (getConfig().getStringList(Configuration.Kit.KIT_MAKER).contains(args[0])) {
                        if (p.getProxy().hasPermission(Permission.Kit.USE + args[0])) {

                            Kit kit = new Kit(args[0]);
                            kit.giveTo(p.getProxy());

                            Message.m(Type.G, p.getProxy(), "Kit", true, "You successfully received kit: " + args[0]);

                        } else {
                            Message.m(Type.W, p.getProxy(), "KIT", true, "You don't have permission to use that kit.");
                        }
                    } else {
                        Message.m(Type.W, p.getProxy(), "KIT", true, args[0] + " is not a valid kit.");
                    }
                }
            } else {
                Message.e("Kit", cs);
            }
        } else {
            Message.m(Type.W, cs, "Kit", true, "This feature is disabled.");
        }
    }
}
