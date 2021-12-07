package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

/**
 * Offers get/change operations on the relationship between reviewer and
 * submission.
 */
public class ReviewedByRepository {

    /**
     * Returns the ReviewedBy dto for the given submission and user.
     *
     * @param submission A submission dto with a valid id.
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled {@code ReviewedBy} dto for the specified
     *         submission and user. Returns null if there is no such
     *         relationship.
     * @throws NotFoundException If there is no submission with the provided id,
     *                           there is no user with the provided id or there
     *                           is no
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static ReviewedBy get(Submission submission, User user,
                                 Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Changes the given {@code ReviewedBy} in the repository.
     *
     * @param reviewedBy A fully filled {@code ReviewedBy} dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no reviewedBy relationship with
     *                           the given user id and submission id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the
     *                                provided {@code ReviewedBy} dto is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(ReviewedBy reviewedBy, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

}
