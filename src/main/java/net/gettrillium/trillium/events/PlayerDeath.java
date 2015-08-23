package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.Configuration.Ability;
import net.gettrillium.trillium.api.Configuration.Chat;
import net.gettrillium.trillium.api.TrilliumAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.lang.reflect.InvocationTargetException;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();

        if (!TrilliumAPI.getInstance().getConfig().getBoolean(Chat.ENABLE_DEATH_MESSAGES)) {
            event.setDeathMessage(null);
        }

        if (TrilliumAPI.getInstance().getConfig().getBoolean(Ability.AUTO_RESPAWN)) {
            try {
                Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
                Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");

                for (Object ob : enumClass.getEnumConstants()) {
                    if (ob.toString().equals("PERFORM_RESPAWN")) {
                        packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
                    }
                }

                Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
            } catch (SecurityException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchFieldException | InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
