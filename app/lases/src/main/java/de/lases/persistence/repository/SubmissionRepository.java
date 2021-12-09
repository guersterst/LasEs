package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;
import org.postgresql.util.PSQLException;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a submission and the
 * possibility to get lists of submissions.
 */
public class SubmissionRepository {

    private static final Logger logger = Logger.getLogger(SubmissionRepository.class.getName());

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
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Submission get(Submission submission, Transaction transaction)
            throws NotFoundException {
        return null;
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
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the required fields of the
     *                                        submission is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @return The submission that was added, but filled with its id.
     */
    public static Submission add(Submission submission, Transaction transaction)
            throws DataNotWrittenException {
        // TODO: die ids auch noch checken, falls die mitlerweile auch Integer sind
        if (submission.getTitle() == null || submission.getState() == null || submission.getSubmissionTime() == null) {
            throw new InvalidFieldsException("At least one of the required fields of the submission was null");
        }

        Connection conn = transaction.getConnection();
        Integer id = null;
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                    SELECT max(id) FROM submission
                    """);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1) + 1;
            } else {
                id = 0;
            }
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            throw new DatasourceQueryFailedException("A datasource exception"
                    + "occurred", ex);
        }
        submission.setId(id);

        try {
            PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO submission
                    VALUES (?, ?, CAST(? as submission_state), ?, ?, ?, ?, ?, ?)
                    """);
            stmt.setInt(1, submission.getId());
            stmt.setString(2, submission.getTitle());
            stmt.setString(3, submission.getState().toString());
            stmt.setTimestamp(4, Timestamp.valueOf(submission.getSubmissionTime()));
            stmt.setBoolean(5, submission.isRevisionRequired());

            Timestamp deadline = submission.getDeadlineRevision() == null ? null
                    : Timestamp.valueOf(submission.getDeadlineRevision());
            stmt.setTimestamp(6, deadline);

            stmt.setInt(7, submission.getAuthorId());
            stmt.setInt(8, submission.getEditorId());
            stmt.setInt(9, submission.getScientificForumId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("A datasource exception "
                    + "occurred", ex);
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

        try {
            ResultSet find = findSubmission(submission, connection);


        } catch (SQLException exception) {
            logger.fine("Removing submission with the submission id: " + submission.getId());
            throw new NotFoundException();
        }

        try {
            PreparedStatement statement = connection.prepareStatement(
                    """
                            DELETE FROM submission
                            WHERE id = ?
                            """
            );
            statement.setInt(1, submission.getId());

            statement.executeUpdate();

        }catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while removing a submission.", exception);
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
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
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
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
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
     */
    public static List<Submission> getList(ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Adds the specified user to the specified submission as a co-author.
     *
     * @param submission  A scientific forum dto with a valid id.
     * @param user        A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws InvalidFieldsException If one of the ids is null.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addCoAuthor(Submission submission, User user,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (user.getId() == null || submission.getId() == null) {
            transaction.abort();
            String nullArgument = user.getId() == null ? "user": "submission";
            throw new InvalidFieldsException("The ids of the " + nullArgument + " must not be null");
        }
        Connection conn = transaction.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO co_authored
                    VALUES (?, ?)
                    """);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, submission.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);

            // 23503: Foreign key constraint violated
            if (ex.getSQLState().equals("23503")) {
                throw new NotFoundException("Either the specified user or submission does not exist");
            } else if (! (ex instanceof PSQLException)) {
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
     * @param submission  A scientific forum dto with a valid id.
     * @param user        A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no user with the
     *                                        provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addReviewer(Submission submission, User user,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
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
     * Removes the specified reviewer from the specified scientific forum.
     *
     * @param submission  A scientific forum dto with a valid id.
     * @param user        A user dto with a valid id, which is a reviewer in the
     *                    aforementioned submission.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no user with the
     *                                        provided id or the provided user is not
     *                                        a reviewer for the provided submission.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeReviewer(Submission submission, User user,
                                      Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Count the number of submissions where the specified user is author.
     *
     * @param user        A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return The number of submission the specified user authored.
     * @throws NotFoundException              If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static int countSubmissions(User user, Transaction transaction)
            throws NotFoundException {
        return 0;
    }

    private static ResultSet findSubmission(Submission submission, Connection connection) throws SQLException {
        PreparedStatement find = connection.prepareStatement(
                """
                        SELECT *
                        FROM submission
                        WHERE id = ?
                        """
        );
        find.setInt(1, submission.getId());

        return find.executeQuery();
    }

}
