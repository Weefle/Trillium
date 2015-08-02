package net.gettrillium.trillium.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void timeToTickConverterTest() {
        assertEquals(0, Utils.timeToTickConverter(""));
        assertEquals(20, Utils.timeToTickConverter("1s"));
        assertEquals(400, Utils.timeToTickConverter("20s"));
        assertEquals(12000, Utils.timeToTickConverter("10m"));
        assertEquals(1240, Utils.timeToTickConverter("2s1m"));
    }

    @Test
    public void TimeToStringTest() {
        assertEquals("00:00:01:00", Utils.timeToString(60 * 20));
        assertEquals("00:00:01:05", Utils.timeToString(65 * 20));
        assertEquals("00:00:03:20", Utils.timeToString(200 * 20));
    }
}
