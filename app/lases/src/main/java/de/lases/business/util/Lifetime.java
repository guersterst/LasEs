package de.lases.business.util;

/**
 * Handles the system's start and shutdown.
 */
public class Lifetime {

    /**
     * On startup we initialize the resources used by the system.
     *
     * Specifically we read the config file and start:
     * <ul>
     *     <li> The datasource, specifically the connection pool </li>
     *     <li> The messaging service </li>
     *     <li> The logger </li>
     * </ul>
     */
    public static void startup() {

    }

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
     */
    public static void shutdown() {

    }

}
