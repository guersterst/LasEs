package de.lases.global.config;

public class Configuration {

    private Configuration() {
    }

    private static final Configuration instance = new Configuration();

    public static synchronized Configuration getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return null;
    }

}
