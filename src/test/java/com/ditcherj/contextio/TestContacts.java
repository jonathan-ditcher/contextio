package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.ContactResponse;
import com.ditcherj.contextio.responses.ListContactsResponse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jonathan Ditcher on 02/04/2017.
 */
public class TestContacts extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestContacts.class);

    private ContextIO contextIO;

    @Before
    public void setUp() throws Exception {
        this.contextIO = new ContextIO(this.key, this.secret);
    }

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
