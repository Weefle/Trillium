package net.gettrillium.trillium.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void timeToTickConverterTest() {
        assertEquals("blank goes to zero", 0, Utils.timeToTickConverter(""));
        assertEquals("1 second goes to 20 ticks", 20, Utils.timeToTickConverter("1s"));
        assertEquals("20 seconds goes to 400 ticks", 400, Utils.timeToTickConverter("20s"));
        assertEquals("10 minutes goes to 12000 ticks", 12000, Utils.timeToTickConverter("10m"));
        assertEquals("1m2s to 1240 ticks", 1240, Utils.timeToTickConverter("2s1m"));
    }

    @Test
    public void timeToStringTest() {
        assertEquals("60 seconds", "00:00:01:00", Utils.timeToString(60 * 20));
        assertEquals("65 seconds", "00:00:01:05", Utils.timeToString(65 * 20));
        assertEquals("200 seconds", "00:00:03:20", Utils.timeToString(200 * 20));
    }
}
