package de.lases.control.internal;

import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.PropertyResourceBundle;

/**
 * Provides the message bundle to the business layer.
 */
public class MessageResourceBundleProducer {

    private static final String MESSAGE_RESOURCE_BUNDLE_NAME = "message";

    /**
     * Get the message bundle for the current locale.
     *
     * @return the message bundle for the current locale.
     */
    @Produces
    public PropertyResourceBundle getMessageResourceBundle() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (PropertyResourceBundle) facesContext.getApplication().getResourceBundle(facesContext, MESSAGE_RESOURCE_BUNDLE_NAME);
    }

}
