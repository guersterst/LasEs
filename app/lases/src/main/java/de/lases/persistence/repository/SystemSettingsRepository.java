package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.exception.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Offers get/update operations on the system settings and the
 * possibility to get and set the logo.
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
    }

    /**
     * Returns a dto with the system settings.
     *
     * @param transaction The transaction to use.
     * @return A fully filled {@code SystemSettings} dto.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static SystemSettings getSettings(Transaction transaction) {
        return null;
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

        // TODO kein access auf interne id -> müsste in dto gespeichert sein
        // companyName als identifier good enough?
        if (logo == null || logo.getFile() == null) {
            logger.severe("The given FileDTO or its contained image was null.");
            throw new InvalidFieldsException();
        }

        /*
        SystemSettings settings = getSettings(transaction);
        if (settings == null || settings.getCompanyName() == null) {
            //TODO
            throw new DatasourceQueryFailedException();
        }
         */

        String sql = """
                UPDATE system
                SET logo_image = ?
                WHERE company_name = ?
                """;

        try {
            Connection conn = transaction.getConnection();
            PreparedStatement setLogoStatement = conn.prepareStatement(sql);
            setLogoStatement.setBytes(1, logo.getFile());
            setLogoStatement.setString(2, "LasEs");
            //setLogoStatement.setString(2, settings.getCompanyName());
            setLogoStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
