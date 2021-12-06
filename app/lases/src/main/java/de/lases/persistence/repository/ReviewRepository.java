package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.util.List;

/**
 * Offers get/add/change/remove operations on a review and the possibility to
 * get lists of reviews.
 */
public class ReviewRepository {

    /**
     * Takes a review dto that is filled out with a valid reviewerId, paperId
     * and submissionId and returns a fully filled review dto.
     *
     * @param review A review dto that must be filled with a valid reviewerId,
     *               paperId and submissionId
     * @param transaction The transaction to use.
     * @return A fully filled review dto.
     * @throws NotFoundException If there is no review with the provided ids.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static Review get(Review review, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Adds a review to the repository.
     *
     * @param review A fully filled review dto. (The id must not be specified,
     *               as the repository will create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the review is
     *                                null.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     * @throws NotFoundException If there is no submission with the given
     *                           submissionId or the submission has no
     *                           reviewer with the given reviewerId.
     */
    public static void add(Review review, Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

    /**
     * Changes the given review in the repository.
     *
     * @param review A fully filled review dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no review with the provided
     *                           reviewerId, paperId and submissionId.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the review is
     *                                null.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Review review, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Takes a review dto that is filled with a valid reviewerId, paperId and
     * submissionId and removes this review from the repository.
     *
     * @param review The review to remove. Must be filled with a valid
     *               reviewerId, paperId and submissionId.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified review was not found in the
     *                           repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Review review, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all reviews that belong to the specified submission that the
     * specified user can see. (An editor can see everything, the submitter
     * can view see all visible reviews, a reviewer his own reviews.)
     *
     * @param submission A submission dto filled with a valid id.
     * @param user A user dto filled with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled review dtos for all reviews that belong
     *         to the specified submission and the specified user can see.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no submission with the provided id
     *                           or there is no user with the provided id.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Review> getList(Submission submission, User user,
                               Transaction transaction,
                               ResultListParameters resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Get the PDF file for the provided review.
     *
     * @param review A review dto filled with a valid reviewerId, paperId and
     *              submissionId.
     * @param transaction The transaction to use.
     * @return A file containing the PDF for the specified review.
     * @throws NotFoundException If there is no review with the provided ids.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getPDF(Paper review, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Sets the PDF belonging to a specified review.
     *
     * @param review A review dto filled with a valid reviewerId, paperId and
     *               submissionId.
     * @param pdf A file dto filled with a pdf file.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws NotFoundException If there is no review with the provided ids.
     * @throws DepletedResourceException If the datasource cannot be
     *                                        queried.
     */
    public static void setPDF(Review review, FileDTO pdf, Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

}
