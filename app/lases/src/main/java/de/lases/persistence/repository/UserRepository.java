package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

/**
 * Offers get/add/change/remove operations on a user and the
 * possibility to get lists of users.
 */
public class UserRepository {

    /**
     * Takes a user dto that is filled with a valid id or a valid
     * email address and returns a fully filled user dto.
     *
     * @param user A {@code User} dto that must be filled
     *             with a valid id or email.
     * @param transaction The transaction to use.
     * @return A fully filled {@code User} dto.
     * @throws NotFoundException If there is no user with the
     *                           provided id or email.
     * @throws InvalidFieldsException If both id and email are provided, but
     *                                they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static User get(User user, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Adds a user to the repository.
     *
     * @param user A user dto with all required fields. Required are:
     *             <ul>
     *             <li> a hashed password and a password salt </li>
     *             <li> the first name </li>
     *             <li> the last name </li>
     *             <li> the email address </li>
     *             </ul>
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If there is already a user with
     *                            the same id or email.
     * @throws InvalidFieldsException If one of the required fields of the
     *                                scientific forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(User user, Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
    }

    /**
     * Changes the given user in the repository.
     *
     * @param user A user dto with all required fields. Required are:
     *             <ul>
     *             <li> a hashed password and a password salt </li>
     *             <li> the first name </li>
     *             <li> the last name </li>
     *             <li> the email address </li>
     *             </ul>
     *             The rest of the fields will be deleted if left empty.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no user with the
     *                           provided id or email.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If there is already a user with
     *                            the same id or email.
     * @throws InvalidFieldsException If one of the fields of the user is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(User user, Transaction transaction)
            throws NotFoundException, DataNotWrittenException,
            KeyExistsException {
    }

    /**
     * Takes a user dto that is filled with a valid id or email address and
     * removes this user from the repository.
     *
     * @param user The user forum to remove. Must be filled
     *             with a valid id or email.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified user forum was not found in
     *                           the repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If both id and email are provided, but
     *                                they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(User user, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Takes a user dto that is filled with a valid id or a valid
     * email address and returns the verification dto that belongs to the user.
     *
     * @param user A {@code User} dto that must be filled
     *             with a valid id or email.
     * @param transaction The transaction to use.
     * @return A fully filled {@code Verification} dto.
     * @throws NotFoundException If there is no user with the
     *                           provided id or email.
     * @throws InvalidFieldsException If both id and email are provided, but
     *                                they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public Verification getVerification(User user, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Gets a list of all users.
     *
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled user dtos.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<User> getList(Transaction transaction,
                                     ResultListParameters resultListParameters)
            throws DataNotCompleteException {
        return null;
    }

    /**
     * Adds the specified science field to the specified scientific user.
     *
     * @param user A user dto with a valid id or email.
     * @param scienceField A science field dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no user with the
     *                           provided id or email or there is no science
     *                           field with the provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If both id and email are provided, but
     *                                they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addScienceField(User user, ScienceField scienceField,
                                       Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Removes the specified science field from the specified user.
     *
     * @param user A user dto with a valid id or email.
     * @param scienceField A science field dto with a valid id that belongs to
     *                     the specified user.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no user with the
     *                           provided id or email or there is no science
     *                           field with the provided id or the science field
     *                           does not belong to the specified user.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If both id and email are provided, but
     *                                they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeScienceField(User user, ScienceField scienceField,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Return if a user with the specified email address exists.
     *
     * @param user A user dto with an email address.
     * @param transaction The transaction to use.
     * @return Is there a user with the specified email address in the
     *         repository.
     * @throws InvalidFieldsException If the email address of the supplied user
     *                                is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static boolean emailExists(User user, Transaction transaction) {
        return false;
    }

    /**
     * Get the image file for the avatar.
     *
     * @param transaction The transaction to use.
     * @return A file containing the logo.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getAvatar(Transaction transaction) {
        return null;
    }

    /**
     * Sets the avatar.
     *
     * @param avatar A file dto filled with an image file.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setAvatar(FileDTO avatar, Transaction transaction)
            throws DataNotWrittenException {
    }

}
