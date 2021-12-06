package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

/**
 * Offers get/add/remove operations on a science field and the
 * possibility to get lists of science fields.
 */
public class ScienceFieldRepository {

    /**
     * Takes a science field dto that is filled out with a valid id or a valid
     * name and returns a fully filled science field dto.
     *
     * @param scienceField A ScienceField dto that must be filled with a valid
     *                     id.
     * @param transaction The transaction to use.
     * @return A fully filled ScienceField dto.
     * @throws NotFoundException If there is no science field with the provided
     *                           id or name.
     * @throws InvalidFieldsException If both name and id are provided, but they
     *                                belong to two different science fields.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static ScienceField get(ScienceField scienceField,
                                   Transaction transaction)
            throws NotFoundException{
        return null;
    }

    /**
     * Adds a science field to the repository.
     *
     * @param scienceField A fully filled science field dto. (The id must not be
     *                     specified, as the repository will create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the science field
     *                                is null.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static void add(ScienceField scienceField, Transaction transaction)
            throws DataNotWrittenException {
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
     * @throws DepletedResourceException If the datasource cannot be
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
     * @throws DepletedResourceException If the datasource cannot be
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
     * @throws DepletedResourceException If the datasource cannot be
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
     * @throws DepletedResourceException If the datasource cannot be
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
