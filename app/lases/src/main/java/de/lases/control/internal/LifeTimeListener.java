package de.lases.control.internal;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listens for startup and shutdown of the system and executes initialization
 * and cleanup tasks respectively.
 */
@WebListener
public class LifeTimeListener implements ServletContextListener {

    /**
     * On shutdown we make sure all used resources are closed gracefully.
     *
     * We perform all already started operations on the following
     * resources and then make sure to free them properly:
     * <ul>
     *     <li> The datasource </li>
     *     <li> The messaging service </li>
     *     <li> The logger </li>
     * </ul>
     *
     * @param event The shutdown event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // TODO Automatisch erstellter Methoden-Stub

    }

    /**
     * On startup we initialize the resources used by the system.
     *
     * Specifically we read the config file and start:
     * <ul>
     *     <li> The datasource, specifically the connection pool </li>
     *     <li> The messaging service </li>
     *     <li> The logger </li>
     * </ul>
     *
     * @param event The startup event.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // TODO Automatisch erstellter Methoden-Stub

    }
}
