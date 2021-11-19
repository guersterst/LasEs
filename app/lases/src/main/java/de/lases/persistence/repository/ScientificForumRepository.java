package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import javax.xml.crypto.Data;
import java.security.Key;
import java.util.List;

/**
 * Offers get/add/change/remove operations on a scientific forum and the
 * possibility to get lists of scientific forums.
 */
public class ScientificForumRepository {

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
        return null;
    }

    /**
     * Adds a scientific forum to the repository.
     *
     * @param scientificForum A fully filled scientific forum dto.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If there is already a scientific forum with
     *                            the same id.
     * @throws InvalidFieldsException If one of the fields of the scientific
     *                                forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(ScientificForum scientificForum,
                           Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
    }

    /**
     * Changes the given scientific forum in the repository.
     *
     * @param scientificForum A fully filled scientificForum dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the scientific
     *                                forum is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(ScientificForum scientificForum,
                              Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
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

    /**
     * Returns the scientific forum that belongs to a specific submission.
     *
     * @param submission The submission to which the forum should be returned.
     * @return The scientific forum for the given submission.
     */
    public ScientificForum getScientificForumForSubmission(
            Submission submission) {
        return null;
    }


}
