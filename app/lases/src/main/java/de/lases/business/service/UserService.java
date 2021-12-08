package de.lases.business.service;

import de.lases.control.internal.LifeTimeListener;
import de.lases.control.internal.UIMessageGenerator;
import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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
     * Updates a user.
     *
     * @param newUser A {@link User}-DTO. The required fields are:
     *                <ul>
     *                <li> id </li>
     *                <li> a hashed password and a password salt </li>
     *                <li> the first name </li>
     *                <li> the last name </li>
     *                <li> the email address </li>
     *                </ul>
     *                         If empty they will be deleted other fields are optional
     *                         and will not be deleted if empty.
     *                   When the email address is changed the verification process is initiated
     *                   using the {@code EmailUtil} utility.
     */
    public void change(User newUser) {
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
    public void addScienceField(User user, ScienceField scienceField) {
    }

    /**
     * Removes an area of expertise, e.g. a {@link ScienceField} from a {@link User}.
     *
     * @param user         The user who loses a {@code ScienceField}.
     *                     Must contain a valid id.
     * @param scienceField A {@code ScienceField} containing a valid id.
     */
    public void removeScienceField(User user, ScienceField scienceField) {
    }

    /**
     * Gets a sorted and filtered list of {@link User}s.
     *
     * @param resultListParams Parameters, that control filtering and sorting of the resulting list.
     * @return A list of {@link User}-DTOs.
     */
    public List<User> getList(ResultListParameters resultListParams) {
        Transaction transaction = new Transaction();
        Logger l = Logger.getLogger(UserService.class.getName());
        List<User> userList = new LinkedList<>();

        try {
            userList = UserRepository.getList(transaction, resultListParams);
        } catch (DataNotCompleteException e) {
            l.info(e.getMessage());
            uiMessageEvent.fire(new UIMessage("Could not fetch data.", MessageCategory.WARNING));
        } catch (InvalidQueryParamsException e) {
            l.severe(e.getMessage());
            uiMessageEvent.fire(new UIMessage("Missing request parameters.", MessageCategory.FATAL));
        } finally {
            transaction.commit();
        }
        return userList;
    }


    /**
     * Gets a list of all {@link User}s involved with a certain {@link Submission}.
     *
     * @param privilege  The role of the requested {@code User}s.
     *                   May be {@code REVIEWER} for reviewers,
     *                   {@code AUTHOR} for (co)-authors.
     *                   {@code ADMIN} and {@code EDITOR} are not supported.
     * @param submission The submission the users should stand in a
     *                   relationship with. Must be filled with a valid id.
     * @return A list of {@code User}s involved with a {@code Submission} in a certain role.
     */
    public List<User> getList(Submission submission, Privilege privilege) {
        Transaction transaction = new Transaction();
        Logger l = Logger.getLogger(UserService.class.getName());
        List<User> userList = new LinkedList<>();

        try {
            userList = UserRepository.getList(transaction, submission, privilege);
        } catch (NotFoundException e) {
            l.info(e.getMessage());
            uiMessageEvent.fire(new UIMessage("This submission does not exist.", MessageCategory.ERROR));
        } catch (DataNotCompleteException e) {
            l.info(e.getMessage());
            uiMessageEvent.fire(new UIMessage("Could not fetch data.", MessageCategory.WARNING));
        } finally {
            transaction.commit();
        }
        return userList;
    }

    /**
     * Gets all {@link User}s that are editors of a specific {@link ScientificForum}.
     *
     * @param scientificForum The forum where the editors are required. Must
     *                        contain a valid id.
     * @return A list of editors for a certain forum.
     */
    public List<User> getList(ScientificForum scientificForum) {
        Transaction transaction = new Transaction();
        Logger l = Logger.getLogger(UserService.class.getName());
        List<User> userList = new LinkedList<>();

        try {
            userList = UserRepository.getList(transaction, scientificForum);
        } catch (DataNotCompleteException e) {
            l.info(e.getMessage());
            uiMessageEvent.fire(new UIMessage("Could not fetch data.", MessageCategory.WARNING));
        } catch (NotFoundException e) {
            l.info(e.getMessage());
            uiMessageEvent.fire(new UIMessage("This forum does not exist.", MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
        return userList;
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
