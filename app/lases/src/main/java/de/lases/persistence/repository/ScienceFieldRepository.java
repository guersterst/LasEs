package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;
import de.lases.persistence.util.TransientSQLExceptionChecker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
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
     * Takes a science field dto that is filled with a valid name and removes
     * this science field from the repository.
     *
     * @param scienceField The science field to remove. Must be filled with a
     *                     valid name.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(ScienceField scienceField, Transaction transaction)
            throws DataNotWrittenException {
        Connection conn = transaction.getConnection();

        if (scienceField.getName() == null) {
            throw new InvalidFieldsException("The name of the science field must not be null");
        }

        String query = """
                DELETE FROM science_field
                WHERE name = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, scienceField.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("Science field could not be removed", e);
        }

    }

    /**
     * Gets a list all science fields that belong to the specified scientific
     * forum. Note: To filter the list of science fields, please use the global search word. The filterColumns map will
     * be ignored, since the science field table contains only one column.
     *
     * @param forum A {@code ScientificForum} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements. To filter the list of science fields,
     *                             please use the global search word. The filterColumns map will be ignored, since
     *                             the science field table contains only one column.
     * @return A list of fully filled science field dtos for all reviews that
     *         belong to the specified submission.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScienceField> getList(ScientificForum forum, Transaction transaction,
                                             ResultListParameters resultListParameters)
            throws DataNotCompleteException {
        Connection conn = transaction.getConnection();

        if (forum.getId() == null) {
            throw new InvalidFieldsException("The id of the forum must not be null");
        }

        String query = """
                SELECT science_field_name FROM topics
                WHERE scientific_forum_id = ?
                AND science_field_name ILIKE ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, forum.getId());
            stmt.setString(2,
                    "%" + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");
            ResultSet set = stmt.executeQuery();

            List<ScienceField> scienceFields = new ArrayList<>();
            while (set.next()) {
                ScienceField scienceField = new ScienceField();
                scienceField.setName(set.getString(1));
                scienceFields.add(scienceField);
            }

            set.close();
            return scienceFields;
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("Science fields could not be received", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Science fields could not be received", e);
            }
        }
    }

    /**
     * Gets a list all science fields that belong to the specified user. Note: To filter the list of science fields,
     * please use the global search word. The filterColumns map will
     * be ignored, since the science field table contains only one column.
     *
     * @param user A {@code User} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements. To filter the list of science fields,
     *                             please use the global search word. The filterColumns map will be ignored, since
     *                             the science field table contains only one column.
     * @return A list of fully filled science field dtos for all reviews that
     *         belong to the specified user.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScienceField> getList(User user, Transaction transaction,
                                             ResultListParameters
                                                     resultListParameters)
            throws DataNotCompleteException {
        Connection conn = transaction.getConnection();

        if (user.getId() == null) {
            throw new InvalidFieldsException("The id of the forum must not be null");
        }

        String query = """
                SELECT science_field_name FROM interests
                WHERE user_id = ?
                AND science_field_name ILIKE ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2,
                    "%" + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");
            ResultSet set = stmt.executeQuery();

            List<ScienceField> scienceFields = new ArrayList<>();
            while (set.next()) {
                ScienceField scienceField = new ScienceField();
                scienceField.setName(set.getString(1));
                scienceFields.add(scienceField);
            }

            set.close();
            return scienceFields;
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("Science fields could not be received", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Science fields could not be received", e);
            }
        }
    }

    /**
     * Gets a list all science fields. Note: To filter the list of science fields,
     * please use the global search word. The filterColumns map will
     * be ignored, since the science field table contains only one column.
     *
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements. To filter the list of science fields,
     *                             please use the global search word. The filterColumns map will be ignored, since
     *                             the science field table contains only one column.
     * @return A list of fully filled science field dtos.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<ScienceField> getList(Transaction transaction, ResultListParameters resultListParameters)
            throws DataNotCompleteException {
        Connection conn = transaction.getConnection();

        String query = """
                SELECT name FROM science_field
                WHERE name ILIKE ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,
                    "%" + Objects.requireNonNullElse(resultListParameters.getGlobalSearchWord(), "") + "%");
            ResultSet set = stmt.executeQuery();

            List<ScienceField> scienceFields = new ArrayList<>();
            while (set.next()) {
                ScienceField scienceField = new ScienceField();
                scienceField.setName(set.getString(1));
                scienceFields.add(scienceField);
            }

            set.close();
            return scienceFields;
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);

            if (TransientSQLExceptionChecker.isTransient(e.getSQLState())) {
                throw new DataNotCompleteException("Science fields could not be received", e);
            } else {
                transaction.abort();
                throw new DatasourceQueryFailedException("Science fields could not be received", e);
            }
        }
    }

    /**
     * Checks if a science field with the given name exists in the repository.
     *
     * @param scienceField The {@link ScienceField} to check.
     *                    Must contain either an id or a name.
     * @param transaction The transaction to use.
     * @throws InvalidFieldsException If the science field does not have a name.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @return Does the given name exist as science field in the repository.
     */
    public static boolean isScienceField(ScienceField scienceField, Transaction transaction) {
        Connection conn = transaction.getConnection();

        if (scienceField.getName() == null) {
            throw new InvalidFieldsException("The science field name was null.");
        }

        String query = """
                SELECT * FROM science_field
                WHERE name = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, scienceField.getName());
            ResultSet resultSet = stmt.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            DatasourceUtil.logSQLException(e, logger);
            transaction.abort();
            throw new DatasourceQueryFailedException("Science field could not be added", e);
        }
    }

}
