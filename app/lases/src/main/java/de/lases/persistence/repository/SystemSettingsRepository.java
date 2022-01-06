package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;
import de.lases.persistence.util.TransientSQLExceptionChecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import java.sql.*;

/**
 * Offers get/update operations on the system settings and the
 * possibility to get and set the logo.
 *
 * @author Stefanie Gürster
 */
public class SystemSettingsRepository {

    private static final Logger logger = Logger.getLogger(SystemSettingsRepository.class.getName());

    /**
     * Changes the system settings in the repository.
     *
     * @param systemSettings A fully filled systemSettings dto.
     * @param transaction    The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the fields of the system
     *                                        settings is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void updateSettings(SystemSettings systemSettings,
                                      Transaction transaction)
            throws DataNotWrittenException {
        if (systemSettings.getImprint() == null || systemSettings.getCompanyName() == null
                || systemSettings.getHeadlineWelcomePage() == null || systemSettings.getMessageWelcomePage() == null
                || systemSettings.getStyle() == null) {
            logger.severe("One of the fields in systemSettings is null.");
            throw new InvalidFieldsException("Fields in systemSettings are null.");
        }

        Connection connection = transaction.getConnection();

        String sql = "UPDATE system SET company_name = ?, welcome_heading = ? " +
                ", welcome_description = ?, css_theme = ?, imprint = ? WHERE id = 0";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, systemSettings.getCompanyName());
            statement.setString(2, systemSettings.getHeadlineWelcomePage());
            statement.setString(3, systemSettings.getMessageWelcomePage());
            statement.setString(4, systemSettings.getStyle());
            statement.setString(5, systemSettings.getImprint());

            statement.executeUpdate();
        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);

            if (TransientSQLExceptionChecker.isTransient(exception.getSQLState())) {
                throw new DataNotWrittenException("Data not written while updating system settings.");
            }
            transaction.abort();
            throw new DatasourceQueryFailedException("A datasource exception occurred while changing the system settings.");
        }
    }

    /**
     * Returns a dto with the system settings.
     *
     * @param transaction The transaction to use.
     * @return A fully filled {@code SystemSettings} dto.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static SystemSettings getSettings(Transaction transaction) throws DatasourceQueryFailedException {
        Connection conn = transaction.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM system");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                SystemSettings systemSettings = new SystemSettings();

                systemSettings.setCompanyName(rs.getString("company_name"));
                systemSettings.setHeadlineWelcomePage(rs.getString("welcome_heading"));
                systemSettings.setMessageWelcomePage(rs.getString("welcome_description"));
                systemSettings.setStyle(rs.getString("css_theme"));
                systemSettings.setImprint(rs.getString("imprint"));
                return systemSettings;
            }
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException();
        }
        throw new DatasourceQueryFailedException("No system settings found.");
    }

    /**
     * Get the image file for the logo.
     *
     * @param transaction The transaction to use.
     * @return A file containing the logo.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getLogo(Transaction transaction) throws NotFoundException, DataNotWrittenException {

        String sql = """
                SELECT logo_image
                FROM system
                """;

        FileDTO logo = new FileDTO();
        ResultSet logoResult;

        try {
            Connection conn = transaction.getConnection();
            PreparedStatement logoStatement = conn.prepareStatement(sql);
            logoResult = logoStatement.executeQuery();

            if (logoResult.next()) {
                byte[] logoBytes = logoResult.getBytes(1);
                logo.setFile(logoBytes);
            } else {
                logger.severe("The logo could not be found in the query results.");
                throw new NotFoundException("The logo could not be found in the query results.");
            }

            if (logoResult.next()) {
                throw new IllegalStateException("There must not be two 'system' entries in the database.");
            }
        } catch (SQLException ex) {
            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                logger.warning("The logo could not be fetched.");
                throw new DataNotWrittenException("The logo could not be fetched.", ex);
            } else {
                DatasourceUtil.logSQLException(ex, logger);
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
        return logo;
    }

    /**
     * Sets the logo.
     *
     * @param logo        A file dto filled with an image file.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If the file dto does not contain a byte
     *                                        array.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setLogo(FileDTO logo, Transaction transaction)
            throws DataNotWrittenException {
        if (logo == null || logo.getFile() == null) {
            logger.severe("The given FileDTO or its contained image was null.");
            throw new InvalidFieldsException();
        }

        @SuppressWarnings({"'Update' statement without 'where' updates all table rows at once"})
        String sql = """
                UPDATE system
                SET logo_image = ?
                """;

        try {
            Connection conn = transaction.getConnection();
            PreparedStatement setLogoStatement = conn.prepareStatement(sql);
            setLogoStatement.setBytes(1, logo.getFile());
            setLogoStatement.executeUpdate();
        } catch (SQLException ex) {
            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                logger.warning("The logo could not be updated into the database.");
                throw new DataNotWrittenException("The logo could not be uploaded", ex);
            } else {
                transaction.abort();
                DatasourceUtil.logSQLException(ex, logger);
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }

}
