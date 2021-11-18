package de.lases.global.config;

import java.util.Properties;

public class Configuration {

    private Configuration() {
    }

    private Properties properties;

    private static final Configuration instance = new Configuration();

    public static synchronized Configuration getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return null;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
