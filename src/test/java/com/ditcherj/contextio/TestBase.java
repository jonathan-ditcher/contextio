package com.ditcherj.contextio;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public class TestBase {

    protected String key;
    protected String secret;
    protected String accountId;

    public TestBase() {
        this.buildProperties();
    }

    private void buildProperties() {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream("contextio.properties"));
            this.key = properties.getProperty("key");
            this.secret = properties.getProperty("secret");
            this.accountId = properties.getProperty("accountId");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
