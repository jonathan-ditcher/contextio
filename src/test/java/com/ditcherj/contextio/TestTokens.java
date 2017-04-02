package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.ConnectTokensResponse;
import com.ditcherj.contextio.responses.CreateConnectTokenResponse;
import com.ditcherj.contextio.responses.SimpleResponse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonathan Ditcher on 02/04/2017.
 */
public class TestTokens extends TestBase {

    @Test
    public void testGetTokens() throws Exception {
        ConnectTokensResponse response = this.contextIO.getConnectTokens(this.accountId);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertNotNull(response.getTokens());
        assertEquals(200, response.getCode());
        assertNotNull(response.getTokens().stream().filter(t -> t.getAccount().getId().equals(this.accountId)).findFirst().get());
    }

    @Test
    public void testCreateDeleteToken() throws Exception {
        String callbackUrl = "http://www.google.com";

        CreateConnectTokenResponse response = this.contextIO.createConnectToken(this.accountId, callbackUrl);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(201, response.getCode());

        SimpleResponse simpleResponse = this.contextIO.deleteConnectToken(this.accountId, response.getToken());
        logger.trace("delete response[{}]", simpleResponse);

        assertNotNull(simpleResponse);
        assertEquals(200, response.getCode());
        assertTrue(simpleResponse.getSuccess());
    }
}
