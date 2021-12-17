package de.lases.business.service;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.util.AvatarUtil;
import de.lases.business.util.EmailUtil;
import de.lases.business.util.Hashing;
import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
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

    private static final Logger l = Logger.getLogger(UserService.class.getName());

    @Inject
    private PropertyResourceBundle propertyResourceBundle;

    @Inject
    private ConfigPropagator configPropagator;

    /**
     * Gets a {@code User}.
     *
     * @param user A {@link User}-DTO, that should contain an existing id or email value.
     * @return A {@code User}-DTO filled with all available fields.
     */
    public User get(User user)  {
        if (user.getId() == null && user.getEmailAddress() == null) {

            // Throw an exception when neither an id nor a valid email address exist.
            l.severe("The id and email are missing. Therefor no user object can be queried.");
            throw new InvalidFieldsException();
        } else {

            Transaction transaction = new Transaction();
            User result = null;
            try {
                result = UserRepository.get(user, transaction);
                transaction.commit();
                l.finest("Successfully fetched a user with the id: " + user.getId());
            } catch (NotFoundException ex) {
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNotFound"),
                        MessageCategory.ERROR));
                l.warning(ex.getMessage());
                transaction.abort();
            }
            return result;
        }
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
        Transaction transaction = new Transaction();

        if (newUser.getPasswordNotHashed() != null) {
            newUser.setPasswordSalt(Hashing.generateRandomSalt());
            newUser.setPasswordHashed(Hashing.hashWithGivenSalt(newUser.getPasswordNotHashed(),
                    newUser.getPasswordSalt()));
        }

        try {
            User oldUser = UserRepository.get(newUser, transaction);
            User newUserOldEmail = oldUser.clone();
            newUserOldEmail.setEmailAddress(oldUser.getEmailAddress());

            UserRepository.change(newUserOldEmail, transaction);

            if (newUserOldEmail.getEmailAddress().equals(newUser.getEmailAddress())) {
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataSaved"),
                        MessageCategory.INFO));
                transaction.commit();
            } else if (reinitiateVerificationProcess(newUser, transaction)) {
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataSavedWithEmail"),
                        MessageCategory.INFO));
                transaction.commit();
            } else {
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNorWritten"),
                        MessageCategory.ERROR));
                transaction.abort();
            }
        } catch (NotFoundException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("userNotFound"),
                    MessageCategory.ERROR));
        } catch (KeyExistsException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("emailInUse"),
                    MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNorWritten"),
                    MessageCategory.ERROR));
        }
    }

    private boolean reinitiateVerificationProcess(User user, Transaction t) {

        Verification verification = new Verification();
        verification.setVerified(false);
        verification.setUserId(user.getId());
        verification.setNonVerifiedEmailAddress(user.getEmailAddress());
        verification.setValidationRandom(Hashing.generateRandomSalt());
        verification.setTimestampValidationStarted(LocalDateTime.now(ZoneOffset.UTC));

        try {
            UserRepository.changeVerification(verification, t);
            l.fine("Verification for user " + user.getId() + " created.");
        } catch (NotFoundException | DataNotWrittenException e) {
            return false;
        }

        String emailBody = propertyResourceBundle.getString("email.verification.body.0")
                + user.getFirstName()
                + propertyResourceBundle.getString("email.verification.body.1")
                + generateValidationUrl(verification);

        try {
            EmailUtil.sendEmail(configPropagator.getProperty("MAIL_ADDRESS_FROM"), new String[]{user.getEmailAddress()},
                    null, propertyResourceBundle.getString("email.verification.subject"), emailBody);
        } catch (EmailTransmissionFailedException e) {
            return false;
        }

        // UIMessage containing verification random if test mode is enabled
        if (configPropagator.getProperty("DEBUG_AND_TEST_MODE").equalsIgnoreCase("true")) {
            uiMessageEvent.fire(new UIMessage(generateValidationUrl(verification), MessageCategory.INFO));
        }

        return true;
    }

    private String generateValidationUrl(Verification verification) {
        String base = configPropagator.getProperty("BASE_URL") + "/views/anonymous/verification.xhtml";
        return FacesContext.getCurrentInstance().getExternalContext().encodeBookmarkableURL(base,
                Map.of("validationRandom", List.of(verification.getValidationRandom())));
    }

    /**
     * Deletes a {@code User} from the application's data.
     *
     * @param user A {@link User}-DTO containing a valid id.
     */
    public void remove(User user) {
        Transaction transaction = new Transaction();

        try {
            UserRepository.remove(user, transaction);
            transaction.commit();
        } catch (NotFoundException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dateNotFound"),
                    MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNotWritten"),
                    MessageCategory.ERROR));
            transaction.abort();
        }
    }

    /**
     * Determines whether an email is already in use in this applications.
     *
     * @param user The {@link User} to be removed,
     *             containing a valid id or email-address.
     * @return {@code true}, if the email does already exist. {@code false} otherwise.
     * @author Thomas Kirz
     */
    public boolean emailExists(User user) {
        Transaction t = new Transaction();
        boolean exists = UserRepository.emailExists(user, t);
        t.commit();
        return exists;
    }

    /**
     * Sets a user's avatar. If the avatar file or the contained byte[] are null, the current avatar will be removed.
     * <p>
     * The avatar is parsed into a thumbnail using the
     * {@link de.lases.business.util.AvatarUtil} utility.
     * </p>
     *
     * @param avatar The {@link FileDTO} containing the image as a byte-array.
     * @param user   The {@link User} whose avatar is being set.
     *               Must contain a valid id.
     *
     * @author Sebastian Vogt
     */
    public void setAvatar(FileDTO avatar, User user) {
        Transaction transaction = new Transaction();
        try {
            UserRepository.setAvatar(user, avatar == null ? null : AvatarUtil.generateThumbnail(avatar), transaction);
            transaction.commit();
        } catch (DataNotWrittenException | IOException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNorWritten"),
                    MessageCategory.ERROR));
        } catch (NotFoundException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("userNotFound"),
                    MessageCategory.ERROR));
        }
    }

    /**
     * Gets the user's avatar. Is null if the user has no avatar.
     *
     * @param user The {@link User} whose avatar is being requested.
     *             Must contain a valid id.
     * @return The user's avatar as a byte-array, wrapped by a {@code FileDTO}.
     *
     * @author Sebstian Vogt
     */
    public FileDTO getAvatar(User user) {
        Transaction transaction = new Transaction();

        try {
            FileDTO avatar = UserRepository.getAvatar(user, transaction);
            transaction.commit();
            return avatar;
        } catch (DataNotCompleteException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("avatarNotLoaded"),
                    MessageCategory.ERROR));
        } catch (NotFoundException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("userNotFound"),
                    MessageCategory.ERROR));
        }
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
     *
     * @author Sebastian Vogt
     */
    public void addScienceField(User user, ScienceField scienceField) {
        Transaction transaction = new Transaction();
        try {
            UserRepository.addScienceField(user, scienceField, transaction);
            transaction.commit();
        } catch (NotFoundException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNotWritten"), MessageCategory.ERROR));
        }

    }

    /**
     * Removes an area of expertise, e.g. a {@link ScienceField} from a {@link User}.
     *
     * @param user         The user who loses a {@code ScienceField}.
     *                     Must contain a valid id.
     * @param scienceField A {@code ScienceField} containing a valid id.
     *
     * @author Sebastian Vogt
     */
    public void removeScienceField(User user, ScienceField scienceField) {
        Transaction transaction = new Transaction();
        try {
            UserRepository.removeScienceField(user, scienceField, transaction);
            transaction.commit();
        } catch (NotFoundException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {
            transaction.abort();
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("dataNotWritten"), MessageCategory.ERROR));
        }
    }

    /**
     * Gets a sorted and filtered list of {@link User}s.
     *
     * @param resultListParams Parameters, that control filtering and sorting of the resulting list.
     * @return A list of {@link User}-DTOs.
     */
    public List<User> getList(ResultListParameters resultListParams) {
        Transaction transaction = new Transaction();
        List<User> userList = new ArrayList<>();

        try {
            l.finest("Getting user list with result list parameters.");
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
        List<User> userList = new ArrayList<>();

        try {
            l.finest("Getting user list for submission with privilege.");
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
        List<User> userList = new ArrayList<>();

        try {
            l.finest("Getting user list of editors of forum.");
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

    /**
     * Verifies a user's email-address.
     *
     * @param verification The {@link Verification}, containing the validation random, otherwise
     *                     a UIMessage is fired.
     * @return The filled {@link Verification} with verified set to true if the verification
     *         was successful.
     * @author Thomas Kirz
     */
    public Verification verify(Verification verification) {
        if (verification.getValidationRandom() == null) {
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("verification.noRandom"),
                    MessageCategory.ERROR));
            return verification;
        }

        Transaction transaction = new Transaction();
        Verification storedVerification;
        try {
            storedVerification = UserRepository.getVerification(verification, transaction);
        } catch (NotFoundException e) {
            transaction.abort();
            l.warning("Could not find verification.");
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("verification.failure"),
                    MessageCategory.ERROR));
            return verification;
        }

        if (storedVerification.isVerified()) {
            transaction.abort();
            l.warning("User already verified");
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("verification.alreadyVerified"),
                    MessageCategory.ERROR));
            return verification;
        }

        if (verification.getValidationRandom().equals(storedVerification.getValidationRandom())) {
            storedVerification.setVerified(true);
            storedVerification.setValidationRandom(null);
            try {
                UserRepository.changeVerification(storedVerification, transaction);
                l.info("Successfully verified user with id " + storedVerification.getUserId());
            } catch (NotFoundException e) {
                transaction.abort();
                l.severe("Could not update verification as the user does not exist.");
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("verification.failure"),
                        MessageCategory.ERROR));
            } catch (DataNotWrittenException e) {
                transaction.abort();
                l.severe("Verification could not be updated in data source.");
                uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("verification.failure"),
                        MessageCategory.ERROR));
            }
        }

        transaction.commit();
        uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("verification.success"),
                MessageCategory.INFO));
        return storedVerification;
    }
}
