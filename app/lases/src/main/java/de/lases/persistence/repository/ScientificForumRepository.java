package de.lases.persistence.repository;

import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.ScienceField;
import de.lases.global.transport.ScientificForum;
import de.lases.global.transport.User;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static final Logger l = Logger.getLogger(ScientificForumRepository.class.getName());

    /**
     * Takes a scientific forum dto that is filled with a valid id or a valid
     * name and returns a fully filled scientific forum dto.
     *
     * @param scientificForum A {@code ScientificForum} dto that must be filled
     *                        with a valid id or name.
     * @param transaction     The transaction to use.
     * @return A fully filled {@code ScientificForum} dto.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or name.
     * @throws InvalidFieldsException         If both name and id are provided, but they
     *                                        belong to two different scientific forums.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static ScientificForum get(ScientificForum scientificForum,
                                      Transaction transaction)
            throws NotFoundException {
        if (scientificForum.getId() == null) {
            l.severe("The passed ScientificForum-DTO does not contain an id.");
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
                l.finer("Retrieved scientific forum with id " + scientificForum.getId());
            } else {
                l.warning("No scientific forum with id " + scientificForum.getId() + " found in database.");
                throw new NotFoundException("No scientific forum with id " + scientificForum.getId());
            }
        } catch (SQLException e) {
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
            l.severe("To query the scientific forum's existence an id or a name must exist.");
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

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException(e.getMessage(), e);
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
    public static void add(ScientificForum scientificForum,
                           Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
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

            l.severe("The name or id must not be changed to null.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)) {

            l.severe("There is no forum with that id: " + scientificForum.getId());
            throw new NotFoundException();
        }

        /*
        // Determine whether a name change would be allowed -> Alternative to exception schtuff
        ScientificForum forumWithOldName = get(scientificForum, transaction);
        boolean isNameChange = forumWithOldName.getName().equals(scientificForum.getName());
        ScientificForum queryForumWithOnlyName = new ScientificForum();
        queryForumWithOnlyName.setName(scientificForum.getName());
        if (isNameChange && exists(queryForumWithOnlyName, transaction)) {
            throw new KeyExistsException();
        }
         */


        String sql = """
                UPDATE scientific_forum
                SET name = ?, description = ?, url = ?, review_manual = ?, timestamp_deadline = ?
                WHERE id = ?
                """;

        ScientificForum result;
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
            l.finest("Successfully changed the forum: " + scientificForum.getId() + ".");
        } catch (SQLException exception) {

            l.severe("The data could not be changed for forum: " + scientificForum.getId());
            throw new DataNotWrittenException(exception.getMessage(), exception);
        }
    }

    /**
     * Takes a scientific forum dto that is filled with a valid id and removes
     * this scientific forum from the repository.
     *
     * @param scientificForum The scientific forum to remove. Must be filled
     *                        with a valid id.
     * @param transaction     The transaction to use.
     * @throws NotFoundException              The specified scientific forum was not found in
     *                                        the repository.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails
     * @throws InvalidFieldsException         If no id is provided.
     */
    public static void remove(ScientificForum scientificForum,
                              Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (scientificForum.getId() == null) {

            l.severe("Forum must contain an id to be removed.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction)) {

            l.severe("Forum should exist in order to be removed.");
            throw new NotFoundException();
        }

        String sql = """
                DELETE
                FROM scientific_forum
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, scientificForum.getId());
            preparedStatement.executeUpdate();
            l.finest("Successfully removed forum: " + scientificForum.getId());
        } catch (SQLException ex) {

            l.severe("Could not remove forum: " + scientificForum.getId());
            throw new DataNotWrittenException(ex.getMessage(), ex);
        }
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
        Connection conn = transaction.getConnection();

        String query = """
                SELECT name FROM scientific_forum
                WHERE name ILIKE ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,
                    "%" + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");
            ResultSet resultSet = stmt.executeQuery();

            List<ScientificForum> scientificForums = new ArrayList<>();
            while (resultSet.next()) {
                ScientificForum scientificForum = createScientificForumFromResultSet(resultSet);
                scientificForums.add(scientificForum);
            }

            resultSet.close();
            return scientificForums;
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, l);
            transaction.abort();
            throw new DatasourceQueryFailedException("Science field could not be added", e);
        }
    }

    /**
     * Adds the specified editor to the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param editor          A user dto with a valid id.
     * @param transaction     The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no user with the
     *                                        provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If no editor and forum ids are provided.
     */
    public static void addEditor(ScientificForum scientificForum, User editor,
                                 Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (editor.getId() == null || scientificForum.getId() == null) {

            l.severe("Must have a editor and forum id to update relationship.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction) || !userExists(editor, transaction)) {

            l.severe("Forum (" + scientificForum.getId() + ") and user (" + editor.getId() + ") must exist"
                    + "in order to update their relationship.");
            throw new NotFoundException();
        }

        String sql = """
                INSERT INTO member_of (editor_id, scientific_forum_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, editor.getId());
            preparedStatement.setInt(2, scientificForum.getId());
            preparedStatement.executeUpdate();
            l.finest("Forum (" + scientificForum.getId() + ") and user (" + editor.getId() + ") are now"
                    + "in an editorial relationship.");
        } catch (SQLException ex) {

            l.severe("Forum (" + scientificForum.getId() + ") and user (" + editor.getId() + ") could not"
                    + "be updated into an editorial relationship.");
            throw new DataNotWrittenException(ex.getMessage(), ex);
        }
    }

    /**
     * Adds the specified science field to the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param scienceField    A science field dto with a valid id.
     * @param transaction     The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no science field with
     *                                        the provided id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If no science field name and forum id are provided.
     */
    public static void addScienceField(ScientificForum scientificForum,
                                       ScienceField scienceField,
                                       Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (scientificForum.getId() == null || scienceField.getName() == null) {

            l.severe("Must contain a sciencefield name and forum id to add as a topic.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction) ||  !scienceFieldExists(scienceField, transaction)) {

            l.severe("Forum (" + scientificForum.getId() + ") and sciencefield (" + scienceField.getName() + ") must exist"
                    + "in order to update their relationship.");
            throw new NotFoundException();
        }

        String sql = """
                INSERT INTO topics (science_field_name, scientific_forum_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, scienceField.getName());
            preparedStatement.setInt(2, scientificForum.getId());
            preparedStatement.executeUpdate();
            l.finest("Forum (" + scientificForum.getId() + ") and sciencefield (" + scienceField.getName() + ") "
                    + "were put in a relationship.");
        } catch (SQLException ex) {

            l.severe("Forum (" + scientificForum.getId() + ") and sciencefield (" + scienceField.getName() + ") "
                    + "could not be put in a relationship.");
            throw new DataNotWrittenException(ex.getMessage(), ex);
        }
    }

    /**
     * Removes the specified editor from the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param editor          A user dto with a valid id, which is an editor in the
     *                        aforementioned forum.
     * @param transaction     The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no user with the
     *                                        provided id or the provided user is not
     *                                        an editor for the provided forum.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If no editor id and forum id are provided.
     */
    public static void removeEditor(ScientificForum scientificForum,
                                    User editor, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (editor.getId() == null || scientificForum.getId() == null) {

            l.severe("Must contain an editor id and forum id to remove an editor.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction) || !userExists(editor, transaction)) {

            l.severe("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") must exist in order to be put in a relationship.");
            throw new NotFoundException();
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
            l.finest("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") relationship was dissolved.");
        } catch (SQLException ex) {

            l.severe("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") relationship could not be dissolved.");
            throw new DataNotWrittenException(ex.getMessage(), ex);
        }
    }

    /**
     * Removes the specified science field from the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param scienceField    A science field dto with a valid id that belongs to
     *                        the aforementioned forum.
     * @param transaction     The transaction to use.
     * @throws NotFoundException              If there is no scientific forum with the
     *                                        provided id or there is no science field with
     *                                        the provided id or the provided science field
     *                                        is not part of the provided field.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If no editor id and forum id are provided.
     */
    public static void removeScienceField(ScientificForum scientificForum,
                                          ScienceField scienceField,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
        if (scientificForum.getId() == null || scienceField.getName() == null) {

            l.severe("Must contain a forum and editor id in order to remove an editor.");
            throw new InvalidFieldsException();
        } else if (!exists(scientificForum, transaction) ||  !scienceFieldExists(scienceField, transaction)) {

            l.severe("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") must exist in order to be cease their relationship.");
            throw new NotFoundException();
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
            l.finest("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") relationship was dissolved.");
        } catch (SQLException ex) {

            l.severe("Forum (" + scientificForum.getId() + ") and editor ("
                    + scientificForum.getId() + ") relationship could not be dissolved.");
            throw new DataNotWrittenException(ex.getMessage(), ex);
        }
    }

    // TODO move to UserRepo
    private static boolean userExists(User user, Transaction transaction) {
        if (user.getId() == null ) {
            throw new InvalidFieldsException();
        }

        String sql = """
                SELECT id 
                FROM "user" 
                WHERE id=?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException(e.getMessage());
        }
    }

    // TODO move to ScienceFieldRepo
    private static boolean scienceFieldExists(ScienceField scienceField, Transaction transaction) {
        if (scienceField.getName() == null ) {
            throw new InvalidFieldsException();
        }

        String sql = """
                SELECT name 
                FROM science_field 
                WHERE name=?
                """;

        try (PreparedStatement preparedStatement = transaction.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, scienceField.getName());
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new DatasourceQueryFailedException(e.getMessage(), e);
        }
    }
}
