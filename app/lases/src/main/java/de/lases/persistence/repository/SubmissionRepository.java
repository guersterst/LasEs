package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.inject.spi.CDI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a submission and the
 * possibility to get lists of submissions.
 *
 * @author Thomas Kirz
 */
public class SubmissionRepository {

    private static final Logger l = Logger.getLogger(SubmissionRepository.class.getName());

    private static final List<String> columnNames = List.of("id", "title", "state", "timestamp_submission",
            "requires_revision", "timestamp_deadline_revision", "author_id", "editor_id", "forum_id");

    /**
     * Takes a scientific forum dto that is filled with a valid id and returns
     * a fully filled submission dto.
     *
     * @param submission A {@code Submission} dto that must be filled
     *                   with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled {@code Submission} dto.
     * @throws NotFoundException If there is no submission with the
     *                           provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Submission get(Submission submission, Transaction transaction)
            throws NotFoundException {
        if (submission.getId() == null) {
            l.severe("The passed Submission-DTO does not contain an id.");
            throw new IllegalArgumentException("Submission id must not be null.");
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
                l.finer("Successfully retrieved submission with id " + submission.getId() + "from database.");
            } else {
                l.warning("No submission with id " + submission.getId() + " found in database.");
                throw new NotFoundException("No submission with id " + submission.getId() + " found.");
            }
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException(e.getMessage(), e);
        }

        return result;
    }

    private static Submission createSubmissionFromResultSet(ResultSet resultSet) throws SQLException {
        Submission sub = new Submission();

        // retrieve required fields
        sub.setId(resultSet.getInt("id"));
        sub.setTitle(resultSet.getString("title"));
        sub.setState(SubmissionState.valueOf(resultSet.getString("state")));
        sub.setSubmissionTime(resultSet.getTimestamp("submission_time").toLocalDateTime());
        sub.setRevisionRequired(resultSet.getBoolean("requires_revision"));
        sub.setDeadlineRevision(resultSet.getTimestamp("deadline_revision").toLocalDateTime());
        sub.setEditorId(resultSet.getInt("editor_id"));

        // retrieve optional fields
        sub.setAuthorId(resultSet.getInt("author_id"));
        sub.setScientificForumId(resultSet.getInt("forum_id"));

        return sub;
    }

    /**
     * Adds a submission to the repository.
     *
     * @param submission A submission dto filled with the required fields.
     *                   Required is:
     *                   <ul>
     *                   <li> scientificForumId </li>
     *                   <li> authorId </li>
     *                   <li> editorId </li>
     *                   <li> title </li>
     *                   <li> state </li>
     *                   <li> submissionTime </li>
     *                   </ul>
     *                   (The id must not be specified, as the repository will
     *                   create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the required fields of the
     *                                submission is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(Submission submission, Transaction transaction)
            throws DataNotWrittenException {
    }

    /**
     * Changes the given submission in the repository.
     *
     * @param submission A submission dto filled with the required fields.
     *                   Required is:
     *                   <ul>
     *                   <li> id </li>
     *                   <li> scientificForumId </li>
     *                   <li> authorId </li>
     *                   <li> editorId </li>
     *                   <li> title </li>
     *                   <li> state </li>
     *                   <li> submissionTime </li>
     *                   </ul>
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no submission with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the required fields of the
     *                                submission is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Takes a submission dto that is filled with a valid id and removes
     * this submission from the repository.
     *
     * @param submission The submission to remove. Must be filled
     *                   with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified submission was not found in
     *                           the repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all submissions that belong to the specified user under the
     * specified role (for example as editor, submitter, reviewer) from the
     * specified scientific forum.
     *
     * @param user A {@code User} dto with a valid id.
     * @param privilege As which role should the user act.
     * @param scientificForum A {@code ScientificForum} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     *         belong to the specified user in the specified role and the
     *         specified scientific forum.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no user with the provided id or no
     *                           scientific forum with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (user.getId() == null || scientificForum.getId() == null) {
            l.severe("A passed DTO is not sufficiently filled.");
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
                        WHERE forum_id = ? AND author_id = ?
                        """;
            };

            sql += generateResultListParametersSQLSuffix(resultListParameters);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, scientificForum.getId());
                stmt.setInt(2, user.getId());
                fillResultListParameterSuffix(3, stmt, resultListParameters);
                resultSet = stmt.executeQuery();

                // Attempt to create a list of submissions from the result set.
                while (resultSet.next()) {
                    result.add(createSubmissionFromResultSet(resultSet));
                }
                l.finer("Retrieved a list of submissions from the database.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     * Gets a list all submissions that belong to the specified user under the
     * specified role (for example as editor, submitter, reviewer).
     *
     * @param user A {@code User} dto with a valid id.
     * @param privilege As which role should the user act.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     *         belong to the specified user in the specified role.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (user.getId() == null) {
            l.severe("Passed  User-DTO is not sufficiently filled.");
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
                    WHERE author_id = ?
                    """;
        };

        sql += generateResultListParametersSQLSuffix(resultListParameters);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (privilege != Privilege.ADMIN) {
                stmt.setInt(1, user.getId());
            }
            fillResultListParameterSuffix(2, stmt, resultListParameters);
            resultSet = stmt.executeQuery();

            // Attempt to create a list of submissions from the result set.
            while (resultSet.next()) {
                result.add(createSubmissionFromResultSet(resultSet));
            }
            l.finer("Retrieved a list of submissions from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Gets a list of all submissions that belong to the specified scientific
     * forum.
     *
     * @param scientificForum A {@code ScientificForum} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     *
     * @return A list of fully filled submission dtos for all reviews that
     *         belong to the specified scientific forum.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Submission> getList(ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (scientificForum.getId() == null) {
            l.severe("Passed User-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User ScientificForum id must not be null.");
        }

        Connection conn = transaction.getConnection();

        ResultSet resultSet;
        List<Submission> result = new ArrayList<>();

        String sql = """
                    SELECT * FROM submission
                    WHERE forum_id = ?
                    """;
        sql += generateResultListParametersSQLSuffix(resultListParameters);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scientificForum.getId());
            fillResultListParameterSuffix(2, stmt, resultListParameters);
            resultSet = stmt.executeQuery();

            // Attempt to create a list of submissions from the result set.
            while (resultSet.next()) {
                result.add(createSubmissionFromResultSet(resultSet));
            }
            l.finer("Retrieved a list of submissions from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds the specified user to the specified submission as a co-author.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
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
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Adds the specified user to the specified submission as a reviewer.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addReviewer(Submission submission, User user,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Removes the specified co-author from the specified scientific forum.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id, which is a co-author in the
     *             aforementioned submission.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id or the provided user is not
     *                           a co-author for the provided submission.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
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
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id, which is a reviewer in the
     *             aforementioned submission.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id or the provided user is not
     *                           a reviewer for the provided submission.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
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
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return The number of submission the specified user authored.
     * @throws NotFoundException If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static int countSubmissions(User user, Transaction transaction)
            throws NotFoundException {
        return 0;
    }

    /**
     * Generates a suffix to a SQL query string that incorporates the result list parameters.
     * Adds <code>params.getFilterColumns().size()</code> query parameters to the SQL query
     * to be filled with patterns for values of the filter <code>this.columnNames</code> and
     * <code>columns.size()</code> parameters for the global search word.
     */
    private static String generateResultListParametersSQLSuffix(ResultListParameters params) {
        StringBuilder sb = new StringBuilder();

        // Filter according to date select parameter
        if (params.getDateSelect() == DateSelect.FUTURE) {
            sb.append(" AND (timestamp_submission >= CURRENT_DATE || timestamp_deadline_revision >= CURRENT_DATE)\n");
        } else if (params.getDateSelect() == DateSelect.PAST) {
            sb.append(" AND (timestamp_deadline_revision <= CURRENT_DATE || timestamp_deadline_revision <= CURRENT_DATE)\n");
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

        // Filter according to filter columns parameter.
        columnNames.forEach(column -> sb.append(" AND ").append(column).append(" LIKE ?\n"));

        // Filter according to global search word.
        sb.append(" AND (");
        for (int i = 0; i < columnNames.size(); i++) {
            sb.append(columnNames.get(i)).append(" LIKE ?\n");
            if (i < columnNames.size() - 1) {
                sb.append("OR ");
            }
        }

        // Sort according to sort column parameter
        if (!"".equals(params.getSortColumn()) && columnNames.contains(params.getSortColumn())) {
            sb.append("ORDER BY ")
                    .append(params.getSortColumn())
                    .append(" ")
                    .append(params.getSortOrder() == SortOrder.ASCENDING ? "ASC" : "DESC")
                    .append("\n");
        }

        // Set limit and offset
        ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
        int paginationLength = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LENGTH"));
        sb.append("LIMIT ").append(paginationLength)
                .append("OFFSET ").append(paginationLength * params.getPageNo());

        // Add semicolon to end of query
        sb.append(";");

        return sb.toString();
    }

    private static int fillResultListParameterSuffix(int qParamCounter, PreparedStatement stmt, ResultListParameters params) throws SQLException {
        // Add values for filter columns and global search word
        for (int i = 0; i < 2; i++) {
            for (String column : columnNames) {
                stmt.setString(qParamCounter++,
                        "%" + Objects.requireNonNullElse(params.getFilterColumns().get(column), "") + "%");
            }
        }

        return qParamCounter;
    }

}
