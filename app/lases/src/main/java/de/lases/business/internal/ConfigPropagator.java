package de.lases.business.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
     * @param configFile The configuration properties to be set
     *                   as a {@link FileDTO} encapsulating an {@code InputStream}.
     */
    public void setProperties(FileDTO configFile) {
        configReader.setProperties(configFile);

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
