package de.lases.persistence.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.ConfigNotReadableException;
import de.lases.persistence.exception.InvalidFieldsException;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Gives access to the message bundle.
 */

@ApplicationScoped
public class MessageReader {

    /**
     * Sets the message bundle of the application.
     *
     * @param bundle The message bundle to be set as a {@link FileDTO}
     *               encapsulating an {@code InputStream}
     *
     * @throws InvalidFieldsException If the file dto does not have an input
     *                                stream.
     * @throws ConfigNotReadableException If the message bundle file cannot
     *                                    be read.
     */
    public void setMessages(FileDTO bundle) {

    }

    /**
     * Gets a message of the application's message bundle.
     *
     * @param key The unique identifier of a message.
     * @return The message associated with the given key.
     */
    public String getMessage(String key) {
        return null;
    }

}
