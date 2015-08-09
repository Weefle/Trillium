package net.gettrillium.trillium.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

/**
 * Created by hints on 8/9/2015.
 */
public class PlayerHomeEventTest {

    private static Player MOCK_PLAYER = PowerMockito.mock(Player.class);
    private static Location MOCK_FROM = PowerMockito.mock(Location.class);
    private static Location MOCK_TO = PowerMockito.mock(Location.class);

    private static PlayerHomeEvent EVENT;

    static {
        EVENT = new PlayerHomeEvent("asdf", MOCK_PLAYER, MOCK_FROM, MOCK_TO);
    }

    @Test
    public void getHomeNameTest() {
        Assert.assertEquals("EVENT.getHomeName() is the home name it was created with", "asdf", EVENT.getHomeName());
    }

    @Test
    public void getFromTest() {
        Assert.assertEquals("EVENT.getFrom() is the Location it was created with", MOCK_FROM, EVENT.getFrom());
    }

    @Test
    public void getToTest() {
        Assert.assertEquals("event.getTo() is the Location it was created with", MOCK_TO, EVENT.getTo());
    }

    @Test
    public void getPlayerTest() {
        Assert.assertEquals("event.getPlayer() is the Player it was created with", MOCK_PLAYER, EVENT.getPlayer());
    }

    @Test
    public void cancellationTest() {
        Assert.assertFalse("Event is not cancelled by default", EVENT.isCancelled());

        EVENT.setCancelled(true);
        Assert.assertTrue("Event.setCancelled(true) works", EVENT.isCancelled());

        EVENT.setCancelled(false);
        Assert.assertFalse("Event.setCancelled(false) works", EVENT.isCancelled());
    }
}
