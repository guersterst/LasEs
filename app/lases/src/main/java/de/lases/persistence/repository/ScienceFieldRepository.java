package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Offers get/add/remove operations on a science field and the
 * possibility to get lists of science fields.
 */
public class ScienceFieldRepository {

    private static final Logger logger = Logger.getLogger(ScienceField.class.getName());

    /**
     * Adds a science field to the repository.
     *
     * @param scienceField A fully filled science field dto. (The id must not be
     *                     specified, as the repository will create the id)
     * @param transaction The transaction to use.
     * @throws KeyExistsException If the provided science field already exists.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the science field
     *                                is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(ScienceField scienceField, Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
        Connection conn = transaction.getConnection();

        if (scienceField.getName() == null) {
            throw new InvalidFieldsException("The name of the science field must not be null");
        }

        String query = """
                INSERT INTO science_field
                VALUES (?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, scienceField.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            // duplicate key value
            if (e.getSQLState().equals("23505")) {
                throw new KeyExistsException("This science field already exists.", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Science field could not be added", e);
            }
        }

    }

    /**
     * Takes a science field dto that is filled with a valid id and removes
     * this science field from the repository.
     *
     * @param scienceField The science field to remove. Must be filled with a
     *                     valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified science field was not found in
     *                           the repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(ScienceField scienceField, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all science fields that belong to the specified scientific
     * forum.
     *
     * @param forum A {@code ScientificForum} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled science field dtos for all reviews that
     *         belong to the specified submission.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no scientific forum with the provided
     *                           id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScienceField> getList(ScientificForum forum,
                                             Transaction transaction,
                                             ResultListParameters
                                                     resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Gets a list all science fields that belong to the specified user.
     *
     * @param user A {@code User} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled science field dtos for all reviews that
     *         belong to the specified user.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScienceField> getList(User user, Transaction transaction,
                                             ResultListParameters
                                                     resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Gets a list all science fields.
     *
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled science field dtos.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScienceField> getList(Transaction transaction,
                                             ResultListParameters
                                                     resultListParameters)
            throws DataNotCompleteException {
        return null;
    }

    /**
     * Checks if a science field with the given name exists in the repository.
     *
     * @param scienceField The {@link ScienceField} to check.
     *                    Must contain either an id or a name.
     * @param transaction The transaction to use.
     * @return Does the given name exist as science field in the repository.
     */
    public static boolean isScienceField(ScienceField scienceField, Transaction transaction) {
        return false;
    }

}
