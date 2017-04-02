package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.AccountResponse;
import com.ditcherj.contextio.responses.AccountsResponse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public class TestAccounts extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestAccounts.class);

    private ContextIO contextIO;

    @Before
    public void setUp() throws Exception {
        this.contextIO = new ContextIO(this.key, this.secret);
    }

    @Test
    public void testGetAccount() throws Exception {
        AccountResponse response = this.contextIO.getAccount(this.accountId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(this.accountId, response.getId());
    }

    @Test
    public void testGetAccounts() throws Exception {
        AccountsResponse response = this.contextIO.getAccounts();
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertNotNull(response.getAccounts());
        assertEquals(200, response.getCode());
        assertNotNull(response.getAccounts().stream().filter(a -> a.getId().equals(this.accountId)).findFirst().get());
    }
}
