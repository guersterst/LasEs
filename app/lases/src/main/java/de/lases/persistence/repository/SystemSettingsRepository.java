package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.exception.InvalidFieldsException;

import java.sql.*;

/**
 * Offers get/update operations on the system settings and the
 * possibility to get and set the logo.
 */
public class SystemSettingsRepository {

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
    public static FileDTO getLogo(Transaction transaction) {
        return null;
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
    }

}
