package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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
     *             If {@code isNotRegistered} is true, then the password and
     *             password salt are not required.
     *             (The id must not be specified, as the repository will
     *             create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If the email address of the added user
     *                            already exists in the datasource.
     * @throws InvalidFieldsException If one of the required fields of the
     *                                scientific forum is null.
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
     * @param user A user dto with all required fields. Required are:
     *             <ul>
     *             <li> id </li>
     *             <li> a hashed password and a password salt </li>
     *             <li> the first name </li>
     *             <li> the last name </li>
     *             <li> the email address </li>
     *             </ul>
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no user with the
     *                           provided id or email.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If the new email address of the user
     *                            already exists in the datasource.
     * @throws InvalidFieldsException If one of the required fields of the user
     *                                is null.
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
    public static Verification getVerification(User user, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Takes a verification dto that is filled with a valid userId and adds the
     * verification to the user.
     *
     * @param verification A fully filled Verification dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no user with the
     *                           provided userId.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setVerification(Verification verification,
                                       Transaction transaction)
            throws NotFoundException{

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
            throws DataNotCompleteException, InvalidFieldsException, NotFoundException {
        if (transaction == null || submission == null || privilege == null) {
            throw new InvalidFieldsException();
        }

        Integer submissionId = submission.getId();
        Connection conn = transaction.getConnection();

        List<User> userList = new LinkedList<>();

        try {
            PreparedStatement exists = conn.prepareStatement(
                    "SELECT * FROM submission WHERE id = ?"
            );
            exists.setInt(1, submissionId);
            ResultSet rsExists = exists.executeQuery();
            if (!rsExists.next()) {
                throw new NotFoundException("No Submission with ID: " + submissionId);
            }
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException();
        }

        switch (privilege) {
            case AUTHOR -> {
                try {
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT u.* FROM \"user\" u, submission s, co_authored c " +
                                    "WHERE ((u.id = s.author_id) OR (u.id = c.user_id AND  c.submission_id = s.id)) " +
                                    "AND s.id = ?"
                    );
                    ps.setInt(1, submissionId);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setPrivileges(getUserPrivileges(user, transaction));
                        user.setTitle(rs.getString("title"));
                        user.setFirstName(rs.getString("firstname"));
                        user.setLastName(rs.getString("lastname"));
                        user.setEmailAddress(rs.getString("email_address"));
                        java.sql.Date birthdate = rs.getDate("birthdate");
                        if (birthdate != null) {
                            user.setDateOfBirth(birthdate.toLocalDate());
                        }
                        user.setEmployer(rs.getString("employer"));

                        userList.add(user);
                    }
                    return userList;
                } catch (SQLException e) {
                    throw new DatasourceQueryFailedException();
                }
            }
            case REVIEWER -> {
                try {
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT u.* FROM \"user\" u, submission s, reviewed_by rb " +
                                    "WHERE u.id = rb.reviewer_id AND rb.submission_id = s.id " +
                                    "AND submission_id = ?"
                    );
                    ps.setInt(1, submissionId);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setPrivileges(getUserPrivileges(user, transaction));
                        user.setTitle(rs.getString("title"));
                        user.setFirstName(rs.getString("firstname"));
                        user.setLastName(rs.getString("lastname"));
                        user.setEmailAddress(rs.getString("email_address"));
                        java.sql.Date birthdate = rs.getDate("birthdate");
                        if (birthdate != null) {
                            user.setDateOfBirth(birthdate.toLocalDate());
                        }
                        user.setEmployer(rs.getString("employer"));

                        userList.add(user);
                    }
                    return userList;
                } catch (SQLException e) {
                    throw new DatasourceQueryFailedException();
                }
            }
            default -> {
                return userList;
            }
        }
    }

    /**
     * Get the Privileges of a certain user. ID must be set.
     * ONLY EDITOR, REVIEWER AND ADMIN ARE CHECKED.
     *
     * @param user        user with a set ID.
     * @param transaction some transaction
     * @return List of Privileges.
     */
    private static List<Privilege> getUserPrivileges(User user, Transaction transaction) throws DatasourceQueryFailedException {
        Integer id = user.getId();
        Connection conn = transaction.getConnection();

        List<Privilege> privileges = new LinkedList<>();

        try {
            PreparedStatement psEditor = conn.prepareStatement(
                    "SELECT u.id FROM \"user\" u WHERE EXISTS(SELECT * FROM member_of mo WHERE u.id = mo.editor_id AND u.id = ?)"
            );
            PreparedStatement psReviewer = conn.prepareStatement(
                    "SELECT u.id FROM \"user\" u WHERE EXISTS(SELECT * FROM reviewed_by rb WHERE u.id = rb.reviewer_id AND u.id = ?)"
            );
            PreparedStatement psAdmin = conn.prepareStatement(
                    "SELECT  u.id FROM \"user\" u WHERE u.is_administrator = true AND u.id = ?"
            );

            psEditor.setInt(1, id);
            ResultSet rs = psEditor.executeQuery();
            if (rs.next()) {
                privileges.add(Privilege.EDITOR);
            }

            psReviewer.setInt(1, id);
            rs = psReviewer.executeQuery();
            if (rs.next()) {
                privileges.add(Privilege.EDITOR);
            }

            psAdmin.setInt(1, id);
            rs = psAdmin.executeQuery();
            if (rs.next()) {
                privileges.add(Privilege.ADMIN);
            }
            return privileges;
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException();
        }
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
            throws DataNotCompleteException, NotFoundException, InvalidQueryParamsException {
        if (transaction == null || scientificForum == null) {
            throw new InvalidQueryParamsException("Parameter was null");
        }

        Integer id = scientificForum.getId();
        Connection conn = transaction.getConnection();
        List<User> userList = new LinkedList<>();

        try {
            PreparedStatement exists = conn.prepareStatement(
                    "SELECT * FROM scientific_forum WHERE id = ?"
            );
            exists.setInt(1, id);
            ResultSet rsExists = exists.executeQuery();
            if (!rsExists.next()) {
                throw new NotFoundException("No Scientific Forum with ID: " + id);
            }

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT u.* FROM \"user\" u, scientific_forum sf, member_of mo WHERE sf.id = ? " +
                            "AND u.id = mo.editor_id AND mo.scientific_forum_id = sf.id"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setPrivileges(getUserPrivileges(user, transaction));
                // We know the user must be an editor
                user.getPrivileges().add(Privilege.EDITOR);

                user.setTitle(rs.getString("title"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmailAddress(rs.getString("email_address"));
                java.sql.Date birthdate = rs.getDate("birthdate");
                if (birthdate != null) {
                    user.setDateOfBirth(birthdate.toLocalDate());
                }
                user.setEmployer(rs.getString("employer"));

                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            // todo: log once branch is updooted
            throw new DatasourceQueryFailedException();
        }
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
     * Get the avatar image file for the avatar of the specified user.
     *
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return A file containing the logo.
     * @throws NotFoundException If there is no user with the specified id.
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
     * @param user A suer dto with a valid id.
     * @param avatar A file dto filled with an image file. If the dto or the
     *               image itself are null, the current avatar will be deleted.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no user with the specified id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setAvatar(User user, FileDTO avatar,
                                 Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

}
