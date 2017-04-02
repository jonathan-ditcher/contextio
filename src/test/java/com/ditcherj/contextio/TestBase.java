package com.ditcherj.contextio;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public abstract class TestBase {

    protected static final Logger logger = LoggerFactory.getLogger(TestBase.class);

    protected String key;
    protected String secret;
    protected String accountId;
    protected String emailAddress;
    protected ContextIO contextIO;

    @Before
    public void setUp() throws Exception {
        this.contextIO = new ContextIO(this.key, this.secret);
    }

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
            this.emailAddress = properties.getProperty("emailAddress");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
