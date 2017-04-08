package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.AccountResponse;
import com.ditcherj.contextio.responses.AccountsResponse;
import com.ditcherj.contextio.responses.AddEmailAddressResponse;
import com.ditcherj.contextio.responses.ListEmailAddressesResponse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public class TestAccounts extends TestBase {

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

    @Test
    public void testEmailAddresses() throws Exception {

        ListEmailAddressesResponse response = this.contextIO.getEmailAddresses(this.accountId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertNotNull(response.getEmailAddresses());
        assertEquals(200, response.getCode());
        assertNotNull(response.getEmailAddresses().stream().filter(a -> a.getEmail().equals(this.emailAddress)).findFirst().get());
    }

    @Test
    public void testAddEmailAddress() throws Exception {

        String email = "some"+Long.valueOf(System.currentTimeMillis()/1000).intValue()+"@email.com";

        AddEmailAddressResponse response = this.contextIO.addEmailAddress(this.accountId, email);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertEquals(200, response.getCode());
        assertEquals(email, response.getEmail_address());
    }
}
