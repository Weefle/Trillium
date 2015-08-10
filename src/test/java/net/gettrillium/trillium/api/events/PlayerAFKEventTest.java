package net.gettrillium.trillium.api.events;

import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

/**
 * Created by hints on 8/10/2015.
 */
public class PlayerAFKEventTest {

    private static Player MOCK_PLAYER = PowerMockito.mock(Player.class);

    private static PlayerAFKEvent IS_AFK;
    private static PlayerAFKEvent ISNT_AFK;

    static {
        IS_AFK = new PlayerAFKEvent(MOCK_PLAYER, true);
        ISNT_AFK = new PlayerAFKEvent(MOCK_PLAYER, false);
    }

    @Test
    public void isAfkTest() {
        Assert.assertTrue("Event constructed with afk=true isAfk()", IS_AFK.isAFK());
        Assert.assertFalse("Event constructed with afk=false isAfk()", ISNT_AFK.isAFK());
    }

    @Test
    public void getPlayerTest() {
        Assert.assertEquals("event.getPlayer() is the Player it was created with", MOCK_PLAYER, IS_AFK.getPlayer());
    }

    @Test
    public void cancellationTest() {
        Assert.assertFalse("Event is not cancelled by default", IS_AFK.isCancelled());

        IS_AFK.setCancelled(true);
        Assert.assertTrue("Event.setCancelled(true) works", IS_AFK.isCancelled());

        IS_AFK.setCancelled(false);
        Assert.assertFalse("Event.setCancelled(false) works", IS_AFK.isCancelled());
    }
}
