package net.gettillium.trillium.modules;

import net.gettillium.trillium.api.Configuration;
import net.gettillium.trillium.api.TrilliumAPI;
import net.gettillium.trillium.api.TrilliumModule;

import java.io.File;

public class GroupManagerModule extends TrilliumModule {

    public GroupManagerModule() {
        super("groupmanager");
    }

    @Override
    public void register() {
        if (getConfig().getBoolean(Configuration.GM.ENABLED)) {
            TrilliumAPI.getInstance().getServer().getScheduler().runTaskTimer(TrilliumAPI.getInstance(), new Runnable() {

                @Override
                public void run() {
                    File players = new File(TrilliumAPI.getInstance().getDataFolder(), "/Trillium Group Manager/players");
                    File worlds = new File(TrilliumAPI.getInstance().getDataFolder(), "/Trillium Group Manager/worlds");

                    for (File f : players.listFiles()) {
                        if (f != null) {
                            //TODO: attachement reloader
                        }
                    }

                    for (File f : worlds.listFiles()) {
                        if (f != null) {
                            //TODO: attachement reloader.
                        }
                    }
                }
            }, 0, getConfig().getInt(Configuration.GM.RELOAD) * 20 * 3600);
        }
    }
}
