package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.FoldersResponse;
import com.ditcherj.contextio.responses.SourcesResponse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonathan Ditcher on 02/04/2017.
 */
public class TestSources extends TestBase {

    @Test
    public void testGetSources() throws Exception {
        SourcesResponse response = this.contextIO.getSourcesList(this.accountId, null, null);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getSources());
        assertFalse(response.getSources().isEmpty());
    }

    @Test
    public void testGetFolders() throws Exception {
        FoldersResponse response = this.contextIO.getFolders(this.accountId, null, null, null);
        logger.trace("response[{}]", response);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getFolders());
        assertFalse(response.getFolders().isEmpty());
    }
}
