package net.gettrillium.trillium.api;

import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class TrilliumModule implements Listener {
    private ConfigurationSection config;

    public TrilliumModule() {
        this.config = TrilliumAPI.getInstance().getConfig();
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    protected TrilliumPlayer player(Player player) {
        return player(player.getName());
    }

    protected TrilliumPlayer player(String name) {
        return TrilliumAPI.getPlayer(name);
    }

    protected void register() {

    }
}
