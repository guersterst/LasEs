package de.lases.persistence.repository;

import de.lases.persistence.exception.DatasourceQueryFailedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Holds a set of open database connections that can be used by the program.
 */
public class ConnectionPool {

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_HOST = "bueno.fim.uni-passau.de";
    private static final String DB_NAME = "sep21g02t";
    private static final String DB_USER = "sep21g02";
    private static final String DB_PASSWORD = "ieQu2aeShoon";
    private static final int INITIAL_POOL_SIZE = 10;

    private static final String DB_URL
            = "jdbc:postgresql://" + DB_HOST + "/" + DB_NAME;

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
        } else {
            throw new IllegalArgumentException("The connection was never part"
                    + "of the connection pool");
        }
    }

    /**
     * Initialize the connection pool.
     */
    public static synchronized void init() {
        Logger logger = Logger.getLogger(ConnectionPool.class.getName());
        getInstance().initialized = true;
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                pool.add(createConnection(DB_URL, DB_USER, DB_PASSWORD));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        getInstance().addConnections(pool);
        logger.info("DB Connection Pool started");
    }

    /**
     * Shuts down the connection pool, freeing all connections.
     *
     * @throws IllegalStateException If the pool is not yet initialized.
     */
    public static synchronized void shutDown() {
        for (Connection conn: instance.freeConnections) {
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new DatasourceQueryFailedException("The connection cannot"
                        + "be closed");
            }
        }

        getInstance().initialized = false;
    }

    private static Connection createConnection(
            String url, String user, String password)
            throws SQLException {
        try {
            Class.forName(DB_DRIVER);
        }
        catch(ClassNotFoundException e) {
            System.err.println("JDBC Driver not found");
        }
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);
        props.setProperty("ssl", "true");   // necessary!
        props.setProperty("sslfactory",
                "org.postgresql.ssl.DefaultJavaSSLFactory");
        Connection connection =
                DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
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
