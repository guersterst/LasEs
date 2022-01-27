package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.global.transport.SystemSettings;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the imprint view.
 *
 * @author Stefanie Gürster
 */
@RequestScoped
@Named
public class ImprintBacking {

    @Inject
    private CustomizationService customizationService;

    private SystemSettings systemSettings;

    /**
     * Load the dto that contains the imprint from the database.
     */
    @PostConstruct
    public void init() {
        systemSettings = customizationService.get();
    }

    /**
     * Get the dto with the system settings, which includes the imprint.
     *
     * @return Dto with the system settings.
     */
    public SystemSettings getSystemSettings() {
        return systemSettings;
    }
}
