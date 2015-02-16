package net.gettillium.trillium.modules;

import net.gettillium.trillium.api.TrilliumAPI;
import net.gettillium.trillium.api.TrilliumModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CoreModule extends TrilliumModule {
    public CoreModule() {
        super("core");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        TrilliumAPI.createNewPlayer(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        TrilliumAPI.getPlayer(e.getPlayer().getName()).dispose();
    }
}
