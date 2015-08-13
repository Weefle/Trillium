package net.gettrillium.trillium.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertNull;

public class UtilsTest {

    private static final String[] EMPTY_STRING_ARRAY = new String[]{};

    @Test
    public void stringSplitterTest() {
        Assert.assertArrayEquals("null input string returns empty array", EMPTY_STRING_ARRAY, Utils.stringSplitter(null, 1));
        Assert.assertArrayEquals("blank input string returns empty array", EMPTY_STRING_ARRAY, Utils.stringSplitter("", 1));
        Assert.assertArrayEquals("interval of 0 returns an array with only the input string", new String[]{"abcde"}, Utils.stringSplitter("abcde", 0));
        Assert.assertArrayEquals("interval of 1 vs 1 length string", new String[]{"a"}, Utils.stringSplitter("a", 1));
        Assert.assertArrayEquals("interval of 1 vs 2 length string", new String[]{"a", "b"}, Utils.stringSplitter("ab", 1));
        Assert.assertArrayEquals("interval of 2 vs 4 length string", new String[]{"ab", "cd"}, Utils.stringSplitter("abcd", 2));
    }

    @Test
    public void compareVersionTest() {
        Assert.assertFalse("return false if our version is null", Utils.compareVersion("0.1", null));
        Assert.assertFalse("return false if new version is null", Utils.compareVersion(null, "0.1"));
        Assert.assertTrue("0.2 is newer than 0.1", Utils.compareVersion("0.2", "0.1"));
        Assert.assertFalse("0.1 is older than 0.2", Utils.compareVersion("0.1", "0.2"));
        Assert.assertTrue("0.2.1 is newer than 0.2", Utils.compareVersion("0.2.1", "0.2"));
        Assert.assertFalse("0.2 is older than 0.2.1", Utils.compareVersion("0.2", "0.2.1"));
    }

    @Test
    public void timeToTickConverterTest() {
        Assert.assertEquals("blank goes to zero", 0, Utils.timeToTickConverter(""));
        Assert.assertEquals("1 second goes to 20 ticks", 20, Utils.timeToTickConverter("1s"));
        Assert.assertEquals("20 seconds goes to 400 ticks", 400, Utils.timeToTickConverter("20s"));
        Assert.assertEquals("10 minutes goes to 12000 ticks", 12000, Utils.timeToTickConverter("10m"));
        Assert.assertEquals("1m2s to 1240 ticks", 1240, Utils.timeToTickConverter("2s1m"));
        Assert.assertEquals("1h1s to 3601 ticks", 72020, Utils.timeToTickConverter("1h1s"));
        Assert.assertEquals("1d1s to 86401 ticks", 1728020, Utils.timeToTickConverter("1d1s"));
    }

    @Test
    public void arrayToStringTest() {
        Assert.assertEquals("null returns blank string", "", Utils.arrayToString(null));
        Assert.assertEquals("blank array returns blank string", "", Utils.arrayToString(EMPTY_STRING_ARRAY));
        Assert.assertEquals("{a, b, c, d} returns a;b;c;d", "a;b;c;d", Utils.arrayToString(new String[]{"a", "b", "c", "d"}));
    }

    @Test
    public void arrayToReadableStringTest() {
        Assert.assertEquals("Return blank string on null array", "", Utils.arrayToReadableString(null));
        Assert.assertEquals("Return blank string on empty array", "", Utils.arrayToReadableString(EMPTY_STRING_ARRAY));
        Assert.assertEquals("[a, b, c, d] -> \"a, b, c, d\"", "a, b, c, d", Utils.arrayToReadableString(new String[]{"a", "b", "c", "d"}));
    }

    @Test
    public void arrayFromStringTest() {
        Assert.assertArrayEquals("null returns null", null, Utils.arrayFromString(null));
        Assert.assertArrayEquals("a;b;c;d returns {a, b, c, d}", new String[]{"a", "b", "c", "d"}, Utils.arrayFromString("a;b;c;d"));
    }

    @Test
    public void timeToStringTest() {
        Assert.assertEquals("60 seconds", "00:00:01:00", Utils.timeToString(60 * 20));
        Assert.assertEquals("65 seconds", "00:00:01:05", Utils.timeToString(65 * 20));
        Assert.assertEquals("200 seconds", "00:00:03:20", Utils.timeToString(200 * 20));
    }

    @Test
    public void locationSerializerTest() {
        World w = PowerMockito.mock(World.class);
        PowerMockito.when(w.getName()).thenReturn("worldname");

        Location loc = PowerMockito.mock(Location.class);
        PowerMockito.when(loc.getWorld()).thenReturn(w);
        PowerMockito.when(loc.getX()).thenReturn(0.0D);
        PowerMockito.when(loc.getY()).thenReturn(1.0D);
        PowerMockito.when(loc.getZ()).thenReturn(2.0D);
        PowerMockito.when(loc.getPitch()).thenReturn(3F);
        PowerMockito.when(loc.getYaw()).thenReturn(4F);

        Assert.assertEquals("location serializes properly", "worldname%0.0%1.0%2.0%3.0%4.0", Utils.locationSerializer(loc));
    }

    @Test
    public void commandBlockifyTest() {
        Player p = PowerMockito.mock(Player.class);
        PowerMockito.when(p.getName()).thenReturn("lolname");

        assertNull("null input string returns null output string", Utils.commandBlockify(null, p));
        Assert.assertEquals("null plaeyr returns original input string", "asdflol @p", Utils.commandBlockify("asdflol @p", null));
        Assert.assertEquals("Replacing @p with player name works", "asdf lolname asdf", Utils.commandBlockify("asdf @p asdf", p));
    }
}
