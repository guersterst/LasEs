package de.lases.control.backing;

import de.lases.control.internal.SessionInformation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Locale;

/**
 * Backing bean for the footer
 */
@Named
@RequestScoped
public class FooterBacking {

    @Inject
    private SessionInformation sessionInformation;

    /**
     * Get the locale of the current request.
     *
     * @return The locale of the current request.
     */
    public Locale getLocale() {
        return null;
    }

}
