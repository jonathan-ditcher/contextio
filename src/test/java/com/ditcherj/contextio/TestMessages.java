package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.*;
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
    public void testGetMessageBody() throws Exception {
        ListMessagesResponse messages = this.contextIO.getMessages(this.accountId);

        MessageBodyResponse response = this.contextIO.getMessageBody(this.accountId, messages.getMessages().get(0).getMessage_id(), null);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getMessageBodies());
        assertFalse(response.getMessageBodies().isEmpty());
    }

    @Test
    public void testGetMessage() throws Exception {
        ListMessagesResponse messages = this.contextIO.getMessages(this.accountId);

        String messageId = messages.getMessages().get(0).getMessage_id();

        MessageResponse response = this.contextIO.getMessage(this.accountId, messageId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(messageId, response.getMessage_id());
    }

    @Test
    public void testGetFlags() throws Exception {
        ListMessagesResponse messages = this.contextIO.getMessages(this.accountId);

        String messageId = messages.getMessages().get(0).getMessage_id();

        FlagsResponse response = this.contextIO.getFlags(this.accountId, messageId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetMessageThread() throws Exception {
        ListMessagesResponse messages = this.contextIO.getMessages(this.accountId);

        String messageId = messages.getMessages().get(0).getMessage_id();

        MessageThreadResponse response = this.contextIO.getMessageThread(this.accountId, messageId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getEmail_message_ids());
        assertNotNull(response.getMessages());
        assertNotNull(response.getMessages().stream().filter(m -> m.getMessage_id().equals(messageId)).findFirst().get());
    }
}
