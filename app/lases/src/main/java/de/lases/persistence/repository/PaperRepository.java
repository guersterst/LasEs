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

    private static final List<String> paperColumnNames = List.of("version", "timestamp_upload", "is_visible");

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

        if (paper.getSubmissionId() == null) {
            transaction.abort();
            logger.severe("Invalid submission id while try to load a paper.");
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            transaction.abort();
            logger.severe("Invalid version number while try to change a paper.");
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();
        String sql = "SELECT * FROM paper WHERE submission_id = ? AND version = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {

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
                logger.warning("Loading paper with the submission id: " + paper.getSubmissionId()
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
        String sqlMaxVersion = "SELECT max(version) FROM paper WHERE submission_id = ?";

        if (paper.getUploadTime() == null) {
            transaction.abort();
            throw new InvalidFieldsException("The upload time of the paper "
                    + "must not be null!");
        }
        if (pdf.getFile() == null) {
            transaction.abort();
            throw new InvalidFieldsException("The file in the pdf must not "
                    + "be null!");
        }
        if (paper.getSubmissionId() == null) {
            transaction.abort();
            throw new InvalidFieldsException("The submission id must not be null!");
        }

        Integer id = null;

        try (PreparedStatement stmt = conn.prepareStatement(sqlMaxVersion)){

            stmt.setInt(1, paper.getSubmissionId());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1) + 1;
            } else {
                id = 0;
            }
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("A datasource exception"
                    + "occurred", ex);
        }

        String sqlInsert = "INSERT INTO paper VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)){
            stmt.setInt(1, id);
            stmt.setInt(2, paper.getSubmissionId());
            stmt.setTimestamp(3, Timestamp.valueOf(paper.getUploadTime()));
            stmt.setBoolean(4, paper.isVisible());
            stmt.setBytes(5, pdf.getFile());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            transaction.abort();
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
            transaction.abort();
            logger.severe("Invalid submission id while try to change a paper.");
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            transaction.abort();
            logger.severe("Invalid version number while try to change a paper.");
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();

        try {
            findPaper(paper, connection);

        } catch (SQLException exception) {
            logger.warning("Searching paper failed. Tried to change paper");
            throw new NotFoundException();
        }
        String sql = "UPDATE paper SET is_visible = ? WHERE version = ? AND submission_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setBoolean(1, paper.isVisible());
            statement.setInt(2, paper.getVersionNumber());
            statement.setInt(3, paper.getSubmissionId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            transaction.abort();
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

        if (paper.getSubmissionId() == null) {
            transaction.abort();
            logger.severe("Invalid submisison id when tried to remove paper");
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            transaction.abort();
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();

        try {
            ResultSet resultSet = findPaper(paper, connection);

        } catch (SQLException exception) {
            logger.warning("Removing paper with the submission id: " + paper.getSubmissionId()
                    + " and version number: " + paper.getVersionNumber());
            throw new NotFoundException();
        }

        String sql = "DELETE FROM paper WHERE version = ? AND submission_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, paper.getVersionNumber());
            statement.setInt(2, paper.getSubmissionId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            transaction.abort();
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
            transaction.abort();
            logger.severe("Invalid parameters for loading a list of papers belonging to a submission. Parameter is null.");
            throw new InvalidQueryParamsException();
        }

        if (submission.getId() == null || user.getId() == null) {
            transaction.abort();
            logger.warning("Loading a list of paper with the submission id: " + submission.getId()
                    + " and a user, who requests it, with the id: " + user.getId());
            throw new NotFoundException();
        }

        List<Paper> paperList = new LinkedList<>();

        Connection connection = transaction.getConnection();
        ResultSet resultSet;

        //Privilege of the given user in this submission
        Privilege privilege = null;
        if (user.isAdmin()) {
            privilege = Privilege.ADMIN;
        } else if (user.getId() == submission.getEditorId()) {
            privilege = Privilege.EDITOR;
        } else if (user.getId() == submission.getAuthorId()) {
            privilege = Privilege.AUTHOR;
        } else {
            List<User> reviewers = UserRepository.getList(transaction, submission, Privilege.REVIEWER);
            for (User reviewer : reviewers) {
                if (reviewer.getId().equals(user.getId())) {
                    privilege = Privilege.REVIEWER;
                }
            }

            if (privilege == null) {
                //Has no matching privileges.
                return paperList;
            }

        }

        String sqlStatment = switch (privilege) {
            case ADMIN -> """
                    SELECT p.* FROM paper p, submission s
                    WHERE s.id = ? 
                    AND p.submission_id = s.id
                    """;
            case EDITOR -> """
                    SELECT p.* FROM paper p, submission s
                    WHERE s.id = ? 
                    AND s.editor_id = ?
                    AND p.submission_id = s.id
                    """;
            case REVIEWER -> """
                    SELECT p.* FROM paper p, submission s , reviewed_by r
                    WHERE s.id = ?
                    AND r.reviewer_id = ?
                    AND s.id = r.submission_id
                    AND p.submission_id = s.id
                    """;
            default -> """
                    SELECT p.* FROM paper p, submission s
                    WHERE s.id = ? 
                    AND s.author_id = ?
                    AND p.submission_id = s.id
                    """;
        };

        sqlStatment += generateSQLForResultListParameters(resultListParameters, privilege);

        try (PreparedStatement statement = connection.prepareStatement(sqlStatment)){
            statement.setInt(1, submission.getId());

            if (privilege == Privilege.EDITOR) {
                statement.setInt(2, submission.getEditorId());
            }else if (privilege == Privilege.AUTHOR) {
                statement.setInt(2, submission.getAuthorId());
            } else if (privilege == Privilege.REVIEWER) {
                statement.setInt(2, user.getId());
            }


            if (resultListParameters.getFilterColumns().get("version") != null
                    && !resultListParameters.getFilterColumns().get("version").isEmpty()) {
                int index = 3;
                if (privilege == Privilege.ADMIN) {
                    index = 2;
                }
                statement.setString(index, resultListParameters.getFilterColumns().get("version"));
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Paper paper = new Paper();
                paper.setVisible(resultSet.getBoolean("is_visible"));
                paper.setVersionNumber(resultSet.getInt("version"));
                paper.setUploadTime(resultSet.getTimestamp("timestamp_upload").toLocalDateTime());
                paper.setSubmissionId(resultSet.getInt("submission_id"));

                paperList.add(paper);
            }

        } catch (SQLException e) {
            transaction.abort();
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException("A datasource exception occurred while loading all papers of a submission.", e);

        }

        return paperList;
    }


    private static String generateSQLForResultListParameters(ResultListParameters parameters, Privilege privilege) {
        StringBuilder stringBuilder = new StringBuilder();

        // Filter according to visibility.
        if (privilege != Privilege.REVIEWER && parameters.getVisibleFilter() == Visibility.NOT_RELEASED) {
            stringBuilder.append(" AND p.is_visible = FALSE ");
        } else if (parameters.getVisibleFilter() == Visibility.RELEASED || privilege == Privilege.REVIEWER) {
            stringBuilder.append(" AND p.is_visible = TRUE ");
        }


        stringBuilder.append("""
                AND p.timestamp_upload < NOW()
                """);

        if (parameters.getFilterColumns().get("version") != null
                && !parameters.getFilterColumns().get("version").isEmpty()) {
            stringBuilder.append("AND p.version::VARCHAR ILIKE ?\n");
        }

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
                .append(" ")
                .append("OFFSET ")
                .append(pgLength * (parameters.getPageNo() - 1));

        stringBuilder.append(";");

        return stringBuilder.toString();
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

        if (paper.getSubmissionId() == null) {
            transaction.abort();
            logger.severe("Invalid submission id when tried to get file.");
            throw new InvalidFieldsException("The submission id of the paper must not be null.");
        }

        if (paper.getVersionNumber() == null) {
            transaction.abort();
            logger.severe("Invalid version number wehn tried to get file.");
            throw new InvalidFieldsException("The version number of the paper must not be null.");
        }

        Connection connection = transaction.getConnection();
        String sql = "SELECT pdf_file FROM paper WHERE version = ? AND submission_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, paper.getVersionNumber());
            statement.setInt(2, paper.getSubmissionId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                FileDTO file = new FileDTO();
                file.setFile(resultSet.getBytes("pdf_file"));
                return file;

            } else {
                logger.warning("Loading paper with the submission id: " + paper.getSubmissionId()
                        + " and version number: " + paper.getVersionNumber());
                throw new NotFoundException();
            }

        } catch (SQLException exception) {
            transaction.abort();
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
     * @param transaction The transaction to use.
     * @return A fully filled paper dto.
     * @throws NotFoundException              If there is no submission with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Paper getNewestPaperForSubmission(Submission submission, Transaction transaction) throws NotFoundException {
        if (submission == null) {
            logger.severe("Invalid null trying to get newest paper.");
            throw new InvalidFieldsException("The submission must not be null.");
        }
        if (submission.getId() == null) {
            transaction.abort();
            logger.severe("Invalid submission id when tried to get newest paper.");
            throw new InvalidFieldsException("The id of a submission must not be null.");
        }

        Connection connection = transaction.getConnection();
        String sql = "SELECT p.* FROM submission s, paper p WHERE s.id = p.submission_id AND s.id = ? ORDER BY p.timestamp_upload ASC";
        Paper paper = new Paper();

        try (PreparedStatement find = connection.prepareStatement(sql)) {
            find.setInt(1, submission.getId());

            ResultSet found = find.executeQuery();

            if (found.next()) {
                paper.setSubmissionId(submission.getId());
                paper.setVersionNumber(found.getInt("version"));
                paper.setVisible(found.getBoolean("is_visible"));
                paper.setUploadTime(found.getTimestamp("timestamp_upload").toLocalDateTime());
                return paper;
            } else {
                logger.fine("No paper exists for submission: " + submission + ". When trying to find newest paper.");
                throw new NotFoundException("No paper exists for this submission: " + submission);
            }
        } catch (SQLException e) {
            logger.severe("Finding newest paper: " + e.getMessage());
            throw new DatasourceQueryFailedException(e.getMessage());
        }
    }


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

        ResultSet rs = find.executeQuery();
        find.close();
        return rs;
    }

}
