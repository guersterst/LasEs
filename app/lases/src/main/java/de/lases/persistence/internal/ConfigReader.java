package de.lases.persistence.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.ConfigNotReadableException;
import de.lases.persistence.exception.InvalidFieldsException;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gives access to the application's configuration.
 */
@ApplicationScoped
public class ConfigReader {

    private Properties props;

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
        InputStream inputStream = configFile.getInputStream();
        try {
            this.props = loadProperties(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
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