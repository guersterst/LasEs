package de.lases.persistence.repository;

import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.ScienceField;
import de.lases.global.transport.ScientificForum;
import de.lases.global.transport.User;
import de.lases.persistence.exception.*;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a scientific forum and the
 * possibility to get lists of scientific forums.
 *
 * @author Thomas Kirz
 */
public class ScientificForumRepository {

    private static final Logger l = Logger.getLogger(ScientificForumRepository.class.getName());

    /**
     * Takes a scientific forum dto that is filled with a valid id or a valid
     * name and returns a fully filled scientific forum dto.
     *
     * @param scientificForum A {@code ScientificForum} dto that must be filled
     *                        with a valid id or name.
     * @param transaction The transaction to use.
     * @return A fully filled {@code ScientificForum} dto.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or name.
     * @throws InvalidFieldsException If both name and id are provided, but they
     *                                belong to two different scientific forums.
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
     * @param transaction The transaction to use.
     * @return Does this scientific forum exist?
     * @throws InvalidFieldsException If both name and id are provided, but they
     *                                belong to two different scientific forums.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static boolean exists(ScientificForum scientificForum,
                                 Transaction transaction) {
        return false;
    }

    /**
     * Adds a scientific forum to the repository.
     *
     * @param scientificForum A scientific forum dto filled with a name
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If the name of the added forum
     *                            already exists in the datasource.
     * @throws InvalidFieldsException If one of the required fields of the
     *                                scientific forum is null.
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
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If the new name of the changed forum
     *                            already exists in the datasource.
     * @throws InvalidFieldsException If one of the required fields of the
     *                                scientific forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(ScientificForum scientificForum,
                              Transaction transaction)
            throws NotFoundException, DataNotWrittenException,
            KeyExistsException {
    }

    /**
     * Takes a scientific forum dto that is filled with a valid id and removes
     * this scientific forum from the repository.
     *
     * @param scientificForum The scientific forum to remove. Must be filled
     *                        with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified scientific forum was not found in
     *                           the repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(ScientificForum scientificForum,
                              Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all scientific forums.
     *
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled scientific forum dtos.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScientificForum> getList(Transaction transaction,
                                                ResultListParameters
                                                        resultListParameters)
            throws DataNotCompleteException {
        return null;
    }

    /**
     * Adds the specified editor to the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param editor A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addEditor(ScientificForum scientificForum, User editor,
                                 Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Adds the specified science field to the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param scienceField A science field dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no science field with
     *                           the provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addScienceField(ScientificForum scientificForum,
                                       ScienceField scienceField,
                                       Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Removes the specified editor from the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param editor A user dto with a valid id, which is an editor in the
     *               aforementioned forum.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id or the provided user is not
     *                           an editor for the provided forum.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeEditor(ScientificForum scientificForum,
                                    User editor, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Removes the specified science field from the specified scientific forum.
     *
     * @param scientificForum A scientific forum dto with a valid id.
     * @param scienceField A science field dto with a valid id that belongs to
     *                     the aforementioned forum.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no science field with
     *                           the provided id or the provided science field
     *                           is not part of the provided field.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeScienceField(ScientificForum scientificForum,
                                          ScienceField scienceField,
                                          Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

}
