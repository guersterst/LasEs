package de.lases.persistence.repository;

import java.sql.Connection;

/**
 * Holds a set of open database connections that can be used by the program.
 */
class ConnectionPool {

    private static final ConnectionPool instance = new ConnectionPool();

    /**
     * Get the connection pool.
     *
     * @return The connection pool.
     */
    static ConnectionPool getInstance() {
        return null;
    }

    /**
     * Get a connection out of the connection pool.
     *
     * @return A connection.
     * @throws IllegalStateException If the pool is not yet initialized.
     */
    synchronized Connection getConnection() {
        return null;
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
    }

    /**
     * Initialize the connection pool.
     */
    public static synchronized void init() {

    }

    /**
     * Shuts down the connection pool, freeing all connections.
     *
     * @throws IllegalStateException If the pool is not yet initialized.
     */
    public static synchronized void shutDown() {

    }

}
