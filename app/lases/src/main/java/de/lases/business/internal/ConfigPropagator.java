package de.lases.business.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

/**
 * Provides access to the configuration by propagating
 * the functionality of the {@link ConfigReader} to higher layers of the application.
 */
@ApplicationScoped
public class ConfigPropagator {

    @Inject
    private ConfigReader configReader;

    private final Logger logger = Logger.getLogger(ConfigPropagator.class.getName());

    /**
     * Sets the configured properties of the application.
     *
     * @param configFile The configuration properties to be set
     *                   as a {@link FileDTO} encapsulating an {@code InputStream}.
     * @throws InvalidFieldsException If the file dto does not have an input stream.
     */
    public void setProperties(FileDTO configFile) {
        if (configFile.getInputStream() == null) {
            logger.severe("The properties from the configuration could not be set due to the internal"
                    + "configuration file not containing an input stream.");
            throw new InvalidFieldsException("The config file's input-stream may not be null.");
        } else {
            configReader.setProperties(configFile);
        }

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
