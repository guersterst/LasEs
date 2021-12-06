package de.lases.business.internal;

import de.lases.persistence.exception.ConfigNotReadableException;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Handles and propagates the system's start and shutdown.
 */
public class Lifetime {

    private static final Logger l = Logger.getLogger(Lifetime.class.getName());

    /**
     * On startup the resources used by the system are initialized.
     *
     * Specifically these resources are started:
     * <ul>
     *     <li> The datasource, specifically the connection pool, </li>
     *     <li> the messaging service </li>
     *     <li> and the logger. </li>
     * </ul>
     *
     * @param loggerConfigStream The logger config file.
     */
    public static void startup(InputStream loggerConfigStream) {
        initializeLogger(loggerConfigStream);
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

    private static void initializeLogger(InputStream loggerConfigStream) {
        // initialize logger with logger config file
        try {
            LogManager.getLogManager().readConfiguration(loggerConfigStream);
            l.info("Logger initialized");
        } catch (IOException e) {
            throw new ConfigNotReadableException("Could not read logger config file", e);
        }
    }


}
