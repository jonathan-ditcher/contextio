package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.ContactResponse;
import com.ditcherj.contextio.responses.ListContactsResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jonathan Ditcher on 02/04/2017.
 */
public class TestContacts extends TestBase {

    @Test
    public void testGetContact() throws Exception {
        ContactResponse response = this.contextIO.getContact(this.accountId, this.emailAddress);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertNotNull(response.getEmails());
        assertEquals(200, response.getCode());
        assertNotNull(response.getEmails().stream().filter(e -> e.equals(this.emailAddress)).findFirst().get());
    }

    @Test
    public void testGetContacts() throws Exception {
        ListContactsResponse response = this.contextIO.listContacts(this.accountId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertNotNull(response.getMatches());
        assertEquals(200, response.getCode());
        assertNotNull(response.getMatches().stream().filter(m -> m.getEmail().equals(this.emailAddress)).findFirst().get());
    }
}
