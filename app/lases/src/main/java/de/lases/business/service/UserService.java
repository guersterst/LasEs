package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Provides all functionality for creating, manipulating or receiving information about users in the application.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class UserService implements Serializable {

    @Serial
    private static final long serialVersionUID = 6294576521701043448L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Gets a {@code User}.
     *
     * @param user A {@link User}-DTO, that should contain an existing id value.
     * @return A {@code User}-DTO filled with all available fields.
     */
    public User get(User user) throws IllegalArgumentException {
        return null;
    }

    /**
     * Manipulates a user.
     *
     * @param newUser A {@link User}-DTO filled with the fields that are desired to be changed.
     *                <p>
     *                All fields filled with legal values will be overwritten, the rest are ignored.
     *                It should contain an existing id value.
     *                When the email address is changed the verification process is initiated
     *                using the {@code EmailUtil} utility.
     *                </p>
     */
    public void change(User newUser) throws IllegalArgumentException {
    }

    /**
     * Deletes a {@code User} from the application's data.
     *
     * @param user A {@link User}-DTO containing a valid id.
     */
    public void remove(User user) {
    }

    /**
     * Determines whether an email is already in use in this applications.
     *
     * @param user The {@link User} to be removed,
     *             containing a valid id or email-address.
     * @return {@code true}, if the email does already exist. {@code false} otherwise.
     */
    public boolean emailExists(User user) {
        return false;
    }

    /**
     * Sets a user's avatar.
     * <p>
     * The avatar is parsed into a thumbnail using the
     * {@link de.lases.business.util.AvatarUtil} utility.
     * </p>
     *
     * @param avatar The {@link FileDTO} containing the image as a byte-array.
     * @param user   The {@link User} whose avatar is being set.
     *               Must contain a valid id.
     */
    public void setAvatar(FileDTO avatar, User user) {
    }

    /**
     * Gets the user's avatar.
     *
     * @param user The {@link User} whose avatar is being requested.
     *             Must contain a valid id.
     * @return The user's avatar as a byte-array, wrapped by a {@code FileDTO}.
     */
    public FileDTO getAvatar(User user) {
        return null;
    }

    /**
     * Deletes a user's avatar.
     *
     * @param user The {@link User} whose avatar is being deleted.
     *             Must contain a valid id.
     */
    public void deleteAvatar(User user) {
    }

    /**
     * Adds an area of expertise, e.g. a {@link ScienceField} to a {@link User}.
     *
     * @param user         The user who receives a new {@code ScienceField}.
     *                     Must contain a valid id.
     * @param scienceField A {@code ScienceField} containing a valid id.
     */
    public static void addScienceField(User user, ScienceField scienceField) {
    }

    /**
     * Removes an area of expertise, e.g. a {@link ScienceField} from a {@link User}.
     *
     * @param user         The user who loses a {@code ScienceField}.
     *                     Must contain a valid id.
     * @param scienceField A {@code ScienceField} containing a valid id.
     */
    public static void removeScienceField(User user, ScienceField scienceField) {
    }

    /**
     * Gets a sorted and filtered list of users.
     *
     * @param resultListParams Parameters, that control filtering and sorting of the resulting list.
     * @return A list of {@link User}-DTOs.
     */
    public List<User> getList(ResultListParameters resultListParams) {
        return null;
    }

    /**
     * Determines whether a users email-address is already verified.
     *
     * @param user The {@link User} whose {@link Verification} is requested.
     *             Must contain a valid id.
     * @return A fully filled {@code Verification} dto.
     */
    public Verification getVerification(User user) {
        return null;
    }
}
