package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.util.DatasourceUtil;
import de.lases.persistence.util.TransientSQLExceptionChecker;
import jakarta.enterprise.inject.spi.CDI;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a user and the
 * possibility to get lists of users.
 *
 * @author Johann Schicho
 * @author Johannes Garstenauer
 * @author Thomas Kirz
 * @author Sebastian Vogt
 */
public class UserRepository {

    private static final List<String> userListColumnNames = List.of("user_role", "firstname", "lastname", "email_address", "employer");
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
            logger.severe("The id and email are missing. Therefor no user object can be queried.");
            throw new InvalidFieldsException();
        }


        String sql_number_of_submissions_and_editor_info = """
                 SELECT (SELECT count_submissions
                 FROM user_data
                 WHERE user_data.id = ?) as number_of_submissions,
                (SELECT COUNT(member_of.editor_id)
                 FROM "user", member_of
                 WHERE "user".id = ?
                   AND member_of.editor_id = "user".id) as editor_id
                                 """;

        String sql_user = """
                SELECT *
                FROM "user"
                WHERE "user".id = ? OR "user".email_address = ?
                """;

        String find_by_email = """
                SELECT id
                FROM "user"
                WHERE LOWER(email_address) = ?
                """;

        User result;
        try {
            Connection conn = transaction.getConnection();

            // first find user by email if no ID is given.
            if (user.getId() == null) {
                PreparedStatement ps = conn.prepareStatement(find_by_email);
                ps.setString(1, user.getEmailAddress().toLowerCase());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                } else {
                    logger.finest("No user found for address: " + user.getEmailAddress());
                    throw new NotFoundException("No user found for email-address: " + user.getEmailAddress());
                }
            }

            // Attempt to query for the user.
            ResultSet userResult;
            PreparedStatement userStatement = conn.prepareStatement(sql_user);
            if (user.getId() != null) {
                userStatement.setInt(1, user.getId());
            } else {
                userStatement.setNull(1, Types.INTEGER);
            }
            userStatement.setString(2, user.getEmailAddress());
            userResult = userStatement.executeQuery();

            // Attempt to query for the user's editor status and number of submissions.
            ResultSet submissionAndEditorResult;
            PreparedStatement extraStatement = conn.prepareStatement(sql_number_of_submissions_and_editor_info);
            if (user.getId() != null) {
                extraStatement.setInt(1, user.getId());
                extraStatement.setInt(2, user.getId());
            } else {
                extraStatement.setNull(1, Types.INTEGER);
                extraStatement.setNull(2, Types.INTEGER);
            }
            submissionAndEditorResult = extraStatement.executeQuery();

            // Attempt to create a user from the query results.
            if (userResult.next() && submissionAndEditorResult.next()) {
                result = createUserFromResultSet(userResult, submissionAndEditorResult);

                if (userResult.next()) {

                    // There cannot be two results of such a query.
                    logger.severe("There are two results of a unique user query. The parameters were"
                            + " id: " + user.getId() + ", email: " + user.getEmailAddress());
                    throw new InvalidFieldsException();
                }
            } else {
                String msg = "Error while loading a user with the id: " + user.getId()
                        + " and email: " + user.getEmailAddress();
                logger.fine(msg);
                throw new NotFoundException(msg);
            }
        } catch (SQLException ex) {
            transaction.abort();
            DatasourceUtil.logSQLException(ex, logger);
            throw new DatasourceQueryFailedException("A datasource exception"
                    + "occurred", ex);
        }

        setVerifiedStatus(result, transaction);

        return result;
    }

    /**
     * Create a {@link User} from the given results of the SQL queries.
     *
     * @param userResult                The result of the query for the user entity.
     * @param submissionAndEditorResult The result of the query for the number of submissions and
     *                                  whether the user is an editor.
     * @return The fully-filled {@code User}
     * @throws SQLException If the ResultSet columns are invalid or  if a database access error occurs or
     *                      if this method is called on a closed ResultSet.
     */
    private static User createUserFromResultSet(ResultSet userResult, ResultSet submissionAndEditorResult)
            throws SQLException {

        // Regular required user data.
        User result = new User();
        result.setId(userResult.getInt("id"));
        result.setEmailAddress(userResult.getString("email_address"));
        result.setAdmin(userResult.getBoolean("is_administrator"));
        result.setFirstName(userResult.getString("firstname"));
        result.setLastName(userResult.getString("lastname"));

        Date birthDate = userResult.getDate("birthdate");
        if (birthDate != null) {
            result.setDateOfBirth(birthDate.toLocalDate());
        }
        result.setPasswordSalt(userResult.getString("password_salt"));
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
            if (submissionAndEditorResult.getInt("editor_id") > 0) {
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
     * @return The user object with his id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the required fields of the
     *                                        scientific forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Thomas Kirz
     */
    public static User add(User user, Transaction transaction)
            throws DataNotWrittenException {
        Connection conn = transaction.getConnection();

        if (user.getEmailAddress() == null || user.getFirstName() == null || user.getLastName() == null) {
            transaction.abort();
            throw new InvalidFieldsException("The email address or the first or last name was null");
        }

        if (user.isRegistered() && (user.getPasswordHashed() == null || user.getPasswordSalt() == null)) {
            transaction.abort();
            throw new InvalidFieldsException("Hashed password or password salt was null for a registered user");
        }

        // specify every column except for the (auto-incrementing) id
        String sql = """
                INSERT INTO "user"(email_address, is_administrator, firstname, lastname, title, employer, birthdate,
                avatar_thumbnail, is_registered, password_hash, password_salt)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmailAddress());
            stmt.setBoolean(2, user.isAdmin());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getTitle());
            stmt.setString(6, user.getEmployer());

            if (user.getDateOfBirth() != null) {
                stmt.setDate(7, Date.valueOf(user.getDateOfBirth()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            stmt.setBytes(8, null);
            stmt.setBoolean(9, user.isRegistered());
            stmt.setString(10, user.getPasswordHashed());
            stmt.setString(11, user.getPasswordSalt());
            stmt.executeUpdate();

            // Get the autogenerated id of the user
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("User could not be added", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception occurred", ex);
            }
        }
        return user;
    }

    /**
     * Changes the given user in the repository. All fields that are not
     * required will be deleted if left empty. Exceptions to this are the avatar and the unhashed password, which
     * are ignored. Left empty means having the value null in this context. This means that primitive types will always
     * be written through and cannot be deleted.
     *
     * @param user        A user dto with all required fields. Required are:
     *                    <ul>
     *                    <li> id to identify the user (cannot be changed) </li>
     *                    <li> a hashed password and a password salt </li>
     *                    <li> the first name </li>
     *                    <li> the last name </li>
     *                    <li> the email address </li>
     *                    <li>
     *                        all primitive types of the user except the number of submissions, which will be
     *                        ignored
     *                    </li>
     *                    </ul>
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws KeyExistsException             If the new email address of the user
     *                                        already exists in the datasource.
     * @throws InvalidFieldsException         If one of the required fields of the user
     *                                        is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Sebastian Vogt
     */
    public static void change(User user, Transaction transaction)
            throws NotFoundException, DataNotWrittenException,
            KeyExistsException {
        if (user.getId() == null || user.getPasswordHashed() == null || user.getPasswordSalt() == null
                || user.getFirstName() == null || user.getLastName() == null || user.getEmailAddress() == null) {
            transaction.abort();
            throw new InvalidFieldsException("One of the required fields of the user was null!");
        }

        Connection conn = transaction.getConnection();

        String sql = """
                UPDATE "user"
                SET email_address = ?, is_administrator = ?, firstname = ?, lastname = ?, title = ?, employer = ?,
                birthdate = ?, is_registered = ?, password_hash = ?, password_salt = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmailAddress());
            stmt.setBoolean(2, user.isAdmin());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getTitle());
            stmt.setString(6, user.getEmployer());
            stmt.setDate(7, user.getDateOfBirth() == null ? null : Date.valueOf(user.getDateOfBirth()));
            stmt.setBoolean(8, user.isRegistered());
            stmt.setString(9, user.getPasswordHashed());
            stmt.setString(10, user.getPasswordSalt());
            stmt.setInt(11, user.getId());
            int rowCount = stmt.executeUpdate();

            if (rowCount == 0) {
                throw new NotFoundException("The user to change was not found");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            // duplicate key value
            if (e.getSQLState().equals("23505")) {
                throw new KeyExistsException("The email address already exists");
            } else if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException("The user could not be changed", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("The user could not be changed", e);
            }
        }
    }

    /**
     * Takes a user dto that is filled with a valid id and
     * removes this user from the repository.
     *
     * @param user        The user to remove. Must be filled
     *                    with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException              The specified user forum was not found in
     *                                        the repository.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If the user id is null
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Sebastian Vogt
     */
    public static void remove(User user, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (user.getId() == null) {
            throw new InvalidFieldsException("The user id is null");
        }

        Connection conn = transaction.getConnection();

        String sql = """
                DELETE FROM "user"
                WHERE id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new NotFoundException("The user to delete was not found.");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException("User was not deleted", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("User was not deleted", e);
            }
        }

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
        if (user.getId() == null && user.getEmailAddress() == null) {
            logger.severe("User dto is not filled with an id or email address.");
            throw new IllegalArgumentException("User dto is not filled with an id or email address.");
        }

        Connection conn = transaction.getConnection();
        String sql = """
                SELECT v.* FROM verification v, "user" u
                WHERE v.id = ?
                OR (v.id = u.id AND u.email_address = ?);
                """;

        Verification result;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getEmailAddress());
            ResultSet rs = stmt.executeQuery();

            // Attempt to create a verification dto from the result set.
            if (rs.next()) {
                result = createVerificationFromResultSet(rs);
                logger.finer("Found verification dto for user");
            } else {
                logger.warning("No verification dto for specified user found");
                throw new NotFoundException("No verification dto for specified user found");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("Failed to retrieve verification from database.", e);
        }

        return result;
    }

    /**
     * Takes a verification dto that is filled with a validation random returns the
     * filled verification dto.
     *
     * @param verification A {@code Verification} dto that must be filled
     *                     with a validation random.
     * @param transaction  The transaction to use.
     * @return A fully filled {@code Verification} dto if a verification with the
     * provided validation random exists, otherwise null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Thomas Kirz
     */
    public static Verification getVerification(Verification verification, Transaction transaction)
            throws NotFoundException {
        if (verification.getValidationRandom() == null) {
            logger.severe("Verification dto is not filled with a validation random.");
            throw new IllegalArgumentException("Verification dto is not filled with a validation random.");
        }

        Connection conn = transaction.getConnection();
        String sql = "SELECT * FROM verification WHERE validation_random = ?";

        Verification result;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, verification.getValidationRandom());
            ResultSet rs = stmt.executeQuery();

            // Attempt to create a verification dto from the result set.
            if (rs.next()) {
                result = createVerificationFromResultSet(rs);
                logger.finer("Found verification dto with validation random "
                        + verification.getValidationRandom());
            } else {
                logger.warning("No verification dto with validation random " + verification.getValidationRandom()
                        + " found.");
                throw new NotFoundException("No verification dto with the specified validation random found.");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("Failed to retrieve Verification from database.", e);
        }

        return result;
    }

    private static Verification createVerificationFromResultSet(ResultSet rs) throws SQLException {
        Verification verification = new Verification();

        verification.setUserId(rs.getInt("id"));
        verification.setValidationRandom(rs.getString("validation_random"));
        verification.setVerified(rs.getBoolean("is_verified"));
        Timestamp timestamp = rs.getTimestamp("timestamp_validation_started");
        verification.setTimestampValidationStarted(timestamp == null ? null : timestamp.toLocalDateTime());
        verification.setNonVerifiedEmailAddress(rs.getString("unvalidated_email_address"));

        return verification;
    }

    /**
     * Takes a filled verification dto and adds it to the database.
     *
     * @param verification A fully filled Verification dto.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided userId.
     * @throws DataNotWrittenException        If the verification could not be added but has
     *                                        high probability of succeeding after retrying.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Thomas Kirz
     */
    public static void addVerification(Verification verification,
                                       Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        User user = new User();
        user.setId(verification.getUserId());
        try {
            get(user, transaction);
        } catch (NotFoundException e) {
            logger.severe("User belonging to verification dto was not found.");
            throw new NotFoundException("User belonging to verification dto was not found.");
        }

        // insert new verification
        Connection conn = transaction.getConnection();
        String sql = """
                INSERT INTO verification (id, validation_random, is_verified, timestamp_validation_started,
                unvalidated_email_address)
                VALUES (?, ?, ?, ?, ?);
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, verification.getUserId());
            stmt.setString(2, verification.getValidationRandom());
            stmt.setBoolean(3, verification.isVerified());
            Timestamp timestamp = verification.getTimestampValidationStarted() == null ?
                    null : Timestamp.valueOf(verification.getTimestampValidationStarted());
            stmt.setTimestamp(4, timestamp);
            stmt.setString(5, verification.getNonVerifiedEmailAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                logger.severe("Failed to add verification to database.");
                throw new DataNotWrittenException("Failed to add verification to database.", e);
            } else {
                logger.severe("Failed to add verification to database.");
                transaction.abort();
                throw new DatasourceQueryFailedException("Failed to add verification to database.", e);
            }
        }
    }

    /**
     * Takes a filled verification dto and replaces the user's verification with this one.
     *
     * @param verification A fully filled Verification dto.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If the verification could not be found in the database.
     * @throws DataNotWrittenException        If the verification could not be added but has
     *                                        high probability of succeeding after retrying.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Thomas Kirz
     */
    public static void changeVerification(Verification verification,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        User user = new User();
        user.setId(verification.getUserId());
        try {
            getVerification(user, transaction);
        } catch (NotFoundException e) {
            logger.severe("Verification does not exist.");
            throw new NotFoundException("Verification does not exist.");
        }

        // update existing verification
        Connection conn = transaction.getConnection();
        String sql = """
                UPDATE verification
                SET validation_random = ?, is_verified = ?, timestamp_validation_started = ?,
                unvalidated_email_address = ?
                WHERE id = ?;
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, verification.getValidationRandom());
            stmt.setBoolean(2, verification.isVerified());
            stmt.setTimestamp(3, Timestamp.valueOf(verification.getTimestampValidationStarted()));
            stmt.setString(4, verification.getNonVerifiedEmailAddress());
            stmt.setInt(5, verification.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException("Failed to add verification to database.", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Failed to add verification to database.", e);
            }
        }
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
            throws DataNotCompleteException, InvalidQueryParamsException {
        if (transaction == null || resultListParameters == null) {
            logger.severe("Invalid Parameters for getList(). Parameter is null.");
            throw new InvalidQueryParamsException();
        }

        Connection conn = transaction.getConnection();
        List<User> userList = new LinkedList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(generateResultListParametersUserListSQL(resultListParameters,
                    false));

            // Set values here for protection against sql injections.
            // See comments in generateResultListParametersUserListSQL()
            final int[] i = {1};

            // Filtering
            for (String userListColumnName : userListColumnNames) {
                String value = resultListParameters.getFilterColumns().get(userListColumnName);
                if (value != null && !value.equals("") && !value.equals("none")) {
                    ps.setString(i[0], Objects.requireNonNullElse("%" + value + "%", "%"));
                    i[0]++;
                }
            }

            // Global Search Word
            for (String ignored : userListColumnNames) {
                ps.setString(i[0], "%"
                        + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");
                i[0]++;
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                Privilege.getPrivilege(rs.getString("user_role"))
                        .ifPresentOrElse(value -> user.setPrivileges(List.of(value)),
                                () -> user.setPrivileges(new LinkedList<>()));
                user.setTitle(rs.getString("title"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmailAddress(rs.getString("email_address"));
                java.sql.Date birthdate = rs.getDate("birthdate");
                if (birthdate != null) {
                    user.setDateOfBirth(birthdate.toLocalDate());
                }
                user.setEmployer(rs.getString("employer"));
                user.setRegistered(rs.getBoolean("is_registered"));
                user.setNumberOfSubmissions(rs.getInt("count_submissions"));

                userList.add(user);
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("List could not be loaded.");
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Could not query DB", e);
            }
        }
        return userList;
    }

    private static String generateResultListParametersUserListSQL(ResultListParameters params, boolean doCount) {
        StringBuilder sb = new StringBuilder();

        if (doCount) {
            sb.append("SELECT COUNT (DISTINCT lastname) FROM user_data WHERE TRUE");
        } else {
            sb.append("SELECT * FROM user_data WHERE TRUE");
        }

        // Filter according to filter columns parameter.
        userListColumnNames.stream()
                .filter(columnName -> params.getFilterColumns().get(columnName) != null
                        && !params.getFilterColumns().get(columnName).equals("")
                        && !params.getFilterColumns().get(columnName).equals("none"))
                .forEach(column -> sb.append(" AND ").append(column).append(" ILIKE ?\n"));

        // Filter according to global search word.
        if (!"".equals(params.getGlobalSearchWord())) {
            sb.append(" AND (");
            sb.append(" user_role ILIKE ?\n");
            sb.append(" OR firstname ILIKE ?\n"); // sb.append(" firstname LIKE '%").append(params.getGlobalSearchWord()).append("%'\n");
            sb.append(" OR lastname ILIKE ?\n"); // ...
            sb.append(" OR email_address ILIKE ?\n");// sb.append(" OR (lastname LIKE '%").append(params.getGlobalSearchWord()).append("%'\n");
            sb.append(" OR employer ILIKE ?\n"); // sb.append(" OR (employer LIKE '%").append(params.getGlobalSearchWord()).append("%'\n");

            sb.append(")");
        }

        // Sort according to sort column parameter
        if (!"".equals(params.getSortColumn()) && userListColumnNames.contains(params.getSortColumn()) && !doCount) {
            sb.append("ORDER BY ")
                    .append(params.getSortColumn())
                    .append(" ")
                    .append(params.getSortOrder() == SortOrder.ASCENDING ? "ASC" : "DESC")
                    .append("\n");
        }

        // Set limit and offset
        if (!doCount) {
            ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
            int paginationLength = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LIST_LENGTH"));
            sb.append("LIMIT ").append(paginationLength)
                    .append(" OFFSET ").append(paginationLength * (params.getPageNo() - 1));
        }
        // Add semicolon to end of query
        sb.append(";");

        return sb.toString();
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

        switch (privilege) {
        case AUTHOR -> {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT DISTINCT u.* FROM \"user\" u, submission s, co_authored c " +
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
                    setVerifiedStatus(user, transaction);

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
                    setVerifiedStatus(user, transaction);

                    userList.add(user);
                }
                return userList;
            } catch (SQLException e) {
                DatasourceUtil.logSQLException(e, logger);
                if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                    throw new DataNotCompleteException("user list could not be loaded.");
                } else {
                    transaction.abort();
                    throw new DatasourceQueryFailedException("Could not query DB", e);
                }
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
            DatasourceUtil.logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("Could not query DB", e);
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
                setVerifiedStatus(user, transaction);

                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("List could not be loaded.");
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Could not query DB", e);
            }
        }
    }

    /**
     * Adds the specified science field to the specified scientific user.
     *
     * @param user         A user dto with a valid id.
     * @param scienceField A science field dto with a valid id.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If there is no user with the
     *                                        provided id or there is no science
     *                                        field with the provided name.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If the use id is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Sebastian Vogt
     */
    public static void addScienceField(User user, ScienceField scienceField,
                                       Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        Connection conn = transaction.getConnection();

        if (user.getId() == null || scienceField.getName() == null) {
            throw new InvalidFieldsException("The user id or science field name was null");
        }

        String sql = """
                INSERT INTO interests
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, scienceField.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            // 23503: Foreign key constraint violated
            if (e.getSQLState().equals("23503")) {
                throw new NotFoundException("Either the specified user or science field do not exist");
            }
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException("Science field could not be added", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Science field could not be added", e);
            }
        }

    }

    /**
     * Removes the specified science field from the specified user.
     *
     * @param user         A user dto with a valid id.
     * @param scienceField A science field dto with a valid name that belongs to
     *                     the specified user.
     * @param transaction  The transaction to use.
     * @throws NotFoundException              If this science field never belonged to this user.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If the user id or science field name are null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Sebastian Vogt
     */
    public static void removeScienceField(User user, ScienceField scienceField,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (user.getId() == null || scienceField.getName() == null) {
            throw new InvalidFieldsException("The user id or science field name was null");
        }

        Connection conn = transaction.getConnection();

        String sql = """
                DELETE FROM interests
                WHERE user_id = ? AND science_field_name = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, scienceField.getName());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Either the user or science field could not be found");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException("The science field could not be removed from the user");
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("The science field could not be removed from the user");
            }
        }
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
        if (user.getEmailAddress() == null) {
            transaction.abort();
            throw new InvalidFieldsException("the email address was null");
        }
        Connection conn = transaction.getConnection();

        String query = """
                SELECT * FROM "user"
                WHERE email_address = ?;
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getEmailAddress());
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("the datasource could not be queried", e);
        }
    }

    /**
     * Get the avatar image file for the avatar of the specified user. Is null if the user has no avatar.
     *
     * @param user        A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return A file containing the logo.
     * @throws NotFoundException              If there is no user with the specified id.
     * @throws InvalidFieldsException         If the user id is null.
     * @throws DataNotCompleteException       If the data cannot be fetched due to a transient fault.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Sebastian Vogt
     */
    public static FileDTO getAvatar(User user, Transaction transaction)
            throws NotFoundException, DataNotCompleteException {

        if (user.getId() == null) {
            throw new InvalidFieldsException("The user id must not be null!");
        }

        Connection connection = transaction.getConnection();

        String query = """
                SELECT avatar_thumbnail
                FROM "user"
                WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                byte[] avatar = resultSet.getBytes(1);
                resultSet.close();
                FileDTO avatarFile = new FileDTO();
                avatarFile.setFile(avatar);
                return avatarFile;
            } else {
                throw new NotFoundException("There is no user with the given id!");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("Avatar could not be fetched", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Avatar could not be fetched", e);
            }
        }


    }

    /**
     * Sets the avatar for the specified user.
     *
     * @param user        A suer dto with a valid id.
     * @param avatar      A file dto filled with an image file. If the dto or the
     *                    image itself are null, the current avatar will be deleted.
     * @param transaction The transaction to use.
     * @throws InvalidFieldsException         If the specified user.
     * @throws NotFoundException              If there is no user with the specified id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Sebastian Vogt
     */
    public static void setAvatar(User user, FileDTO avatar,
                                 Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
        if (user.getId() == null) {
            throw new InvalidFieldsException("The user id must not be null!");
        }

        Connection connection = transaction.getConnection();

        String query = """
                UPDATE "user"
                SET avatar_thumbnail = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBytes(1, avatar == null ? null : avatar.getFile());
            stmt.setInt(2, user.getId());
            int rowCount = stmt.executeUpdate();
            if (rowCount == 0) {
                throw new NotFoundException("The user could not be found!");
            }
        } catch (SQLException e) {
            // TODO: was, wenn es den user nicht gibt?
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException("The avatar could not be written!", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("The avatar could not be written!", e);
            }
        }
    }

    private static void setVerifiedStatus(User user, Transaction transaction) {
        Verification verification;
        try {
            verification = getVerification(user, transaction);
        } catch (NotFoundException e) {
            verification = null;
            logger.fine("No verification found for user: " + user.getEmailAddress());
        }
        user.setVerified(verification != null && verification.isVerified());
    }

    public static int getCountItemsList(Transaction transaction, ResultListParameters resultListParameters)
            throws DataNotCompleteException, NotFoundException {

        if (transaction == null) {
            logger.severe("Passed transaction is invalid.");
            throw new IllegalArgumentException("Transaction can not be null.");
        }
        if (resultListParameters == null) {
            logger.severe("Passed result-list parameters are null.");
            throw new IllegalArgumentException("ResultListParameters must not be null.");
        }

        Connection connection = transaction.getConnection();

        try (PreparedStatement ps = connection.prepareStatement(generateResultListParametersUserListSQL(resultListParameters, true))) {

            // Set values here for protection against sql injections.
            int i = 1;

            // Filtering
            for (String userColumnName : userListColumnNames) {
                String value = resultListParameters.getFilterColumns().get(userColumnName);
                if (value != null && !value.equals("") && !value.equals("none")) {
                    ps.setString(i, Objects.requireNonNullElse("%" + value + "%", "%"));
                    i++;
                }
            }

            // Global Search Word
            for (String ignored : userListColumnNames) {
                ps.setString(i, "%"
                        + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");
                i++;
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            } else {
                logger.severe("Could not compute the amount of users.");
                throw new NotFoundException();
            }

        } catch (SQLException e) {
            transaction.abort();
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException(e.getMessage(), e);
        }
    }
}
