package de.lases.business.internal;

/**
 * Handles and propagates the system's start and shutdown.
 */
public class Lifetime {

    /**
     * On startup the resources used by the system are initialized.
     *
     * Specifically these resources are started:
     * <ul>
     *     <li> The datasource, specifically the connection pool, </li>
     *     <li> the messaging service </li>
     *     <li> and the logger. </li>
     * </ul>
     */
    public static void startup() {

    }

    /**
     * On shutdown all used resources are closed gracefully.
     *
     * All operations in process are finished
     * and then freed properly:
     * <ul>
     *     <li> The datasource, </li>
     *     <li> the messaging service and </li>
     *     <li> and the logger </li>
     * </ul>
     */
    public static void shutdown() {

    }

}
