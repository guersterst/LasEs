package de.lases.global.config;

import java.util.HashMap;

public class Configuration {

    private Configuration() {
    }

    private HashMap<String, String> configuration;

    private static final Configuration instance = new Configuration();

    public static synchronized Configuration getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return null;
    }

    public void setConfig(HashMap<String, String> config) {

    }

}
