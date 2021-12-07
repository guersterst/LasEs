package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a user and the
 * possibility to get lists of users.
 */
public class UserRepository {

    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());

    /**
     * Takes a user dto that is filled with a valid id or a valid
     * email address and returns a fully filled user dto.
     *
     * @param user        A {@code User} dto that must be filled
     *                    with a valid id or email.
     * @param transaction The transaction to use.
     * @return A fully filled {@code User} dto.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id or email.
     * @throws InvalidFieldsException         If both id and email are provided, but
     *                                        they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static User get(User user, Transaction transaction)
            throws NotFoundException {
        if (user.getId() == null && user.getEmailAddress() == null) {

            // Throw an exception when neither an id nor a valid email address exist.
            String msg = "The id and email are missing. Therefor no user object can be queried.";
            logger.severe(msg);
            throw new IllegalArgumentException(msg);
        }

        String sql_number_of_submissions_and_editor_info = """
                 SELECT (SELECT COUNT(*)
                 FROM submission, "user"
                 WHERE "user".id = submission.author_id
                   AND "user".id = ?) as number_of_submissions,
                (SELECT member_of.editor_id
                 FROM "user", member_of
                 WHERE "user".id = ?
                   AND member_of.editor_id = "user".id) as editor_id
                                 """;

        String sql_user = """
                SELECT *
                FROM "user"
                WHERE "user".id = ? OR "user".email_address = ?
                """;

        Connection conn = transaction.getConnection();
        User result;

        try {

            // Attempt to query for the user.
            ResultSet userResult;
            PreparedStatement userStatement = conn.prepareStatement(sql_user);
            userStatement.setInt(1, user.getId());
            userStatement.setString(2, user.getEmailAddress());
            userResult = userStatement.executeQuery();

            // Attempt to query for the user's editor status and number of submissions.
            ResultSet submissionAndEditorResult;
            PreparedStatement extraStatement = conn.prepareStatement(sql_number_of_submissions_and_editor_info);
            extraStatement.setInt(1, user.getId());
            extraStatement.setInt(2, user.getId());
            submissionAndEditorResult = extraStatement.executeQuery();


            // Attempt to create a user from the query results.
            if (userResult.next() && submissionAndEditorResult.next()) {
                result = createUserFromResultSet(userResult, submissionAndEditorResult);

                if (userResult.next()) {

                    // There cannot be two results of such a query.
                    String msg = "There are two results of a unique user query. The parameters were"
                            + " id: " + user.getId() + ", email: " + user.getEmailAddress();
                    logger.severe(msg);
                    throw new InvalidFieldsException(msg);
                }
            } else {
                String msg = "Error while loading a user with the id: " + user.getId()
                        + " and email: " + user.getEmailAddress();
                logger.fine(msg);
                throw new NotFoundException(msg);
            }
        } catch (SQLException ex) {
            throw new DatasourceQueryFailedException(ex.getMessage());
        }
        return result;
    }

    private static User createUserFromResultSet(ResultSet userResult, ResultSet submissionAndEditorResult) throws SQLException {

        // Regular required user data.
        User result = new User();
        result.setId(userResult.getInt("id"));
        result.setVerificationId(userResult.getInt("id"));
        result.setEmailAddress(userResult.getString("email_address"));
        result.setAdmin(userResult.getBoolean("is_administrator"));
        result.setFirstName(userResult.getString("firstname"));
        result.setLastName(userResult.getString("lastname"));

        Date birthDate = userResult.getDate("birthdate");
        if (birthDate != null) {
            result.setDateOfBirth(birthDate.toLocalDate());
        }
        result.setPasswordHashed(userResult.getString("password_hash"));
        result.setRegistered(userResult.getBoolean("is_registered"));

        // Optional data about a user.
        result.setTitle(userResult.getString("title"));
        result.setEmployer(userResult.getString("employer"));

        // Set the list of all relevant global privileges.
        List<Privilege> privileges = new ArrayList<>();
        if (result.isAdmin()) {
            privileges.add(Privilege.ADMIN);
        }
        if (result.isRegistered()) {
            privileges.add(Privilege.AUTHENTICATED);
        }

        // Set the extra data gathered from other database entities.
        try {
            if (submissionAndEditorResult.getInt("editor_id") != 0) {
                privileges.add(Privilege.EDITOR);
            }
        } catch (SQLException ex) {

            // Do nothing.
        }
        result.setPrivileges(privileges);

        try {
            result.setNumberOfSubmissions(submissionAndEditorResult.getInt("number_of_submissions"));
        } catch (SQLException ex) {

            // Do nothing.
        }
        return result;
    }

    /**
     * Adds a user to the repository.
     *
     * @param user        A user dto with all required fields. Required are:
     *                    <ul>
     *                    <li> a hashed password and a password salt </li>
     *                    <li> the first name </li>
     *                    <li> the last name </li>
     *                    <li> the email address </li>
     *                    </ul>
     *                    If {@code isNotRegistered} is true, then the password and
     *                    password salt are not required.
     *                    (The id must not be specified, as the repository will
     *                    create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws KeyExistsException             If the email address of the added user
     *                                        already exists in the datasource.
     * @throws InvalidFieldsException         If one of the required fields of the
     *                                        scientific forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(User user, Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
    }

    /**
     * Changes the given user in the repository. All fields that are not
     * required will be deleted if left empty.
     *
     * @param user        A user dto with all required fields. Required are:
     *                    <ul>
     *                    <li> id </li>
     *                    <li> a hashed password and a password salt </li>
     *                    <li> the first name </li>
     *                    <li> the last name </li>
     *                    <li> the email address </li>
     *                    </ul>
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id or email.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws KeyExistsException             If the new email address of the user
     *                                        already exists in the datasource.
     * @throws InvalidFieldsException         If one of the required fields of the user
     *                                        is null.
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
     * @param user        The user forum to remove. Must be filled
     *                    with a valid id or email.
     * @param transaction The transaction to use.
     * @throws NotFoundException              The specified user forum was not found in
     *                                        the repository.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If both id and email are provided, but
     *                                        they belong to two different users.
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
     * @param user        A {@code User} dto that must be filled
     *                    with a valid id or email.
     * @param transaction The transaction to use.
     * @return A fully filled {@code Verification} dto.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id or email.
     * @throws InvalidFieldsException         If both id and email are provided, but
     *                                        they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Verification getVerification(User user, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Takes a verification dto that is filled with a valid userId and adds the
     * verification to the user.
     *
     * @param verification A fully filled Verification dto.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided userId.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setVerification(Verification verification,
                                       Transaction transaction)
            throws NotFoundException {

    }

    /**
     * Gets a list of all users.
     *
     * @param transaction          The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled user dtos.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     */
    public static List<User> getList(Transaction transaction,
                                     ResultListParameters resultListParameters)
            throws DataNotCompleteException {
        return null;
    }

    /**
     * Gets a list of all users that fulfil a specific role in regard to a
     * specific submission
     *
     * @param transaction The transaction to use.
     * @param privilege   The role the users must fulfil in regard to the
     *                    specified submission. Must be REVIEWER for reviewers
     *                    and AUTHOR for (co)-authors.
     *                    ADMIN and EDITOR are not supported.
     * @param submission  The submission the users should stand in a
     *                    relationship with. Must be filled with a valid id.
     * @return A list of fully filled user dtos.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidFieldsException         If the privilege is ADMIN or EDITOR.
     * @throws NotFoundException              If there is no submission with the specified
     *                                        id.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     */
    public static List<User> getList(Transaction transaction,
                                     Submission submission,
                                     Privilege privilege)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Gets a list of all users that are editor of a specific scientific forum.
     *
     * @param transaction     The transaction to use.
     * @param scientificForum The forum the users must be editor of. Must
     *                        contain a valid id.
     * @return A list of fully filled user dtos.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        specified id.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     */
    public static List<User> getList(Transaction transaction,
                                     ScientificForum scientificForum)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Adds the specified science field to the specified scientific user.
     *
     * @param user         A user dto with a valid id or email.
     * @param scienceField A science field dto with a valid id.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id or email or there is no science
     *                                        field with the provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If both id and email are provided, but
     *                                        they belong to two different users.
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
     * @param user         A user dto with a valid id or email.
     * @param scienceField A science field dto with a valid id that belongs to
     *                     the specified user.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id or email or there is no science
     *                                        field with the provided id or the science field
     *                                        does not belong to the specified user.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If both id and email are provided, but
     *                                        they belong to two different users.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeScienceField(User user, ScienceField scienceField,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Return if a user with the specified email address exists.
     *
     * @param user        A user dto with an email address.
     * @param transaction The transaction to use.
     * @return Is there a user with the specified email address in the
     * repository.
     * @throws InvalidFieldsException         If the email address of the supplied user
     *                                        is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static boolean emailExists(User user, Transaction transaction) {
        return false;
    }

    /**
     * Get the avatar image file for the avatar of the specified user.
     *
     * @param user        A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return A file containing the logo.
     * @throws NotFoundException              If there is no user with the specified id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getAvatar(User user, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Sets the avatar for the specified user.
     *
     * @param user        A suer dto with a valid id.
     * @param avatar      A file dto filled with an image file. If the dto or the
     *                    image itself are null, the current avatar will be deleted.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no user with the specified id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setAvatar(User user, FileDTO avatar,
                                 Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

}
