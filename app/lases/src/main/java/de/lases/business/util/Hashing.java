package de.lases.business.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Provides functionality for the hashing of passwords.
 *
 * @author Sebastian Vogt
 */
public class Hashing {

    /**
     * The length of the salt strings returned by generateRandomSalt()
     */
    public static final int SALT_LENGTH_BASE64 = 24;

    /**
     * Salt length in BYTE.
     */
    private static final int SALT_LENGTH_BYTE = SALT_LENGTH_BASE64 * 4 / 6;

    /**
     * Hash length in BIT.
     */
    private static final int HASH_LENGTH_BIT = 128;

    /**
     * Number of hashing iterations.
     */
    private static final int ITERATIONS = 75000;

    private Hashing() { }

    /**
     * Generates a random salt for increased entropy in hashing.
     * The salts are 128 Bit long and are returned as base64 strings with
     * 24 characters.
     *
     * @return The random salt.
     */
    public static String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH_BYTE];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
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
     * @return The hashed and salted password.
     */
    public static String hashWithGivenSalt(String password, String salt) {
        byte[] saltArr = Base64.getDecoder().decode(salt);
        byte[] hash = hash(password, saltArr);
        return Base64.getEncoder().encodeToString(hash);
    }

    private static byte[] hash(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS,
                HASH_LENGTH_BIT);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Password hashing failed because the hash algorithm"
                    + "was not found. This error is fatal.");
        }
        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new Error("Password hashing failed because the key "
                    + "specification does not match the hash algorithm. This"
                    + "error is fatal.");
        }
        return hash;
    }


}
