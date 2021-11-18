package de.lases.persistence.repository;

import java.sql.Connection;

/**
 * Holds a set of open database connections that can be used by the program.
 */
class ConnectionPool {

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
     */
    synchronized Connection getConnection() {
        return null;
    }

    /**
     * Return a connection back to the connection pool.
     *
     * @param connection The connection to return.
     */
    synchronized void releaseConnection(Connection connection) {
    }

    /**
     * Initialize the connection pool.
     */
    public static void init() {

    }

}
