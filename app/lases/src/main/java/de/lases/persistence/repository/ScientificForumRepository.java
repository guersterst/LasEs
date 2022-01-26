package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.util.DatasourceUtil;
import de.lases.persistence.util.TransientSQLExceptionChecker;
import jakarta.enterprise.inject.spi.CDI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a scientific forum and the
 * possibility to get lists of scientific forums.
 *
 * @author Thomas Kirz
 * @author Johannes Garstenauer
 */
public class ScientificForumRepository {

    private static final Logger logger = Logger.getLogger(ScientificForumRepository.class.getName());

    /**
     * Takes a scientific forum dto that is filled with a valid id and returns a fully filled scientific forum dto.
     *
     * @param scientificForum A {@code ScientificForum} dto that must be filled with a valid id.
     * @param transaction     The transaction to use.
     * @return A fully filled {@code ScientificForum} dto.
     * @throws NotFoundException              If there is no scientific forum with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be queried.
     * @author Thomas Kirz
     */
    public static ScientificForum get(ScientificForum scientificForum,
                                      Transaction transaction)
            throws NotFoundException {
        if (scientificForum.getId() == null) {
            logger.severe("The passed ScientificForum-DTO does not contain an id.");
            throw new IllegalArgumentException("ScientificForum id must not be null.");
        }

        Connection conn = transaction.getConnection();
        String sql = "SELECT * FROM scientific_forum WHERE id = ?";

        ScientificForum result;
        ResultSet resultSet;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scientificForum.getId());
            resultSet = stmt.executeQuery();

