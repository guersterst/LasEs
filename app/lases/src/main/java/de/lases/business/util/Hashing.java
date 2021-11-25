package de.lases.business.util;

/**
 * Provides functionality for the hashing of passwords.
 */
public class Hashing {

    private Hashing() {
    }

    /**
     * Generates a random salt for increased entropy.
     * The salts are 128 Bit long and are returned as base64 strings with
     * 24 characters.
     *
     * @return Returns the random salt.
     */
    public static String generateRandomSalt() {
        return null;
    }

    /**
     * Hashes a password.
     * The following rules are followed for hashing:
     * <ul>
     *     <li>
     *         The PBKDF2 hash function is used.
     *     </li>
     *     <li>
     *          The hashes are 128 Bit long
     *     </li>
     *     <li>
     *         The hashed are returned as base64 strings with a length of
     *         24 character.
     *     </li>
     * </ul>
     *
     * @param password The password to be hashed.
     * @param salt The salt for increased entropy.
     * @return Returns the hashed and salted password.
     */
    public static String hashWithGivenSalt(String password, String salt) {
        return null;
    }

}
