package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.util.DatasourceUtil;
import de.lases.persistence.util.TransientSQLExceptionChecker;
import jakarta.enterprise.inject.spi.CDI;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a submission and the
 * possibility to get lists of submissions.
 *
 * @author Thomas Kirz
 * @author Sebastian Vogt
 * @author Stefanie Gürster
 */
public class SubmissionRepository {


    private static final Logger logger = Logger.getLogger(SubmissionRepository.class.getName());

    private static final List<String> filterColumnNames = List.of("title", "state", "timestamp_submission",
            "timestamp_deadline_revision", "forum");


    /**
     * Takes a scientific forum dto that is filled with a valid id and returns
     * a fully filled submission dto.
     *
     * @param submission  A {@code Submission} dto that must be filled
     *                    with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled {@code Submission} dto.
     * @throws NotFoundException              If there is no submission with the
     *                                        provided id.
     * @throws DataNotCompleteException       If retrieving the data failed but has a high probability of succeeding
     *                                        after a retry.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Thomas Kirz
     */
    public static Submission get(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotCompleteException {
        if (submission.getId() == null) {
            logger.severe("The passed Submission-DTO does not contain an id.");
            throw new IllegalArgumentException("The passed Submission-DTO does not contain an id.");
        }

        Connection conn = transaction.getConnection();
        String sql = "SELECT * FROM submission WHERE id = ?";

        Submission result;
        ResultSet resultSet;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, submission.getId());
            resultSet = stmt.executeQuery();

            // Attempt to create a submission from the result set.
            if (resultSet.next()) {
                result = createSubmissionFromResultSet(resultSet);
                logger.finer("Successfully retrieved submission with id " + submission.getId() + "from database.");
            } else {
                logger.warning("No submission with id " + submission.getId() + " found in database.");
                throw new NotFoundException("No submission with id " + submission.getId() + " found.");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("Submission could not be retrieved", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Submission could not be retrieved", e);
            }
        }

        return result;
    }

    private static Submission createSubmissionFromResultSet(ResultSet resultSet) throws SQLException {
        Submission sub = new Submission();

        // retrieve required fields
        sub.setId(resultSet.getInt("id"));
        sub.setTitle(resultSet.getString("title"));
        sub.setState(SubmissionState.valueOf(resultSet.getString("state")));
        Timestamp ts = resultSet.getTimestamp("timestamp_submission");
        sub.setSubmissionTime(ts == null ? null : ts.toLocalDateTime());
        sub.setRevisionRequired(resultSet.getBoolean("requires_revision"));
        Timestamp td = resultSet.getTimestamp("timestamp_deadline_revision");
        sub.setDeadlineRevision(td == null ? null : td.toLocalDateTime());
        sub.setEditorId(resultSet.getInt("editor_id"));

        // retrieve optional fields
        sub.setAuthorId(resultSet.getInt("author_id"));
        sub.setScientificForumId(resultSet.getInt("forum_id"));

        return sub;
    }

