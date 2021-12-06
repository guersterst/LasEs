package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.SystemSettings;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.DepletedResourceException;
import de.lases.persistence.exception.InvalidFieldsException;

/**
 * Offers get/update operations on the system settings and the
 * possibility to get and set the logo.
 */
public class SystemSettingsRepository {

    /**
     * Changes the system settings in the repository.
     *
     * @param systemSettings A fully filled systemSettings dto.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the system
     *                                settings is null.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static void updateSettings(SystemSettings systemSettings,
                                      Transaction transaction)
            throws DataNotWrittenException{
    }

    /**
     * Returns a dto with the system settings.
     *
     * @param transaction The transaction to use.
     * @return A fully filled {@code SystemSettings} dto.
     * @throws DepletedResourceException If the datasource cannot be
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
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getLogo(Transaction transaction) {
        return null;
    }

    /**
     * Sets the logo.
     *
     * @param logo A file dto filled with an image file.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If the file dto does not contain a byte
     *                                array.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static void setLogo(FileDTO logo, Transaction transaction)
            throws DataNotWrittenException{
    }

}
