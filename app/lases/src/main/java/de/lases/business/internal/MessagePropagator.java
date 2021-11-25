package de.lases.business.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.internal.MessageReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Locale;

/**
 * Provides access to the message bundle by propagating the functionality of
 * the {@link MessageReader} to higher layers of the application.
 */
@ApplicationScoped
public class MessagePropagator {

    @Inject
    private MessageReader messageReader;

    /**
     * Sets the message bundle of the application.
     *
     * @param messageBundle The message bundle to be set
     *                      as a {@link FileDTO} encapsulating an {@code InputStream}
     *                      of the resource file.
     */
    public void setMessages(FileDTO messageBundle) {

    }

    /**
     * Gets a message from the message bundle for a key and a locale. If the
     * given locale is not supported, the standard message is returned.
     *
     * @param key The unique identifier of a message.
     * @param locale The locale for which to get the key
     * @return The message associated with the given key.
     */
    public String getMessage(String key, Locale locale) {
        return null;
    }

}
