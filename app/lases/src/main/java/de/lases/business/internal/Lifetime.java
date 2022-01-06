package de.lases.business.internal;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.ConfigNotReadableException;
import de.lases.persistence.repository.ConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Handles and propagates the system's start and shutdown.
 *
 * @author Johann Schicho
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
     *
     * @param loggerConfig File containing the logger properties as InputStream.
     */
    public static void startup(FileDTO loggerConfig) {
        initializeLogger(loggerConfig.getInputStream());
        initializeDBPool();
        initializePeriodicWorker();
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
        PeriodicWorker.stop();
        ConnectionPool.shutDown();
    }

    private static void initializeLogger(InputStream loggerConfigStream) {
        // initialize logger with logger config file
        try {
            LogManager.getLogManager().readConfiguration(loggerConfigStream);
            Logger.getLogger(Lifetime.class.getName()).info("Logger initialized");
        } catch (IOException e) {
            throw new ConfigNotReadableException("Could not read logger config file", e);
        }
    }

    private static void initializeDBPool() {
        ConnectionPool.init();
    }

    private static void initializePeriodicWorker() {
        PeriodicWorker.init();
    }
}
