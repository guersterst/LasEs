package de.lases.control.internal;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;

import java.io.Serial;
import java.io.Serializable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Provides the message bundle to the business layer.
 */
public class MessageBundleProducer {

    /**
     * Get the message bundle for the current locale.
     *
     * @return the message bundle for the current locale.
     */
    @Produces
    public PropertyResourceBundle getMessageBundle() {
        return null;
    }

}
