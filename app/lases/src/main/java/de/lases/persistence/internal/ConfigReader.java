package de.lases.persistence.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.ConfigNotReadableException;
import de.lases.persistence.exception.InvalidFieldsException;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Gives access to the application's configuration.
 */
@ApplicationScoped
public class ConfigReader {

    private Properties props;

    private final Logger logger = Logger.getLogger(ConfigReader.class.getName());

    /**
     * Sets the configured properties of the application.
     *
     * @param configFile The configuration properties to be set
     *                   as a {@link FileDTO} encapsulating an
     *                   {@code InputStream}.
     * @throws InvalidFieldsException If the file dto does not have an input
     *                                stream.
     * @throws ConfigNotReadableException If the configuration file cannot
     *                                    be read.
     */
    public void setProperties(FileDTO configFile) {
        if (configFile.getInputStream() == null) {
            logger.severe("The properties from the configuration could not be set due to the internal"
                    + "configuration file not containing an input stream.");
            throw new InvalidFieldsException("The config file's input-stream may not be null.");
        } else {
            try {
                this.props = loadProperties(configFile.getInputStream());
                logger.info("The configuration's properties were set.");
            } catch (IOException e) {
                logger.severe("The properties from the configuration could not be set due to a failure"
                        + "in loading.");
                throw new ConfigNotReadableException(e.getMessage(), e);
            }
        }
    }

    private static Properties loadProperties(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }

    /**
     * Gets a property of the application's configuration.
     *
     * @param key The unique identifier of a property.
     * @return The property's value associated with the given key.
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
