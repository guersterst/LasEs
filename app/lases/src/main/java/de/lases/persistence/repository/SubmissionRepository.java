package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import javax.xml.crypto.Data;
import java.security.Key;
import java.util.List;

/**
 * Offers get/add/change/remove operations on a submission and the
 * possibility to get lists of submissions.
 */
public class SubmissionRepository {

    /**
     * Takes a scientific forum dto that is filled with a valid id and returns
     * a fully filled submission dto.
     *
     * @param submission A {@code Submission} dto that must be filled
     *                   with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled {@code Submission} dto.
     * @throws NotFoundException If there is no submission with the
     *                           provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Submission get(Submission submission, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Adds a submission to the repository.
     *
     * @param submission A fully filled submission dto.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If there is already a submission with
     *                            the same id.
     * @throws InvalidFieldsException If one of the fields of the submission is
     *                                null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(Submission submission, Transaction transaction)
            throws DataNotWrittenException, KeyExistsException {
    }

    /**
     * Changes the given submission in the repository.
     *
     * @param submission A fully filled submission dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no submission with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the submission
     *                                is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Takes a submission dto that is filled with a valid id and removes
     * this submission from the repository.
     *
     * @param submission The submission to remove. Must be filled
     *                   with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified submission was not found in
     *                           the repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Submission submission, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all submissions that belong to the specified user under the
     * specified role (for example as editor, submitter, reviewer) from the
     * specified scientific forum.
     *
     * @param user A {@code User} dto with a valid id.
     * @param privilege As which role should the user act.
     * @param scientificForum A {@code ScientificForum} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     *         belong to the specified user in the specified role and the
     *         specified scientific forum.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no user with the provided id or no
     *                           scientific forum with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException{
        return null;
    }

    /**
     * Gets a list all submissions that belong to the specified user under the
     * specified role (for example as editor, submitter, reviewer).
     *
     * @param user A {@code User} dto with a valid id.
     * @param privilege As which role should the user act.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     *         belong to the specified user in the specified rol.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Submission> getList(User user, Privilege privilege,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Gets a list all submissions that belong to the specified scientific
     * forum.
     *
     * @param scientificForum A {@code ScientificForum} dto with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled submission dtos for all reviews that
     *         belong to the specified scientific forum.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Submission> getList(ScientificForum scientificForum,
                                           Transaction transaction,
                                           ResultListParameters
                                                   resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Adds the specified user to the specified submission as a co-author.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addCoAuthor(Submission submission, User user,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Adds the specified user to the specified submission as a reviewer.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void addReviewer(Submission submission, User user,
                                   Transaction transaction)
            throws NotFoundException, DataNotWrittenException{
    }

    /**
     * Removes the specified co-author from the specified scientific forum.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id, which is a co-author in the
     *             aforementioned submission.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id or the provided user is not
     *                           a co-author for the provided submission.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeCoAuthor(Submission submission, User user,
                                      Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Removes the specified reviewer from the specified scientific forum.
     *
     * @param submission A scientific forum dto with a valid id.
     * @param user A user dto with a valid id, which is a reviewer in the
     *             aforementioned submission.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no scientific forum with the
     *                           provided id or there is no user with the
     *                           provided id or the provided user is not
     *                           a reviewer for the provided submission.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void removeReviewer(Submission submission, User user,
                                      Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Count the number of submissions where the specified user is author.
     *
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return The number of submission the specified user authored.
     * @throws NotFoundException If there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static int countSubmissions(User user, Transaction transaction)
            throws NotFoundException {
        return 0;
    }

}
