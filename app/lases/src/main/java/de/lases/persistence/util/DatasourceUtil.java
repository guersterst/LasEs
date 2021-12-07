package de.lases.persistence.util;

import de.lases.global.transport.ConnectionState;
import de.lases.persistence.exception.DatasourceNotFoundException;
import de.lases.persistence.repository.Transaction;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Offers methods for managing the datasource.
 */
public class DatasourceUtil {

    /**
     * Deletes all users/email addresses (if the user already has a valid email
     * address he will not be deleted) from the datasource that need to verify
     * their email address but did not do so within 1 hour.
     */
    public static void cleanUpVerifications() {
    }

    /**
     * Creates the schema of the datasource.
     *
     * @throws DatasourceNotFoundException If the datasource cannot be
     *                                     reached.
     */
    public static void createDatasource() {

    }

    /**
     * Tests if the datasource is connected.
     *
     * @return the connection state of the datasource.
     */
    public static ConnectionState testDatasourceConnection() {
        return null;
    }

    /**
     * Log the contents of an SQL exception.
     *
     * @param sqlException An sql exception.
     * @param logger The logger to user.
     */
    public static void logSQLException(SQLException sqlException,
                                       Logger logger) {
        logger.log(Level.SEVERE,
                """
                Message: %s
                SQLState: %s
                Vendor error code: %s
                """.formatted(sqlException.getMessage(),
                        sqlException.getSQLState(), sqlException.getErrorCode())
        );
    }

}
