package net.gettrillium.trillium;

import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void MessageUtilTest() {
        Message msg = new Message("TestCommand", Error.NO_PERMISSION);
        assertEquals("§8[§4TestCommand§8] §9>> §7You don't have permission to do that.", msg.toString());
    }
}
