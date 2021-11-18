package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the initial configuration page.
 */
@RequestScoped
@Named
public class InitialConfigBacking {

    @Inject
    private CustomizationService customizationService;

    private String datasourceConnectionState;

    /**
     * Load the state of the database connection into this bean.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Create the datasource and return the welcome page.
     *
     * @return Show the welcome page.
     */
    public String createDatasource() {
        return null;
    }

    /**
     * Get information about the connection state of the datasource and an
     * error description if it is not connected.
     *
     * @return Information about the connection state of the datasource.
     */
    public String getDatasourceConnectionState() {
        return datasourceConnectionState;
    }
}
