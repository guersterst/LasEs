package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides functionality regarding the management and handling of submissions.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class SubmissionService implements Serializable {

    @Serial
    private static final long serialVersionUID = 3347910586400642643L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

    private static final Logger logger = Logger.getLogger(PaperService.class.getName());

    /**
     * Gets a submission.
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @return The submission's data.
     */
    public Submission get(Submission submission) {
        return null;
    }

    /**
     * Creates a new submission.
     * <p>
     * The selected editor, reviewers and co-authors are informed about this
     * using the {@link de.lases.business.util.EmailUtil} utility.
     * Do not forget to also add a paper using {@link PaperService#add(FileDTO, Paper)}.
     * </p>
     *
     * @param submission The submission's data in a {@link Submission}.
     *                   Must contain a valid forum's id, authorId, editorId, state and title.
     * @param reviewers  The desired reviewers as proper {@code User}-DTOs with id's or exclusively containing
     *                   an email-address.
     * @param coAuthors  The desired co-athors as proper {@link User}-DTOs with id's or exclusively containing
     *                   an email-address.
     */
    public void add(Submission submission, List<User> reviewers,
                    List<User> coAuthors) {

    }

    /**
     * Deletes a submission.
     * <p>
     * Assigned editors will be informed about this using the
     * {@link de.lases.business.util.EmailUtil}-utility.
     * </p>
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     */
    public void remove(Submission submission) {
        //TODO: EMail to inform editor

        if (submission.getId() == null) {
            logger.severe("The id of the submission is not valid . Submission can't be deleted");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));
        } else {
            Transaction transaction = new Transaction();

            try {
                SubmissionRepository.remove(submission, transaction);
                transaction.commit();
            } catch (DataNotWrittenException e) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.WARNING));
                logger.log(Level.WARNING, e.getMessage());

                transaction.abort();

            } catch (NotFoundException e) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
                logger.fine("Error while removing a submssion with the id: " + submission.getId());

                transaction.abort();

            }
        }

    }


    /**
     * Manipulates a submission.
     *
     * Some manipulations will cause an email to be dispatched
     * using the {@link de.lases.business.util.EmailUtil}-utility.
     * <ul>
     * <li> The submitter and all co-authors are informed
     * about an accept or reject decision </li>
     * <li> The submitter will be informed if a revision is demanded.</li>
     * <li> When changed, the new editor will be informed.</li>
     * <li> The submitter about a required revision. </li>
     * </ul>
     *
     * @param newSubmission A {@link Submission}-DTO. The required fields can't be changed.
     *                      Only following can be changed:
     *                      <ul>
     *                      <li>
     *                          Submission state
     *                      </li>
     *                      <li>
     *                          Require a revision
     *                      </li>
     *                      <li>
     *                          Deadline of a revision.
     *                      </li>
     *                      </ul>
     */
    public void change(Submission newSubmission) {
        // TODO: Send a Email in some cases.

        if (newSubmission.getId() == null) {

            logger.severe("The id of the submission is not valid. Therefore no submission can be changed. ");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));

        } else {
            Transaction transaction = new Transaction();

            try {
                SubmissionRepository.change(newSubmission, transaction);
                transaction.commit();
            } catch (DataNotWrittenException e) {

                logger.log(Level.WARNING, e.getMessage());
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"),MessageCategory.ERROR));

                transaction.abort();

            } catch (NotFoundException e) {

                logger.fine("Error while changing a submission with the submission id: " +newSubmission.getId());
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("submissionNotFound"), MessageCategory.ERROR));

                transaction.abort();
            }
        }

    }

    /**
     * Adds a reviewer to a submission.
     * <p>
     * If successful the reviewer is informed by email using the
     * {@code EmailUtil} utility.
     * </p>
     *
     * @param submission The submission, with a valid id, that receives a new reviewer.
     * @param reviewer   The reviewer to be added to the submission.
     * @param reviewedBy Information about the review-request relationship.
     */
    public void addReviewer(Submission submission, User reviewer, ReviewedBy reviewedBy) {
    }


    /**
     * Gets the {@code ReviewedBy}-DTO for a submission and a reviewer.
     * Therefore, gets whether a reviewer has accepted to review a submission
     * and the reviews' deadline.
     *
     * @param submission The submission which is requested to be reviewed by a reviewer.
     * @param reviewer   The requested reviewer for a submission.
     * @return The {@link ReviewedBy}-DTO containing information about the review-request relationship of a
     * submission and the reviewer. Returns null if no such relationship exists.
     */
    public ReviewedBy getReviewedBy(Submission submission, User reviewer) {
        return null;
    }

    /**
     * Updates the given {@code ReviewedBy} in the repository.
     *
     * @param reviewedBy The fully filled {@link ReviewedBy}.
     */
    public void changeReviewedBy(ReviewedBy reviewedBy) {
    }

    /**
     * Removes a reviewer.
     * <p>
     * If successful the reviewer is informed by email using the
     * {@code EmailUtil} utility.
     * </p>
     *
     * @param submission The submission, that loses a reviewer.
     * @param reviewer   The reviewer to be removed from the submission.
     */
    public void removeReviewer(Submission submission, User reviewer) {
    }

    /**
     * Releases a review to be viewed by the submitter.
     *
     * @param review     The review to be released.
     * @param submission The submission containing that review.
     */
    public void releaseReview(Review review, Submission submission) {
    }

    /**
     * Adds a co-author.
     *
     * @param coAuthor   The co-author to be added. This can be a regular {@link User}-DTO
     *                   with a valid id
     *                   or exclusively contain an email address.
     * @param submission The submission, that receives a new co-author.
     */
    public void addCoAuthor(Submission submission, User coAuthor) {
    }

    /**
     * Determines whether a user has permission to view a submission
     *
     * @param submission The {@link Submission}, with a valid id, to be viewed.
     * @param user       The {@link User} whose view access is being determined.
     *                   Must contain a view-privilege.
     * @return {@code false} if view access is restricted, {@code true} otherwise.
     */
    public boolean canView(Submission submission, User user) {
        return false;
    }


    /**
     * Delivers all submissions, that a user has submitted, is an editor to or reviews.
     *
     * @param privilege    The role, to which submissions belong, in relation to a user.
     *                     Meaning, the user can request to receive the submissions which he is an editor to,
     *                     reviews or has submitted himself.
     * @param user         The user, whose editorial, reviewed or own and coauthored submissions.
     * @param resultParams The parameters, that control filtering and sorting of the resulting list.
     * @return The resulting list of submissions, which a user is involved in.
     */
    public List<Submission> getList(Privilege privilege, User user, ResultListParameters resultParams) {
        return null;
    }

    /**
     * Delivers all submissions, that a user either has submitted, is an editor to or reviews
     * in a specified scientific forum.
     *
     * @param scientificForum The {@link ScientificForum} where the wanted submissions are submitted to.
     * @param user            The user, whose editorial, reviewed or own submissions.
     * @param privilege       The role, to which submissions belong, in relation to a user.
     *                        Meaning, the user can request to receive the submissions which he is an editor to,
     *                        reviews or has submitted himself.
     * @param resultParams    The parameters, that control filtering and sorting of the resulting list.
     * @return The resulting list of submissions, that were submitted to a given scientific forum.
     */
    public List<Submission> getList(ScientificForum scientificForum, User user, Privilege privilege,
                                    ResultListParameters resultParams) {
        return null;
    }

    /**
     * Gets a list all submissions that belong to a given scientific forum.
     *
     * @param scientificForum      A {@link ScientificForum} with a valid id.
     * @param resultListParameters The {@link ResultListParameters} that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of all {@link Submission}s that belong to a given scientific forum.
     */
    public static List<Submission> getList(ScientificForum scientificForum,
                                           ResultListParameters
                                                   resultListParameters) {
        return null;
    }

    /**
     * The amount of submissions where the given user is a direct author to.
     *
     * @param user A {@link User}-DTO with a valid id.
     * @return The number of submission the specified user authored.
     */
    public static int countSubmissions(User user) {
        return 0;
    }
}