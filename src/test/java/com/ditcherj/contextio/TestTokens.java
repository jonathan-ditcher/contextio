package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.ConnectTokensResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
}
