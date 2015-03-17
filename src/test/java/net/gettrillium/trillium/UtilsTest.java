package net.gettrillium.trillium;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void timeToTickConverterTest() {
        assertEquals("'' -> 0 tick", 0, Utils.timeToTickConverter(""));
        assertEquals("'1s' -> 20 ticks", 20, Utils.timeToTickConverter("1s"));
        assertEquals("invalid variable -> 0", 0, Utils.timeToTickConverter("invalid variable"));
    }
}
