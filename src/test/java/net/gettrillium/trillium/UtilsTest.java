package net.gettrillium.trillium;

import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void timeToTickConverterTest() {
        assertEquals("'' -> 0 tick", 0, Utils.timeToTickConverter(""));
        assertEquals("'1s' -> 20 ticks", 20, Utils.timeToTickConverter("1s"));
        assertEquals("invalid variable -> 0", 0, Utils.timeToTickConverter("invalid variable"));
    }

    @Test
    public void TimeToStringTest() {
        assertEquals("00:01:00", Utils.TimeToString(60 * 20));
        assertEquals("00:01:05", Utils.TimeToString(65 * 20));
        assertEquals("00:03:20", Utils.TimeToString(200 * 20));
    }

    @Test
    public void getEncapsulationsTest() {
        ArrayList<String> ex = new ArrayList<>();
        ex.add(ChatColor.ITALIC + "*lol*");

        assertEquals(ex, Utils.redditReformat("haha *lol* potato"));

    }
}