    /**
     * Adds a submission to the repository.
     *
     * @param submission  A submission dto filled with the required fields.
     *                    Required is:
     *                    <ul>
     *                    <li> scientificForumId </li>
     *                    <li> authorId </li>
     *                    <li> editorId </li>
     *                    <li> title </li>
     *                    <li> state </li>
     *                    <li> submissionTime </li>
     *                    </ul>
     *                    (The id must not be specified, as the repository will
     *                    create the id)
     * @param transaction The transaction to use.
     * @return The submission that was added, but filled with its id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the required fields of the
     *                                        submission is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     *
     * @author Sebastian Vogt
     */
    public static Submission add(Submission submission, Transaction transaction)
            throws DataNotWrittenException {
        if (submission.getTitle() == null || submission.getState() == null || submission.getSubmissionTime() == null) {
            throw new InvalidFieldsException("At least one of the required fields of the submission was null");
        }

        Connection conn = transaction.getConnection();

        String sql = """
                INSERT INTO submission(title, state, timestamp_submission, requires_revision,
                    timestamp_deadline_revision, author_id, editor_id, forum_id)
                VALUES (?, CAST(? as submission_state), ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, submission.getTitle());
            stmt.setString(2, submission.getState().toString());
            stmt.setTimestamp(3, Timestamp.valueOf(submission.getSubmissionTime()));
            stmt.setBoolean(4, submission.isRevisionRequired());

            Timestamp deadline = submission.getDeadlineRevision() == null ? null
                    : Timestamp.valueOf(submission.getDeadlineRevision());
            stmt.setTimestamp(5, deadline);

            stmt.setInt(6, submission.getAuthorId());
            stmt.setInt(7, submission.getEditorId());
            stmt.setInt(8, submission.getScientificForumId());

            stmt.executeUpdate();

            // Get the autogenerated id of the submission
            ResultSet resultSet = stmt.getGeneratedKeys();
            resultSet.next();
            submission.setId(resultSet.getInt(1));
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("Submission could not be added", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception "
                        + "occurred", ex);
            }
        }
        return submission;
    }

    /**
     * Changes the given submission in the repository.
     *
     * @param submission  A submission dto filled with the required fields.
     *                    Required is:
     *                    <ul>
     *                    <li> id </li>
     *                    <li> scientificForumId </li>
     *                    <li> authorId </li>
     *                    <li> editorId </li>
     *                    <li> title </li>
     *                    <li> state </li>
     *                    <li> submissionTime </li>
     *                    </ul>
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no submission with the
     *                                        provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the required fields of the
     *                                        submission is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (submission.getId() == null) {
            logger.severe("Invalid submission id when tried to change a submission.");
            throw new InvalidFieldsException("The submission id must not be null here.");
        }

        Connection connection = transaction.getConnection();

        // Only the following data can be changed.
        String sql = """
                UPDATE submission
                SET state = CAST( ? AS submission_state), requires_revision = ?, timestamp_deadline_revision = ?, editor_id = ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, submission.getState().toString());
            statement.setBoolean(2, submission.isRevisionRequired());
            if (submission.getDeadlineRevision() == null) {
                statement.setTimestamp(3, null);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(submission.getDeadlineRevision()));
            }
            statement.setInt(4, submission.getEditorId());
            statement.setInt(5, submission.getId());

            if (statement.executeUpdate() == 0) {
                logger.severe("Searching for a submission failed. Tried to change the submission.");
                throw new NotFoundException("Submission id doesn't exist.");
            }

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);

            if (TransientSQLExceptionChecker.isTransient(exception.getSQLState())) {
                throw new DataNotWrittenException("Changing submission wasn't successful.", exception);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception occurred while changing a submission's data.", exception);
            }
        }

    }

    /**
     * Takes a submission dto that is filled with a valid id and removes
     * this submission from the repository.
     *
     * @param submission  The submission to remove. Must be filled
     *                    with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException              The specified submission was not found in
     *                                        the repository.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {

        if (submission.getId() == null) {
            logger.severe("Invalid submission id while try to remove a submission.");
            throw new IllegalArgumentException("the submission id must not be null while removing it.");
        }

        Connection connection = transaction.getConnection();

        String sql = """
                DELETE FROM submission
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, submission.getId());

            if (statement.executeUpdate() == 0) {
                logger.severe("Removing submission with the submission id: " + submission.getId());
                throw new NotFoundException();
            }

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);

            if (TransientSQLExceptionChecker.isTransient(exception.getSQLState())) {
                throw new DataNotWrittenException("Removing submission wasn't successful.", exception);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception occurred while removing a submission.", exception);
            }
        }
    }

    /**
     * Gets a list all submissions that belong to the specified user under the
     * specified role (for example as editor, submitter, reviewer) from the
     * specified scientific forum.
     *
     * @param user                 A {@code User} dto with a valid id.
     * @param privilege            As which role should the user act.
     * @param scientificForum      A {@code ScientificForum} dto with a valid id.
     * @param transaction          The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     * belong to the specified user in the specified role and the
     * specified scientific forum.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws NotFoundException              If there is no user with the provided id or no
     *                                        scientific forum with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     * @author Thomas Kirz
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {

        if (user.getId() == null || scientificForum.getId() == null) {
            logger.severe("A passed DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User and ScientificForum id must not be null.");
        }

        Connection conn = transaction.getConnection();

        ResultSet resultSet;
        List<Submission> result = new ArrayList<>();

        if (privilege == Privilege.ADMIN) {
            return getList(scientificForum, transaction, resultListParameters);
        } else {
            String sql = switch (privilege) {
                case EDITOR -> """
                        SELECT * FROM submission
                        WHERE forum_id = ? AND editor_id = ?
                        """;
                case REVIEWER -> """
                        SELECT * FROM submission
                        WHERE forum_id = ?
                        AND EXISTS (SELECT 1 FROM reviewed_by rb
                                    WHERE rb.submission_id = submission.id
                                    AND rb.reviewer_id = ?)
                        """;
                default -> """
                        SELECT * FROM submission
                        WHERE submission.forum_id = ?
                          AND ? IN (
                              SELECT submission.author_id
                              UNION
                              SELECT ca.user_id FROM co_authored ca WHERE ca.submission_id = submission.id)
                        """;
            };

            sql += generateResultListParametersSQLSuffix(resultListParameters, true);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, scientificForum.getId());
                stmt.setInt(2, user.getId());
                fillResultListParameterSuffix(3, stmt, resultListParameters);
                resultSet = stmt.executeQuery();

                // Attempt to create a list of submissions from the result set.
                while (resultSet.next()) {
                    result.add(createSubmissionFromResultSet(resultSet));
                }
                logger.finer("Retrieved a list of submissions from the database.");
            } catch (SQLException e) {
                DatasourceUtil.logSQLException(e, logger);
                if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                    throw new DataNotCompleteException("The list of submissions could not be retrieved.", e);
                } else {
                    transaction.abort();
                    throw new DatasourceQueryFailedException("The list of submissions could not be retrieved.", e);
                }
            }
            return result;
        }
    }

    /**
     * Gets a list all submissions that belong to the specified user under the
     * specified role (for example as editor, submitter, reviewer).
     *
     * @param user                 A {@code User} dto with a valid id.
     * @param privilege            As which role should the user act.
     * @param transaction          The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     * belong to the specified user in the specified role.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws NotFoundException              If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     * @author Thomas Kirz
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (user.getId() == null) {
            logger.severe("Passed  User-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User id must not be null.");
        }

        Connection conn = transaction.getConnection();

        ResultSet resultSet;
        List<Submission> result = new ArrayList<>();

        String sql = switch (privilege) {
            case ADMIN -> """
                    SELECT * FROM submission
                    """;
            case EDITOR -> """
                    SELECT * FROM submission
                    WHERE editor_id = ?
                    """;
            case REVIEWER -> """
                    SELECT * FROM submission
                    WHERE EXISTS (SELECT 1 FROM reviewed_by rb
                                WHERE rb.submission_id = submission.id
                                AND rb.reviewer_id = ?)
                    """;
            default -> """
                    SELECT * FROM submission
                    WHERE ? IN (
                        SELECT submission.author_id
                        UNION
                        SELECT ca.user_id FROM co_authored ca WHERE ca.submission_id = submission.id)
                    """;
        };

        sql += generateResultListParametersSQLSuffix(resultListParameters, true);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int parameterIndex = 1;

            if (privilege != Privilege.ADMIN) {
                stmt.setInt(parameterIndex++, user.getId());
            }
            fillResultListParameterSuffix(parameterIndex, stmt, resultListParameters);
            resultSet = stmt.executeQuery();

            // Attempt to create a list of submissions from the result set.
            while (resultSet.next()) {
                result.add(createSubmissionFromResultSet(resultSet));
            }
            logger.finer("Retrieved a list of submissions from the database.");
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("The list of submissions could not be retrieved.", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("The list of submissions could not be retrieved.", e);
            }
        }
        return result;
    }

    /**
     * Gets a list of all submissions that belong to the specified scientific
     * forum.
     *
     * @param scientificForum      A {@code ScientificForum} dto with a valid id.
     * @param transaction          The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     * belong to the specified scientific forum.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     * @author Thomas Kirz
     */
    public static List<Submission> getList(ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (scientificForum.getId() == null) {
            logger.severe("Passed User-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User ScientificForum id must not be null.");
        }

        Connection conn = transaction.getConnection();

        ResultSet resultSet;
        List<Submission> result = new ArrayList<>();

        String sql = """
                SELECT * FROM submission
                WHERE forum_id = ?
                """;
        sql += generateResultListParametersSQLSuffix(resultListParameters, true);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scientificForum.getId());
            fillResultListParameterSuffix(2, stmt, resultListParameters);
            resultSet = stmt.executeQuery();

            // Attempt to create a list of submissions from the result set.
            while (resultSet.next()) {
                result.add(createSubmissionFromResultSet(resultSet));
            }
            logger.finer("Retrieved a list of submissions from the database.");
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("The list of submissions could not be retrieved.", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("The list of submissions could not be retrieved.", e);
            }
        }
        return result;
    }

    /**
     * Adds the specified user to the specified submission as a co-author.
     *
     * @param submission  A scientific forum dto with a valid id.
     * @param user        A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no submission with the
     *                                        provided id or there is no user with the
     *                                        provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     *
     * @author Sebastian Vogt
     */
    public static void addCoAuthor(Submission submission, User user,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException {

        if (user.getId() == null || submission.getId() == null) {
            transaction.abort();
            String nullArgument = user.getId() == null ? "user" : "submission";
            throw new InvalidFieldsException("The ids of the " + nullArgument + " must not be null");
        }
        Connection conn = transaction.getConnection();
        String sql = """
                INSERT INTO co_authored
                VALUES (?, ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setInt(2, submission.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);

            // 23503: Foreign key constraint violated
            if (ex.getSQLState().equals("23503")) {
                throw new NotFoundException("Either the specified user or submission does not exist");
            } else if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The co-author was not added", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }

    /**
     * Adds the specified user to the specified submission as a reviewer.
     *
     * @param reviewedBy  A relation between submission and a user in a role of a reviewer.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no submission forum with the
     *                                        provided id or there is no user with the
     *                                        provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws KeyAlreadyExistsException      If a user already reviews this submission.
     */
    public static void addReviewer(ReviewedBy reviewedBy,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (reviewedBy.getSubmissionId() == null) {
            transaction.abort();
            logger.severe("Passed submission DTO is not sufficiently filled.");
            throw new InvalidFieldsException("Submission with id: " + reviewedBy.getSubmissionId() + " must not be null.");
        }
        if (reviewedBy.getReviewerId() == null) {
            transaction.abort();
            logger.severe("Passed user DTO is not sufficiently filled.");
            throw new InvalidFieldsException("User with id: " + reviewedBy.getReviewerId() + " must not be null.");
        }

        Connection connection = transaction.getConnection();


        String sql = "INSERT INTO reviewed_by VALUES (?, ?, CAST (? as review_task_state), ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reviewedBy.getReviewerId());
            statement.setInt(2, reviewedBy.getSubmissionId());
            statement.setString(3, reviewedBy.getHasAccepted().name());
            statement.setTimestamp(4, Timestamp.valueOf(reviewedBy.getTimestampDeadline()));

            if (statement.executeUpdate() == 0) {
                logger.warning("Searching for an user with the id: " + reviewedBy.getReviewerId());
                throw new NotFoundException();
            }

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            if (exception.getSQLState().equals("23505")) {
                transaction.abort();
                throw new KeyAlreadyExistsException("Reviewer does already review this submission.");
            } else if (TransientSQLExceptionChecker.isTransient(exception.getSQLState())) {
                throw new DataNotWrittenException("Data not written while adding a reviewer.", exception);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception occurred while adding a new reviewer.");
            }

        }

    }

    /**
     * Removes the specified co-author from the specified scientific forum.
     *
     * @param submission  A scientific forum dto with a valid id.
     * @param user        A user dto with a valid id, which is a co-author in the
     *                    aforementioned submission.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no user with the
     *                                        provided id or the provided user is not
     *                                        a co-author for the provided submission.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeCoAuthor(Submission submission, User user,
                                      Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Count the number of submissions that belong to the user under the specified role.
     *
     * @param user                 A user dto with a valid id.
     * @param privilege            As which role should the user act.
     * @param transaction          The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return The number of submission the specified user is author, editor
     * or reviewer of.
     * @throws NotFoundException              If there is no user with the provided id.
     * @throws DataNotCompleteException       If retrieving the data failed but has a high probability of succeeding
     *                                        after a retry.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @author Thomas Kirz
     */
    public static int countSubmissions(User user, Privilege privilege,
                                       Transaction transaction,
                                       ResultListParameters
                                               resultListParameters)
            throws NotFoundException, DataNotCompleteException {
        if (user.getId() == null) {

            logger.severe("Passed  User-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User id must not be null.");

        }

        // Throw NotFoundException if user does not exist
        UserRepository.get(user, transaction);

        Connection conn = transaction.getConnection();

        ResultSet rs;

        String sql = switch (privilege) {
            case ADMIN -> """
                    SELECT COUNT(*) FROM submission
                    """;
            case EDITOR -> """
                    SELECT COUNT(*) FROM submission
                    WHERE editor_id = ?
                    """;
            case REVIEWER -> """
                    SELECT COUNT(*) FROM submission
                    WHERE EXISTS (SELECT 1 FROM reviewed_by rb
                                WHERE rb.submission_id = submission.id
                                AND rb.reviewer_id = ?)
                    """;
            default -> """
                    SELECT COUNT(*) FROM submission
                    WHERE ? IN (
                        SELECT submission.author_id
                        UNION
                        SELECT ca.user_id FROM co_authored ca WHERE ca.submission_id = submission.id)
                    """;
        };

        sql += generateResultListParametersSQLSuffix(resultListParameters, false);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (privilege != Privilege.ADMIN) {
                stmt.setInt(1, user.getId());
            }
            fillResultListParameterSuffix(2, stmt, resultListParameters);
            rs = stmt.executeQuery();

            // Attempt to create a list of submissions from the result set.
            if (rs.next()) {
                int result = rs.getInt(1);
                logger.finer("Retrieved the number of submissions authored by the user with id " + user.getId() + ": " + result);
                return result;
            } else {
                logger.severe("The number of submissions authored by the user with id " + user.getId() +
                        " could not be retrieved.");
                transaction.abort();
                throw new DatasourceQueryFailedException("The number of submissions authored by the user with id "
                        + user.getId() + " could not be retrieved.");
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("The number of submissions could not be retrieved.", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("The list of submissions could not be retrieved.", e);
            }
        }
    }

    /**
     * Generates a suffix to a SQL query string that incorporates the result list parameters.
     * Adds <code>params.getFilterColumns().size()</code> query parameters to the SQL query
     * to be filled with patterns for values of the filter <code>this.columnNames</code> and
     * <code>columns.size()</code> parameters for the global search word.
     */
    private static String generateResultListParametersSQLSuffix(ResultListParameters params, boolean limit) {
        StringBuilder sb = new StringBuilder();

        // Filter according to date select parameter
        if (params.getDateSelect() == DateSelect.FUTURE) {
            sb.append(" AND (timestamp_submission::date >= CURRENT_DATE OR timestamp_deadline_revision::date >= CURRENT_DATE)\n");
        } else if (params.getDateSelect() == DateSelect.PAST) {
            sb.append(" AND (timestamp_deadline_revision::date <= CURRENT_DATE OR timestamp_deadline_revision::date <= CURRENT_DATE)\n");
        }

        // Filter according to submission state parameter
        if (params.getSubmissionState() != null) {
            sb.append(switch (params.getSubmissionState()) {
                case ACCEPTED -> " AND state = 'ACCEPTED'\n";
                case REJECTED -> " AND state = 'REJECTED'\n";
                case REVISION_REQUIRED -> " AND state = 'REVISION_REQUIRED'\n";
                case SUBMITTED -> " AND state = 'SUBMITTED'\n";
            });
        }

        // Filter according to filter column parameters
        // Only append if filter column is specified because filtering for a null value would not return any results
        // because of the AND operators.
        if (isFilled(params.getFilterColumns().get("title"))) {
            sb.append(" AND title ILIKE ?\n");
        }
        if (isFilled(params.getFilterColumns().get("state"))) {
            sb.append(" AND state::VARCHAR ILIKE ?\n");
        }
        if (isFilled(params.getFilterColumns().get("timestamp_submission"))) {
            sb.append(" AND timestamp_submission::VARCHAR ILIKE ?\n");
        }
        if (isFilled(params.getFilterColumns().get("timestamp_deadline_revision"))) {
            sb.append(" AND timestamp_deadline_revision::VARCHAR ILIKE ?\n");
        }
        if (isFilled(params.getFilterColumns().get("forum"))) {
            sb.append(" AND (SELECT f.name FROM scientific_forum f WHERE f.id = submission.forum_id) ILIKE ?\n");
        }


        sb.append("""
                AND (
                    title ILIKE ?
                    OR state::VARCHAR ILIKE ?
                    OR timestamp_submission::VARCHAR ILIKE ?
                    OR timestamp_deadline_revision::VARCHAR ILIKE ?
                    OR (SELECT f.name FROM scientific_forum f WHERE f.id = submission.forum_id) ILIKE ?
                )
                """);

        if (limit) {
            // Sort according to sort column parameter
            if (params.getSortColumn() != null && filterColumnNames.contains(params.getSortColumn())) {
                sb.append("ORDER BY ");
                if (params.getSortColumn().equals("forum")) { // need to get forum name
                    sb.append("(SELECT f.name FROM scientific_forum f WHERE f.id = submission.forum_id)");
                } else {
                    sb.append(params.getSortColumn());
                }
                sb.append(" ")
                        .append(params.getSortOrder() == SortOrder.ASCENDING ? "ASC" : "DESC")
                        .append("\n");
            }

            // Set limit and offset
            ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
            int paginationLength = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LIST_LENGTH"));
            sb.append("LIMIT ").append(paginationLength)
                    .append(" OFFSET ").append(paginationLength * (params.getPageNo() - 1));
        }

        // Add semicolon to end of query
        sb.append(";");

        return sb.toString();
    }

    private static int fillResultListParameterSuffix(int qParamCounter, PreparedStatement stmt, ResultListParameters params) throws SQLException {
        // Add values for filter columns and global search word
        for (String column : filterColumnNames) {
            if (params.getFilterColumns().get(column) != null && !params.getFilterColumns().get(column).isEmpty()) {
                stmt.setString(qParamCounter++, "%" + params.getFilterColumns().get(column) + "%");
            }
        }
        for (int i = 0; i< filterColumnNames.size(); i++) {
            stmt.setString(qParamCounter++,
                    "%" + Objects.requireNonNullElse(params.getGlobalSearchWord(), "") + "%");
        }

        return qParamCounter;
    }


    private static boolean isFilled(String s) {
        return s != null && !s.isEmpty();
    }
}
