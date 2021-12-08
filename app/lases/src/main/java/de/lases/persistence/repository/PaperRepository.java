package de.lases.persistence.repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.util.DatasourceUtil;
import jakarta.enterprise.inject.spi.CDI;
import org.postgresql.util.PSQLException;

/**
 * @author Stefanie Guerster, Sebastian Vogt
 * <p>
 * Offers get/add/change/remove operations on a paper and the possibility to
 * get lists of papers.
 */
public class PaperRepository {

    private static final List<String> paperColumnNames = List.of("version","timestamp_upload","is_visible");

    private static final Logger logger = Logger.getLogger(PaperRepository.class.getName());

    /**
     * Takes a paper dto that is filled out with a valid id and submission id
     * and returns a fully filled paper dto.
     *
     * @param paper       A paper dto that must be filled with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled paper dto.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Paper get(Paper paper, Transaction transaction) throws NotFoundException {

        //TODO: brauch ich eigentlich überall?
        if (paper.getSubmissionId() == null) {
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM paper
                    WHERE submission_id = ? AND version = ?
                    """);
            statement.setInt(1, paper.getSubmissionId());
            statement.setInt(2, paper.getVersionNumber());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Paper resultPaper = new Paper();

                resultPaper.setSubmissionId(resultSet.getInt("submission_id"));
                resultPaper.setVisible(resultSet.getBoolean("is_visible"));
                resultPaper.setVersionNumber(resultSet.getInt("version"));
                resultPaper.setUploadTime(resultSet.getTimestamp("timestamp_upload").toLocalDateTime());

                return resultPaper;
            } else {
                logger.fine("Loading paper with the submission id: " + paper.getSubmissionId()
                        + " and version number: " + paper.getVersionNumber());
                throw new NotFoundException();
            }

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw new DatasourceQueryFailedException("A data source exception occurred.", exception);
        }
    }

    /**
     * Adds a paper to the repository.
     *
     * @param paper       A fully filled paper dto. (The id must not be
     *                    specified, as the repository will create the id)
     * @param pdf         The pdf file belonging to the paper.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the fields of the paper is
     *                                        null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(Paper paper, FileDTO pdf, Transaction transaction) throws DataNotWrittenException {
        Connection conn = transaction.getConnection();

        if (paper.getUploadTime() == null) {
            throw new InvalidFieldsException("The upload time of the paper "
                    + "must not be null!");
        }
        if (pdf.getFile() == null) {
            throw new InvalidFieldsException("The file in the pdf must not "
                    + "be null!");
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO paper
                    VALUES (?, ?, ?, ?, ?)
                    """);
            stmt.setInt(1, paper.getVersionNumber());
            stmt.setInt(2, paper.getSubmissionId());
            stmt.setTimestamp(3, Timestamp.valueOf(paper.getUploadTime()));
            stmt.setBoolean(4, paper.isVisible());
            stmt.setBytes(5, pdf.getFile());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            String sqlState = ex.getSQLState();
            throw new DatasourceQueryFailedException("A datasource exception"
                    + "occurred", ex);
        }
    }

    /**
     * Changes the given paper in the repository.
     *
     * @param paper       A fully filled paper dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the fields of the paper is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Paper paper, Transaction transaction) throws NotFoundException, DataNotWrittenException {
        if (paper.getSubmissionId() == null) {
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();

        try {
            ResultSet resultSet = findPaper(paper, connection);

            if (!resultSet.next()) {
                logger.fine("Changing paper with the submission id: " + paper.getSubmissionId()
                        + " and version number: " + paper.getVersionNumber());
                throw new NotFoundException();
            }

            PreparedStatement statement = connection.prepareStatement(
                    """
                            UPDATE paper
                            SET is_visible = ?
                            WHERE version = ? AND submission_id = ?
                            """
            );
            statement.setBoolean(1, paper.isVisible());
            statement.setInt(2, paper.getVersionNumber());
            statement.setInt(3, paper.getSubmissionId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while changing paper data.", exception);
        }
    }

    /**
     * Takes a paper dto that is filled with a valid id and submission id and
     * removes this paper from the repository.
     *
     * @param paper       The paper to remove. Must be filled with a valid id and
     *                    submission id.
     * @param transaction The transaction to use.
     * @throws NotFoundException              The specified paper was not found in the
     *                                        repository.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Paper paper, Transaction transaction) throws NotFoundException, DataNotWrittenException {

        //TODO: nicht dokumentiert
        if (paper.getSubmissionId() == null) {
            throw new IllegalArgumentException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            throw new IllegalArgumentException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();

        try {
            ResultSet resultSet = findPaper(paper, connection);

        }catch (SQLException exception) {
            logger.fine("Removing paper with the submission id: " + paper.getSubmissionId()
                    + " and version number: " + paper.getVersionNumber());
            throw new NotFoundException();
        }

        try {


            PreparedStatement statement = connection.prepareStatement(
                    """
                            DELETE FROM paper
                            WHERE version = ? AND submission_id = ?
                            """
            );
            statement.setInt(1, paper.getVersionNumber());
            statement.setInt(2, paper.getSubmissionId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while removing a paper.", exception);
        }

    }

    /**
     * Gets a list all papers that belong to the specified submission.
     * <p>
     * The papers returned are determined by the highest privilege, which that
     * user possesses on that submission.
     * </p>
     *
     * @param submission           A submission dto filled with a valid id.
     * @param transaction          The transaction to use.
     * @param user                 The user who requests the papers.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled paper dtos for all papers that belong
     * to the specified submission.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws NotFoundException              If there is no submission with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     */
    public static List<Paper> getList(Submission submission, Transaction transaction, User user, ResultListParameters resultListParameters) throws DataNotCompleteException, NotFoundException {
        if (transaction == null || resultListParameters == null) {
            logger.severe("Invalid parameters for loading a list of papers belonging to a submission. Parameter is null.");
            throw new InvalidQueryParamsException();
        }

        if (submission.getId() == null || user.getId() == null) {
            logger.fine("Loading a list of paper with the submission id: " + submission.getId()
                    + " and a user, who requests it, with the id: " + user.getId());
            throw new NotFoundException();
        }

        Connection connection = transaction.getConnection();
        ResultSet resultSet;
        List<Paper> paperList = new LinkedList<>();

        //Privilege of the given user in this submission
        Privilege privilege;
        if (user.isAdmin()) {
            privilege = Privilege.ADMIN;
        } else if (user.getId() == submission.getEditorId()) {
            privilege = Privilege.EDITOR;
        } else if (user.getId() == submission.getAuthorId()) {
            privilege = Privilege.AUTHOR;
        } else {
            //Has no matching privileges.
            return paperList;
        }

        String sqlStatment = switch (privilege) {
            case ADMIN -> """
                    SELECT * FROM paper p, submission s
                    WHERE s.id = ? 
                    AND p.submission_id = s.id
                    """;
            case EDITOR -> """
                    SELECT * FROM paper p, submission s
                    WHERE s.id = ? 
                    AND s.editor_id = ?
                    AND p.submission_id = s.id
                    """;
            default -> """
                    SELECT * FROM paper p, submission s
                    WHERE s.id = ? 
                    AND s.author_id = ?
                    AND p.is_visible = TRUE
                    AND p.submission_id = s.id
                    """;
        };

        sqlStatment += generateSQLForResultListParameters(resultListParameters, privilege);

        try {
            PreparedStatement statement = connection.prepareStatement(sqlStatment);

            statement.setInt(1, submission.getId());

            if (privilege == Privilege.EDITOR) {
                statement.setInt(2, submission.getEditorId());
            }
            if (privilege == Privilege.AUTHOR) {
                statement.setInt(2, submission.getAuthorId());
            }

            fillUpStatement(2, statement, resultListParameters);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                for (Paper paper : paperList) {
                    paper.setVisible(resultSet.getBoolean("is_visible"));
                    paper.setVersionNumber(resultSet.getInt("version"));
                    paper.setUploadTime(resultSet.getTimestamp("timestamp_upload").toLocalDateTime());
                    paper.setSubmissionId(resultSet.getInt("submission_id"));
                }
            }

        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while loading all papers of a submission.", e);

        }
        return paperList;
    }


    private static String generateSQLForResultListParameters(ResultListParameters parameters, Privilege privilege) {
        StringBuilder stringBuilder = new StringBuilder();

        // Filter according to visibility.
        if (!parameters.isVisibleFilter() && (privilege == Privilege.ADMIN || privilege == Privilege.EDITOR)) {
            stringBuilder.append(" AND p.is_visible = FALSE");
        } else if (parameters.isVisibleFilter()) {
            stringBuilder.append(" AND p.is_visible = TRUE");
        }


        //Filter columns of a pagination.
        paperColumnNames.forEach(column -> stringBuilder.append(" AND p.").append(column).append(" LINKE ?\n"));

        //Filter global search word.
        stringBuilder.append(" AND (");
        for (int i = 0; i < paperColumnNames.size(); i++) {
            stringBuilder.append(paperColumnNames.get(i)).append(" LIKE ?\n");
            if (i < paperColumnNames.size() - 1) {
                stringBuilder.append("OR ");
            }
        }
        stringBuilder.append(")");

        // Sort according to sort column parameter
        if (!"".equals(parameters.getSortColumn()) && paperColumnNames.contains(parameters.getSortColumn())) {
            stringBuilder.append("ORDER BY ")
                    .append(parameters.getSortColumn())
                    .append(" ")
                    .append(parameters.getSortOrder() == SortOrder.ASCENDING ? "ASC" : "DESC")
                    .append("\n");
        }

        // Set limit and offset
        ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
        int pgLength = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LIST_LENGTH"));
        stringBuilder.append("LIMIT ")
                .append(pgLength)
                .append("OFFSET ")
                .append(pgLength * parameters.getPageNo());

        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    private static void fillUpStatement(int count, PreparedStatement statement, ResultListParameters parameters) throws SQLException {
        // Add value for each ? in the statement that isn't filled until now.
        // Two times. One for filter, one for global search.
        for (int i = 0; i < 2; i++) {
            for (String column : paperColumnNames) {
                statement.setString(count++, "%" + Objects.requireNonNullElse(parameters.getFilterColumns().get(column), "") + "%");
            }
        }
    }

    /**
     * Get the PDF file for the provided paper.
     *
     * @param paper       A paper dto filled with a valid paper id and submission id.
     * @param transaction The transaction to use.
     * @return A file containing the PDF for the specified paper.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getPDF(Paper paper, Transaction transaction) throws NotFoundException {

        //TODO: nicht dokumentiert
        if (paper.getSubmissionId() == null) {
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();

        try {

            PreparedStatement statement = connection.prepareStatement(
                    """
                            SELECT pdf_file
                            FROM paper
                            WHERE version = ? AND submission_id = ?
                            """
            );
            statement.setInt(1, paper.getVersionNumber());
            statement.setInt(2, paper.getSubmissionId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                FileDTO file = new FileDTO();
                file.setFile(resultSet.getBytes("pdf_file"));
                return file;

            } else {
                logger.fine("Loading paper with the submission id: " + paper.getSubmissionId()
                        + " and version number: " + paper.getVersionNumber());
                throw new NotFoundException();
            }

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while loading a file.", exception);
        }
    }

    /**
     * Takes a submission dto that is filled out with a valid id
     * and returns a fully filled paper dto for the newest paper in that
     * submission.
     *
     * @param submission  A submission dto that must be filled with a valid id.
     * @param user        The user who requests the papers.
     * @param transaction The transaction to use.
     * @return A fully filled paper dto.
     * @throws NotFoundException              If there is no submission with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Paper getNewestPaperForSubmission(Submission submission, User user, Transaction transaction) throws NotFoundException {
        if (submission.getId() == null) {
            throw new InvalidFieldsException("The id of a submission must not be null.");
        }

        Connection connection = transaction.getConnection();
        Paper paper = new Paper();

        try {
            //TODO: notfound exception 2x?
            PreparedStatement find = connection.prepareStatement(
                    """
                            SELECT s.*
                            FROM submission s, \"user\" u
                            WHERE s.id = ? AND u.id = ? AND s.author_id = u.id
                            """
            );
            find.setInt(1, submission.getId());
            find.setInt(2, user.getId());

            ResultSet found = find.executeQuery();

            if (!found.next()) {
                logger.fine("Searching for a submission with the id: " + submission.getId()
                        + " for an author with the id: " + user.getId() + " in order to proof if the ids are valid.");
                throw new NotFoundException();
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException("A datasource exception occured while looking for the submission.", e);
        }

        try {
            PreparedStatement statement = connection.prepareStatement(
                    """
                            SELECT p2.*
                            FROM paper p2, ( SELECT s.* FROM submission s, \"user\" u WHERE s.id = ? AND u.id = ? AND s.author_id = u.id) AS sub 
                            WHERE p2.submission_id = sub.id
                            AND p2.version = (SELECT max (p3.version) from paper p3 WHERE sub.id = p3.submission_id)
                                                        """
            );

            statement.setInt(1, submission.getId());
            statement.setInt(2, user.getId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                paper.setVersionNumber(resultSet.getInt("version"));
                paper.setSubmissionId(resultSet.getInt("submission_id"));
                paper.setUploadTime(resultSet.getTimestamp("timestamp_upload").toLocalDateTime());
                paper.setVisible(resultSet.getBoolean("is_visible"));

            } else {
                logger.fine("Loading newest paper of a submission with the id: " + submission.getId()
                        + " for an author with the id: " + user.getId());
                throw new NotFoundException();
            }

        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while loading the newest paper of a submission.", exception);

        }
        return paper;
    }

    //TODO
    private static ResultSet findPaper(Paper paper, Connection connection) throws SQLException {

        PreparedStatement find = connection.prepareStatement(
                """
                        SELECT *
                        FROM paper
                        WHERE version = ? AND submission_id = ?
                        """
        );
        find.setInt(1, paper.getVersionNumber());
        find.setInt(2, paper.getSubmissionId());

        return find.executeQuery();
    }

}
