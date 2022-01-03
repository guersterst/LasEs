package de.lases.persistence.util;

import de.lases.global.transport.ConnectionState;
import de.lases.persistence.exception.DatasourceNotFoundException;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.repository.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Offers methods for managing the datasource.
 */
public class DatasourceUtil {

    private static final Logger logger = Logger.getLogger(DatasourceUtil.class.getName());

    /**
     * Deletes all users/verifications (if the user already has a valid email
     * address they will not be deleted) from the datasource that need to verify
     * their email address but did not do so within 1 hour.
     * Users that are (co-)authors of submissions are not deleted.
     */
    public static void cleanUpVerifications() {
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();

        String sql = """
                DELETE
                FROM "user" u
                WHERE (SELECT is_verified
                       FROM verification v
                       WHERE u.id = v.id) = FALSE
                  AND (SELECT timestamp_validation_started
                       FROM verification v
                       WHERE u.id = v.id) < NOW() - '1 hour'::INTERVAL
                  AND NOT EXISTS(
                        SELECT 1
                        FROM submission s
                        WHERE u.id = s.author_id
                    )
                  AND NOT EXISTS(
                        SELECT 1
                        FROM co_authored ca
                        WHERE u.id = ca.user_id
                    );
                """;

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            logger.info("Cleaning up verifications.");
            statement.executeUpdate();
            transaction.commit();
        } catch (SQLException e) {
            logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("Could not clean up verifications.", e);
        }
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
                SQLException with the following attributes:
                - Class: %s
                - Message: %s
                - SQLState: %s
                - Vendor error code: %s
                """.formatted(sqlException.getClass(),
                        sqlException.getMessage(), sqlException.getSQLState(),
                        sqlException.getErrorCode())
        );
    }

}
