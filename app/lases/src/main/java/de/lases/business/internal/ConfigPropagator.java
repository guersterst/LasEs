package de.lases.business.internal;

import de.lases.persistence.repository.ConfigReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;

/**
 * Provides access to the configuration by propagating
 * the functionality of the {@link ConfigReader} to higher layers of the application.
 */
@ApplicationScoped
public class ConfigPropagator {

    @Inject
    private ConfigReader configReader;

    /**
     * Sets the configured properties of the application.
     *
     * @param inputStream The properties to be set.
     */
    public void setProperties(InputStream inputStream) {
        configReader.setProperties(inputStream);

    }

    /**
     * Gets a property of the application's configuration.
     *
     * @param key The unique identifier of a property.
     * @return The property's value associated with the given key.
     */
    public String getProperty(String key) {
        return configReader.getProperty(key);
    }
}
