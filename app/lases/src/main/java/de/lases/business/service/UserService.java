package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Provides all functionality for creating, manipulating or receiving information about users in the application.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class UserService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    /**
     * Gets a {@code User}-DTO.
     *
     * @param user A {@link User}-DTO that should contain an existing id value.
     * @return A {@code User}-DTO filled with all available fields.
     */
    public User getUser(User user) throws IllegalArgumentException {
        return null;
    }

    /**
     * Manipulates a {@code User}-DTO.
     *
     * @param newUser A {@link User}-DTO filled with the fields that are desired to be changed.
     *                <p>
     * All fields filled with legal values will be overwritten, the rest are ignored.
     * It should contain an existing id value.
     */
    public void editUser(User newUser) throws IllegalArgumentException {
    }

    /**
     * Deletes a {@code User} from the applications data.
     *
     * @param user A {@link User}-DTO containing a valid id.
     */
    public void deleteUser(User user) {
    }

    /**
     * Determines whether an email is already in use in this applications.
     *
     * @param user A {@link User}-DTO containing a valid id and an email-adress.
     * @return {@code true}, if the email does already exist. {@code false} otherwise.
     */
    public boolean emailExists(User user) {
        return false;
    }

    /**
     * Sets a users avatar.
     * <p>
     * The avatar is parsed into a thumbnail.
     *
     * @param file The {@link File} containing the image as a byte-array.
     */
    public void setAvatar(File file) {
    }

    /**
     * Gets the users avatar.
     *
     * @return The users avatar as a byte-array, wrapped by a {@code File}.
     */
    public File getAvatar() {
        return null;
    }

    /**
     * Deletes a users avatar.
     *
     * @param user A {@link User}-DTO containing a valid id.
     */
    public void deleteAvatar(User user) {
    }

    /**
     * Gets a sorted list of users. filterung, sortierung
     *
     * @param privilege The view privileges of the calling user.
     * @param resultListParams Allows for filtering and sorting of the resulting list.
     * @return A list of {@link User}-DTOs.
     */
    public List<User> getUsers(Privilege privilege, ResultListParameters resultListParams) {
        return null;
    }

    /**
     * Verifies a user's email-adress.
     *
     * @param verification A {@code Verification}-DTO. Must conatain a valid id and a validationRandom.
     * @return {@code true} if the verification process was succesful. {@code false} otherwise.
     */
    public boolean verify(Verification verification) {
        return false;
    }

    /**
     * Determines whether a users email-adress is already verified.
     * @param user A {@link User}-DTO which should contain a valid id.
     * @return {@true} if the user is verified. {@code false otherwise}.
     */
    public boolean isVerified(User user) {
        return false;
    }
}
