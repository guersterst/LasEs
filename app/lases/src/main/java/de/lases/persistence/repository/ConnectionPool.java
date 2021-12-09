package de.lases.persistence.repository;

import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.exception.DepletedResourceException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.inject.spi.CDI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds a set of open database connections that can be used by the program.
 */
public class ConnectionPool {

    private static final ConfigReader configReader = CDI.current().select(ConfigReader.class).get();

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_HOST = "bueno.fim.uni-passau.de";
    private static final String DB_NAME = "sep21g02t";
    private static final String DB_USER = "sep21g02";
    private static final String DB_PASSWORD = "ieQu2aeShoon";
    public static final int INITIAL_POOL_SIZE = 10;
    private static final int TIMEOUT = 3000;
    private static final String DB_URL
            = "jdbc:postgresql://" + DB_HOST + "/" + DB_NAME;

    private static final Logger logger
            = Logger.getLogger(Transaction.class.getName());

    private List<Connection> freeConnections;
    private List<Connection> usedConnections = new ArrayList<>();

    private static final ConnectionPool instance = new ConnectionPool();

    private boolean initialized;

    /**
     * Get the connection pool.
     *
     * @return The connection pool.
     */
    static ConnectionPool getInstance() {
        return instance;
    }

    /**
     * Get a connection out of the connection pool.
     *
     * @return A connection.
     * @throws IllegalStateException If the pool is not yet initialized.
     */
    synchronized Connection getConnection() {
        checkInitialized();

        long timestamp = System.currentTimeMillis() + TIMEOUT;

        while (freeConnections.isEmpty()) {
            try {
                this.wait(Math.max(timestamp - System.currentTimeMillis(), 1));
            } catch(InterruptedException ex) {
                logger.log(Level.WARNING, ex.getMessage());
                throw new DepletedResourceException("Connection pool has no "
                        + "more connections");
            }

            if (timestamp <= System.currentTimeMillis()) {
                throw new DepletedResourceException("Connection pool has no "
                        + "more connections");
            }
        }

        Connection connection = freeConnections
                .remove(freeConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    /**
     * Return a connection back to the connection pool.
     *
     * @param connection The connection to return.
     * @throws IllegalStateException If the pool is not yet initialized.
     * @throws IllegalArgumentException If the provided connection is not
     *                                  managed by the connection pool and
     *                                  thus cannot be returned.
     */
    synchronized void releaseConnection(Connection connection) {
        checkInitialized();
        if (usedConnections.contains(connection)) {
            freeConnections.add(connection);
            usedConnections.remove(connection);
            notifyAll();
        } else {
            throw new IllegalArgumentException("The connection was never part"
                    + "of the connection pool");
        }
    }

    /**
     * Initialize the connection pool.
     */
    public static void init() {
        synchronized (instance) {
            Logger logger = Logger.getLogger(ConnectionPool.class.getName());
            getInstance().initialized = true;
            List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                pool.add(createConnection(DB_URL, DB_USER, DB_PASSWORD));
            }
            getInstance().addConnections(pool);
            logger.info("DB Connection Pool started");
        }
    }

    /**
     * Shuts down the connection pool, freeing all connections.
     *
     * @throws IllegalStateException If the pool is not yet initialized or there
     *                               still are used connections.
     */
    public static void shutDown() {
        synchronized (instance) {
            instance.checkInitialized();

            long timestamp = System.currentTimeMillis() + TIMEOUT;

            while (!instance.usedConnections.isEmpty()) {
                try {
                    instance.wait(Math.max(timestamp - System.currentTimeMillis(), 1));
                } catch (InterruptedException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                    throw new IllegalStateException("Cannot shut down connection pool, "
                            + "shutdown was interrupted while waiting for used connections to be returned.");
                }

                if (timestamp <= System.currentTimeMillis()) {
                    throw new IllegalStateException("Cannot shut down connection pool, "
                            + "there still are used connections.");
                }
            }

            for (Connection conn : instance.freeConnections) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new DatasourceQueryFailedException("The connection cannot"
                            + " be closed. Shutdown failed");
                }
            }
            logger.info("DB Connection Pool stopped");
            getInstance().initialized = false;
        }
    }

    /**
     * Get the number of remaining connections in the pool.
     *
     * @return The number of remaining connections in the pool.
     */
    public int getNumberOfFreeConnections() {
        return freeConnections.size();
    }

    private static Connection createConnection(
            String url, String user, String password) {
        try {
            Class.forName(DB_DRIVER);
        }
        catch(ClassNotFoundException e) {
            System.err.println("JDBC Driver not found");
        }
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);
        props.setProperty("ssl", "true");
        props.setProperty("sslfactory",
                "org.postgresql.ssl.DefaultJavaSSLFactory");
        try {
            Connection connection =
                    DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch(SQLException ex) {
            throw new DatasourceQueryFailedException("Connection could not"
                    + "be created.", ex);
        }
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("the pool is not yet initialized");
        }
    }

    private void addConnections(List<Connection> connections) {
        this.freeConnections = connections;
    }

}