            // Attempt to create a scientific forum from the result set.
            if (resultSet.next()) {
                result = createScientificForumFromResultSet(resultSet);
                logger.finer("Retrieved scientific forum with id " + scientificForum.getId());
            } else {
                logger.warning("No scientific forum with id " + scientificForum.getId() + " found in database.");
                throw new NotFoundException("No scientific forum with id " + scientificForum.getId());
            }
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException(e.getMessage(), e);
        }

        return result;
    }

    private static ScientificForum createScientificForumFromResultSet(ResultSet resultSet) throws SQLException {
        ScientificForum forum = new ScientificForum();

        // retrieve fields from result set
        forum.setId(resultSet.getInt("id"));
        forum.setName(resultSet.getString("name"));
        forum.setDescription(resultSet.getString("description"));
        forum.setUrl(resultSet.getString("url"));
        forum.setReviewManual(resultSet.getString("review_manual"));
        Timestamp ts = resultSet.getTimestamp("timestamp_deadline");
        forum.setDeadline(ts == null ? null : ts.toLocalDateTime());

        return forum;
    }

    /**
     * Takes a scientific forum dto that is filled with an id or a
     * name and returns if this scientific forum exists.
     *
     * @param scientificForum A {@code ScientificForum} dto that must be filled
     *                        with an id or name.
     * @param transaction     The transaction to use.
     * @return Does this scientific forum exist?
     * @throws InvalidFieldsException         If neither name nor id are provided.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static boolean exists(ScientificForum scientificForum,
                                 Transaction transaction) {
        if (scientificForum.getId() == null && scientificForum.getName() == null) {
            logger.severe("To query the scientific forum's existence an id or a name must exist.");
            throw new InvalidFieldsException();
        }

        String baseSQL = """
                SELECT id
                FROM scientific_forum
                WHERE
                """;
        String sql_with_id = baseSQL + " id = ?";
        String sql_with_name = baseSQL + " name = ?";
        String sqlRes;

        // Determine whether to query with id or name.
        boolean hasIdFlag = scientificForum.getId() != null;
        if (hasIdFlag) {
            sqlRes = sql_with_id;
        } else {
            sqlRes = sql_with_name;
        }

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sqlRes)) {

            if (hasIdFlag) {
                preparedStatement.setInt(1, scientificForum.getId());
            } else {
                preparedStatement.setString(1, scientificForum.getName());
            }

            return preparedStatement.executeQuery().next();

        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);
            throw new DatasourceQueryFailedException("A datasource exception"
                    + "occurred", ex);
        }
    }

    /**
     * Adds a scientific forum to the repository.
     *
     * @param scientificForum A scientific forum dto filled with a name
     * @param transaction     The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws KeyExistsException             If the name of the added forum
     *                                        already exists in the datasource.
     * @throws InvalidFieldsException         If one of the required fields of the
     *                                        scientific forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static ScientificForum add(ScientificForum scientificForum,
                                      Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
        Connection conn = transaction.getConnection();
        String sql = "INSERT INTO scientific_forum(name, description, url, review_manual, timestamp_deadline) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, scientificForum.getName());
            ps.setString(2, scientificForum.getDescription());
            if (scientificForum.getUrl() == null || scientificForum.getUrl().isEmpty()) {
                ps.setString(3, null);
            } else {
                ps.setString(3, scientificForum.getUrl());
            }
            ps.setString(4, scientificForum.getReviewManual());
            if (scientificForum.getDeadline() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(scientificForum.getDeadline()));
            } else {
                ps.setTimestamp(5, null);
            }

            ps.executeUpdate();

            // Return new ID.
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ScientificForum sf = new ScientificForum();
                    sf.setId(generatedKeys.getInt(1));
                    return sf;
                } else {
                    throw new SQLException("Creating forum failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new KeyExistsException();
            } else if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotWrittenException();
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException();
            }
        }
    }

    /**
     * Changes the given scientific forum in the repository. All fields that
     * are not required will be deleted if left empty.
     *
     * @param scientificForum A scientificForum dto with an id and a name.
     * @param transaction     The transaction to use.
     * @throws NotFoundException       If there is no scientific forum with the
     *                                 provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException      If the new name of the changed forum
     *                                 already exists in the datasource.
     * @throws InvalidFieldsException  If one of the required fields of the
     *                                 scientific forum is null.
     */
    public static void change(ScientificForum scientificForum,
                              Transaction transaction)
            throws NotFoundException, DataNotWrittenException,
            KeyExistsException {
        if (scientificForum.getId() == null && scientificForum.getName() == null) {

            logger.severe("The name or id must not be changed to null.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)) {

            logger.severe("There is no forum with that id: " + scientificForum.getId());
            throw new NotFoundException();
        }

        String sql = """
                UPDATE scientific_forum
                SET name = ?, description = ?, url = ?, review_manual = ?, timestamp_deadline = ?
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, scientificForum.getName());
            preparedStatement.setString(2, scientificForum.getDescription());
            preparedStatement.setString(3, scientificForum.getUrl());
            preparedStatement.setString(4, scientificForum.getReviewManual());
            preparedStatement.setInt(6, scientificForum.getId());

            if (scientificForum.getDeadline() != null) {
                preparedStatement.setTimestamp(5, Timestamp.valueOf(scientificForum.getDeadline()));
            } else {
                preparedStatement.setTimestamp(5, null);
            }

            preparedStatement.executeUpdate();
            logger.finest("Successfully changed the forum: " + scientificForum.getId() + ".");
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The forum could not be changed", ex);
            } else if (ex.getSQLState().equals(TransientSQLExceptionChecker.UNIQUE_VIOLATION)) {
                throw new KeyExistsException("The name: " + scientificForum.getName() + " already exists", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }

    /**
     * Takes a scientific forum dto that is filled with a valid id and removes
     * this scientific forum from the repository.
     *
     * @param scientificForum The scientific forum to remove. Must be filled
     *                        with a valid id.
     * @param transaction     The transaction to use.
     * @throws NotFoundException       The specified scientific forum was not found in
     *                                 the repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails
     * @throws InvalidFieldsException  If no id is provided.
     */
    public static void remove(ScientificForum scientificForum,
                              Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (scientificForum.getId() == null) {

            logger.severe("Forum must contain an id to be removed.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)) {

            logger.severe("Forum must exist in order to be removed.");
            throw new InvalidFieldsException();
        }

        String sql = """
                DELETE
                FROM scientific_forum
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, scientificForum.getId());
            preparedStatement.executeUpdate();
            logger.finest("Successfully removed forum: " + scientificForum.getId());
        } catch (SQLException ex) {

            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The forum could not be removed", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }

    private static boolean isFilled(String s) {
        return s != null && !s.isEmpty();
    }

    /**
     * Gets a list all scientific forums.
     *
     * @param transaction          The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled scientific forum dtos.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static List<ScientificForum> getList(Transaction transaction,
                                                ResultListParameters
                                                        resultListParameters) {
        if (transaction == null) {
            logger.severe("Passed transaction is null.");
            throw new IllegalArgumentException("Transaction must not be null.");
        }
        if (resultListParameters == null) {
            logger.severe("Passed result-list parameters is null.");
            throw new IllegalArgumentException("ResultListParameters must not be null.");
        }

        Connection conn = transaction.getConnection();
        ResultSet resultSet;
        List<ScientificForum> scientificForums = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(getStatementForumList(resultListParameters, false))) {
            int i = 1;
            if (isFilled(resultListParameters.getFilterColumns().get("name"))) {
                ps.setString(i, "%" + resultListParameters.getFilterColumns().get("name") + "%");
                i++;

            }

            // Global Search Word
            ps.setString(i, "%"
                    + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");

            i++;
            ps.setString(i, "%"
                    + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");


            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                ScientificForum forum = createScientificForumFromResultSet(resultSet);
                scientificForums.add(forum);
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DatasourceQueryFailedException(e.getMessage(), e);
        }
        return scientificForums;
    }

    private static String getStatementForumList(ResultListParameters params, boolean doCount) {
        StringBuilder sb = new StringBuilder();

        if (doCount) {
            sb.append("SELECT COUNT (DISTINCT f.name) as count\n");
        } else {
            sb.append("SELECT *\n");
        }

        sb.append("""
                FROM scientific_forum f
                """).append("\n");
        sb.append("WHERE").append("\n");

        boolean hasPriorCond = false;
        if (isFilled(params.getFilterColumns().get("name"))) {
            sb.append("f.name ILIKE ?\n");
            hasPriorCond = true;
        }

        // Filter according to date select parameter
        if (params.getDateSelect() == DateSelect.FUTURE) {
            if (hasPriorCond) {
                sb.append(" AND");
            }
            sb.append(" (f.timestamp_deadline::date >= CURRENT_DATE)\n");
            hasPriorCond = true;
        } else if (params.getDateSelect() == DateSelect.PAST) {
            if (hasPriorCond) {
                sb.append(" AND");
            }
            sb.append(" (f.timestamp_deadline::date <= CURRENT_DATE)\n");
            hasPriorCond = true;
        }

        if (!hasPriorCond && "".equals(params.getGlobalSearchWord())) {
            StringBuilder newSb = new StringBuilder();
            newSb.append(sb.toString().replace(" WHERE", ""));
            sb = newSb;
        }

        // Filter according to global search word.
        if (!"".equals(params.getGlobalSearchWord())) {
            if (hasPriorCond) {
                sb.append(" AND");
            }
            sb.append(" (");
            sb.append(" f.name ILIKE ?\n");
            sb.append(" OR f.description ILIKE ?\n");
            sb.append(")\n");
        }

        // Sort according to sort column parameters
        if (!doCount) {
            if (isFilled(params.getSortColumn())) {
                if (params.getSortColumn().equals("name")) {
                    sb.append(" ORDER BY f.name");
                } else {
                    sb.append(" ORDER BY f.").append(params.getSortColumn());
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

        return sb.toString();
    }


    public static int getCountItemsList(Transaction transaction, ResultListParameters resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (transaction == null) {
            logger.severe("Passed transaction is null.");
            throw new IllegalArgumentException("Transaction can not be null.");
        }
        if (resultListParameters == null) {
            logger.severe("Passed result-list parameters is null.");
            throw new IllegalArgumentException("ResultListParameters must not be null.");
        }

        Connection connection = transaction.getConnection();
        ResultSet resultSet;

        try (PreparedStatement ps = connection.prepareStatement(getStatementForumList(resultListParameters, true))) {
            int i = 1;
            if (isFilled(resultListParameters.getFilterColumns().get("name"))) {
                ps.setString(i, "%" + resultListParameters.getFilterColumns().get("name") + "%");
                i++;

            }

            // Global Search Word
            ps.setString(i, "%"
                    + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");

            i++;
            ps.setString(i, "%"
                    + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("count");
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DatasourceQueryFailedException(e.getMessage(), e);
        }

        return -1;
    }

    /**
     * Adds the specified editor to the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param editor          A user dto with a valid id.
     * @param transaction     The transaction to use.
     * @throws NotFoundException       If there is no scientific forum with the
     *                                 provided id or there is no user with the
     *                                 provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException  If no editor and forum ids are provided.
     */
    public static void addEditor(ScientificForum scientificForum, User editor,
                                 Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (editor.getId() == null || scientificForum.getId() == null) {

            logger.severe("Must have a editor and forum id to update relationship.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)) {

            logger.severe("Forum (" + scientificForum.getId() + ") and user (" + editor.getId() + ") must exist"
                    + "in order to update their relationship.");
            throw new InvalidFieldsException();
        }

        String sql = """
                INSERT INTO member_of (editor_id, scientific_forum_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, editor.getId());
            preparedStatement.setInt(2, scientificForum.getId());
            preparedStatement.executeUpdate();
            logger.finest("Forum (" + scientificForum.getId() + ") and user (" + editor.getId() + ") are now"
                    + "in an editorial relationship.");
        } catch (SQLException ex) {

            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The editor could not be added", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }

    /**
     * Adds the specified science field to the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param scienceField    A science field dto with a valid id.
     * @param transaction     The transaction to use.
     * @throws NotFoundException       If there is no scientific forum with the
     *                                 provided id or there is no science field with
     *                                 the provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException  If no science field name and forum id are provided.
     */
    public static void addScienceField(ScientificForum scientificForum,
                                       ScienceField scienceField,
                                       Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (scientificForum.getId() == null || scienceField.getName() == null) {

            logger.severe("Must contain a sciencefield name and forum id to add as a topic.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)
                || !ScienceFieldRepository.isScienceField(scienceField, transaction)) {

            logger.severe("Forum (" + scientificForum.getId() + ") and sciencefield (" + scienceField.getName() + ") must exist"
                    + "in order to update their relationship.");
            throw new InvalidFieldsException();
        }

        String sql = """
                INSERT INTO topics (science_field_name, scientific_forum_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, scienceField.getName());
            preparedStatement.setInt(2, scientificForum.getId());
            preparedStatement.executeUpdate();
            logger.finest("Forum (" + scientificForum.getId() + ") and sciencefield (" + scienceField.getName() + ") "
                    + "were put in a relationship.");
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The field could not be added to the forum.", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }

    /**
     * Removes the specified editor from the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param editor          A user dto with a valid id, which is an editor in the
     *                        aforementioned forum.
     * @param transaction     The transaction to use.
     * @throws NotFoundException       If there is no scientific forum with the
     *                                 provided id or there is no user with the
     *                                 provided id or the provided user is not
     *                                 an editor for the provided forum.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException  If no editor id and forum id are provided.
     */
    public static void removeEditor(ScientificForum scientificForum,
                                    User editor, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (editor.getId() == null || scientificForum.getId() == null) {

            logger.severe("Must contain an editor id and forum id to remove an editor.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)) {

            logger.severe("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") must exist in order to be put in a relationship.");
            throw new InvalidFieldsException();
        }

        String sql = """
                DELETE FROM member_of
                WHERE editor_id = ?
                AND scientific_forum_id = ?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, editor.getId());
            preparedStatement.setInt(2, scientificForum.getId());
            preparedStatement.executeUpdate();
            logger.finest("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") relationship was dissolved.");
        } catch (SQLException ex) {

            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The editor could not be removed", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }

        ResultSet resultSet;
        User newEditor = new User();

        String sql2 = """
                SELECT editor_id
                FROM member_of
                WHERE scientific_forum_id = ?
                """;
        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql2)){
            preparedStatement.setInt(1, scientificForum.getId());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newEditor.setId(resultSet.getInt("editor_id"));
            } else {
                throw new NotFoundException("No new editor found");
            }
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The editor could not be set", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
        if (newEditor.getId() != null) {
            String update = """
                UPDATE submission
                SET editor_id = ?
                WHERE forum_id = ? AND editor_id = ?
                """;

            try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(update)){
                preparedStatement.setInt(1, newEditor.getId());
                preparedStatement.setInt(2, scientificForum.getId());
                preparedStatement.setInt(3, editor.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                DatasourceUtil.logSQLException(ex, logger);

                if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                    throw new DataNotWrittenException("The new editor could not be set", ex);
                } else {
                    transaction.abort();
                    throw new DatasourceQueryFailedException("A datasource exception"
                            + "occurred", ex);
                }
            }
        }
    }

    /**
     * Removes the specified science field from the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param scienceField    A science field dto with a valid id that belongs to
     *                        the aforementioned forum.
     * @param transaction     The transaction to use.
     * @throws NotFoundException       If there is no scientific forum with the
     *                                 provided id or there is no science field with
     *                                 the provided id or the provided science field
     *                                 is not part of the provided field.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException  If no editor id and forum id are provided.
     */
    public static void removeScienceField(ScientificForum scientificForum,
                                          ScienceField scienceField,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (scientificForum.getId() == null || scienceField.getName() == null) {

            logger.severe("Must contain a forum and editor id in order to remove an editor.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)
                || !ScienceFieldRepository.isScienceField(scienceField, transaction)) {

            logger.severe("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") must exist in order to be cease their relationship.");
            throw new InvalidFieldsException();
        }

        String sql = """
                DELETE FROM topics
                WHERE scientific_forum_id = ?
                AND science_field_name = ?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, scientificForum.getId());
            preparedStatement.setString(2, scienceField.getName());
            preparedStatement.executeUpdate();
            logger.finest("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") relationship was dissolved.");
        } catch (SQLException ex) {
            DatasourceUtil.logSQLException(ex, logger);

            if (TransientSQLExceptionChecker.isTransient(ex.getSQLState())) {
                throw new DataNotWrittenException("The field could not be removed from the forum", ex);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("A datasource exception"
                        + "occurred", ex);
            }
        }
    }
}
