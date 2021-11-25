package de.lases.persistence.util;

import de.lases.global.transport.ConnectionState;

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
     * Tests if the datasource is connected.
     *
     * @return the connection state of the datasource.
     */
    public static ConnectionState testDatasourceConnection() {
        return null;
    }

}
