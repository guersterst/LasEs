package de.lases.control.backing;

import de.lases.business.service.CustomizationService;
import de.lases.global.transport.ConnectionState;
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

    private ConnectionState connectionState;

    /**
     * Load the state of the database connection into this bean.
     */
    @PostConstruct
    public void init() throws IllegalAccessException {
        connectionState = customizationService.getConnectionState();
        if (connectionState.isDatasourceSchemaCreated()) {
            throw new IllegalAccessException();
        }
    }

    /**
     * Create the datasource and return the welcome page.
     *
     * @return Show the welcome page.
     */
    public String createDatasource() {
        customizationService.createDataSourceSchema();
        return "/views/anonymous/welcome.xhtml?faces-redirect=true";
    }

    /**
     * Information about the current Datasource Connection.
     * @return {@link ConnectionState}
     */
    public ConnectionState getConnectionState() {
        return connectionState;
    }
}
