package net.gettrillium.trillium.api;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void stringSplitterTest() {
        Assert.assertArrayEquals("null input string returns empty array", new String[] {}, Utils.stringSplitter(null, 1));
        Assert.assertArrayEquals("blank input string returns empty array", new String[] {}, Utils.stringSplitter("", 1));
        Assert.assertArrayEquals("interval of 0 returns an array with only the input string", new String[] {"abcde"}, Utils.stringSplitter("abcde", 0));
        Assert.assertArrayEquals("interval of 1 vs 1 length string", new String[] {"a"}, Utils.stringSplitter("a", 1));
        Assert.assertArrayEquals("interval of 1 vs 2 length string", new String[] {"a", "b"}, Utils.stringSplitter("ab", 1));
        Assert.assertArrayEquals("interval of 2 vs 4 length string", new String[] {"ab", "cd"}, Utils.stringSplitter("abcd", 2));
    }

    @Test
    public void compareVersionTest() {
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
    }

    @Test
    public void arrayToStringTest() {
        Assert.assertEquals("null returns null", null, Utils.arrayToString(null));
        Assert.assertEquals("blank array returns blank string", "", Utils.arrayToString(new String[] {}));
        Assert.assertEquals("{a, b, c, d} returns a;b;c;d", "a;b;c;d", Utils.arrayToString(new String[] {"a", "b", "c", "d"}));
    }

    @Test
    public void timeToStringTest() {
        Assert.assertEquals("60 seconds", "00:00:01:00", Utils.timeToString(60 * 20));
        Assert.assertEquals("65 seconds", "00:00:01:05", Utils.timeToString(65 * 20));
        Assert.assertEquals("200 seconds", "00:00:03:20", Utils.timeToString(200 * 20));
    }
}
