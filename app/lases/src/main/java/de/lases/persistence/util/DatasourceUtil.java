package de.lases.persistence.util;

import de.lases.global.transport.ConnectionState;
import de.lases.persistence.exception.DatasourceNotFoundException;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.repository.Transaction;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Offers methods for managing the datasource.
 *
 * @author Thomas Kirz
 * @author Sebastian Vogt
 */
public class DatasourceUtil {

    private static final Logger logger = Logger.getLogger(DatasourceUtil.class.getName());

    private static final String SQL_FILE_LOCATION = "/WEB-INF/classes/de/lases/persistence/util/CREATE_ALL.sql";

    /**
     * Deletes all users/verifications (if the user already has a valid email
     * address they will not be deleted) from the datasource that need to verify
     * their email address but did not do so within 1 hour.
     * Users that are (co-)authors of submissions are not deleted.
     *
     * @author Thomas Kirz
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
    public static void createDatasource() throws IOException {
        ServletContext sctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String sql = new String(sctx.getResourceAsStream(SQL_FILE_LOCATION).readAllBytes(),
                StandardCharsets.UTF_8);
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            transaction.commit();
        } catch (SQLException e) {
            logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceNotFoundException(e.getMessage());
        }
    }

    /**
     * Tests if the datasource is connected.
     *
     * @return the connection state of the datasource.
     */
    public static ConnectionState testDatasourceConnection() {
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();

        ConnectionState connectionState = new ConnectionState();
        connectionState.setSuccessfullyConnected(false);
        connectionState.setDatasourceSchemaCreated(false);
        connectionState.setErrorMessage("Connected");

        // Check if the connection works.
        try {
            connectionState.setSuccessfullyConnected(conn.isValid(10));
        } catch (SQLException e) {
            connectionState.setErrorMessage(e.getMessage());
            logSQLException(e, logger);

            // fail
            transaction.abort();
            return connectionState;
        }

        // Check if the datasource has been created.
        try {
            // If review exists, so does submission, paper and user.
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM review");
            ps.executeQuery();
            ps.close();

            // system is essential.
            ps = conn.prepareStatement("SELECT * FROM system");
            ps.executeQuery();
            ps.close();
        } catch (SQLException e) {
            connectionState.setErrorMessage("DB schema has not been created yet.");
            logSQLException(e, logger);

            // fail
            transaction.abort();
            return connectionState;
        }

        connectionState.setDatasourceSchemaCreated(true);
        transaction.abort();
        return connectionState;
    }

    /**
     * Log the contents of an SQL exception.
     *
     * @param sqlException An sql exception.
     * @param logger The logger to user.
     *
     * @author Sebastian Vogt
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
