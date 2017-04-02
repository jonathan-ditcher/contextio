package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.ListMessagesResponse;
import com.ditcherj.contextio.responses.MessageBodyResponse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonathan Ditcher on 02/04/2017.
 */
public class TestMessages extends TestBase {

    @Test
    public void testGetMessages() throws Exception {
        ListMessagesResponse response = this.contextIO.getMessages(this.accountId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertNotNull(response.getMessages());
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetMessage() throws Exception {
        ListMessagesResponse messages = this.contextIO.getMessages(this.accountId);

        MessageBodyResponse response = this.contextIO.getMessageBody(this.accountId, messages.getMessages().get(0).getMessage_id(), null);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getMessageBodies());
        assertFalse(response.getMessageBodies().isEmpty());
    }
}
