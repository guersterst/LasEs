package de.lases.business.service;

import de.lases.global.transport.*;

import de.lases.persistence.exception.*;

import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.NotFoundException;

import de.lases.persistence.repository.*;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Provides functionality regarding the management and handling of submissions.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 *
 * @author Thomas Kirz
 */
@Dependent
public class SubmissionService implements Serializable {

    private static final Logger logger = Logger.getLogger(SubmissionService.class.getName());

    @Serial
    private static final long serialVersionUID = 3347910586400642643L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

    @Inject
    private UserService userService;


    /**
     * Gets a submission.
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @return The submission's data.
     */
    public Submission get(Submission submission) {
        if (submission.getId() == null) {
            logger.severe("The passed Submission-DTO does not contain an id.");
            throw new IllegalArgumentException("The passed Submission-DTO does not contain an id.");
        }

        Submission result = null;
        Transaction t = new Transaction();
        try {
            result = SubmissionRepository.get(submission, t);
            t.commit();
            logger.finer("Submission with id " + submission.getId() + " retrieved.");
        } catch (NotFoundException e) {
            logger.severe("Submission not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.requestedSubmissionDoesNotExist"),
                    MessageCategory.ERROR));
            t.abort();
        }

        return result;
    }

    /**
     * Creates a new submission.
     * <p>
     * The selected editor and co-authors are informed about this
     * using the {@link de.lases.business.util.EmailUtil} utility.
     * Do not forget to also add a paper using {@link PaperService#add(FileDTO, Paper)}.
     * </p>
     *
     * @param submission The submission's data in a {@link Submission}.
     *                   Must contain a valid forum's id, authorId, editorId, state and title.
     * @param coAuthors  The desired co-athors as proper {@link User}-DTOs with an email-address.
     * @return The submission that was added, but filled with its id.
     */
    public Submission add(Submission submission, List<User> coAuthors) {
        Transaction transaction = new Transaction();

        User editor = new User();
        editor.setId(submission.getEditorId());
        try {
            editor = UserRepository.get(editor, transaction);
        } catch (NotFoundException ex) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString(
                    "dataNotWritten"), MessageCategory.ERROR));
            logger.log(Level.WARNING, "Editor was not found when adding a submission.");
            transaction.abort();
            return null;
        }
        // TODO: Das einkommentieren, wenn es die get Methode im user repo gibt!
        logger.log(Level.SEVERE, "UNCOMMENT THIS");
        // assert editor != null;
        // assert editor.getEmailAddress() != null;

        if (!coAuthors.stream().allMatch((user) -> user.getEmailAddress() != null)) {
            throw new InvalidFieldsException("There where co-authors without an email address!");
        }

        List<String> coAuthorEmails = coAuthors.stream().map(User::getEmailAddress).toList();

        // TODO: Das auch einkommentieren!
        // sendEmailsForAddSubmission(editor.getEmailAddress(), coAuthorEmails);

        try {
            submission = SubmissionRepository.add(submission, transaction);
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("dataNotWritten"),
                    MessageCategory.ERROR));
            logger.log(Level.WARNING, e.getMessage());
            transaction.abort();
            return null;
        }
        for (User user: coAuthors) {
            try {
                User userWithId;
                if (UserRepository.emailExists(user, transaction)) {
                    userWithId = UserRepository.get(user, transaction);
                } else {
                    user.setRegistered(false);
                    userWithId = UserRepository.add(user, transaction);
                }
                assert userWithId.getId() != null;
                SubmissionRepository.addCoAuthor(submission, userWithId, transaction);
            } catch (DataNotWrittenException e) {
                uiMessageEvent.fire(new UIMessage(
                        resourceBundle.getString("dataNotWritten"),
                        MessageCategory.ERROR));
                logger.log(Level.WARNING, e.getMessage());
                transaction.abort();
                return null;
            } catch (NotFoundException e) {
                logger.log(Level.SEVERE, e.getMessage());
                transaction.abort();
                throw new AssertionError("Tried to add co authors that don't exist even though this method"
                        + " makes sure that every co author exists.");
            }
        }
        transaction.commit();
        return submission;
    }

    private void sendEmailsForAddSubmission(String emailEditor, List<String> emailsCoAuthors) {
        logger.log(Level.SEVERE, "PLEASE IMPLEMENT ME! (sending emails)");
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
     * <p>
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
        Transaction transaction = new Transaction();
        try {
            return ReviewedByRepository.get(submission, reviewer, transaction);
        } catch (NotFoundException e) {
            return null;
        } finally {
            transaction.commit();
        }
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
     *                   Must contain an id.
     * @return {@code false} if view access is restricted, {@code true} otherwise.
     */
    public boolean canView(Submission submission, User user) {
        if (submission.getId() == null || user.getId() == null) {
            logger.severe("Submission and user id must not be null.");
            throw new IllegalArgumentException("Submission and user id must not be null.");
        }

        // load user from database
        Transaction t = new Transaction();
        try {
            user = UserRepository.get(user, t);
            logger.finer("User with id " + user.getId() + " retrieved.");
        } catch (NotFoundException e) {
            logger.severe("User with id " + user.getId() + " not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.loadingSubmissionFailed"),
                    MessageCategory.ERROR));
            t.abort();
            return false;
        }

        // load submission from database
        try {
            submission = SubmissionRepository.get(submission, t);
            logger.finer("Submission with id " + submission.getId() + " retrieved.");
        } catch (NotFoundException e) {
            logger.severe("Submission with id " + submission.getId() + " not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.loadingSubmissionFailed"),
                    MessageCategory.ERROR));
            t.abort();
            return false;
        }

        ScientificForum forum = new ScientificForum();
        forum.setId(submission.getScientificForumId());

        // Check if the user is admin, editor of the forum
        // or reviewer or author of the submission
        return user.isAdmin()
                || userService.getList(forum).contains(user)
                || userService.getList(submission, Privilege.REVIEWER).contains(user)
                || userService.getList(submission, Privilege.AUTHOR).contains(user);
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
        if (user.getId() == null) {
            logger.severe("The passed User-DTO has no id.");
            throw new IllegalArgumentException("The passed User-DTO has no id.");
        }

        List<Submission> result = null;
        Transaction t = new Transaction();
        try {
            result = SubmissionRepository.getList(user, privilege, t, resultParams);
            t.commit();
            logger.finer("List of submissions retrieved.");
        } catch (DataNotCompleteException e) {
            logger.warning("Data not complete: " + e.getMessage());
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("warning.dataNotComplete"),
                    MessageCategory.WARNING));
        } catch (NotFoundException e) {
            logger.severe("User to get submissions for not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.findingSubmissionListFailed"),
                    MessageCategory.ERROR));
            t.abort();
        }

        return result;
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
        if (user.getId() == null || scientificForum.getId() == null) {
            logger.severe("User and ScientificForum id must not be null.");
            throw new IllegalArgumentException("User and ScientificForum id must not be null.");
        }

        List<Submission> result = null;
        Transaction t = new Transaction();
        try {
            result = SubmissionRepository.getList(user, privilege, scientificForum, t, resultParams);
            t.commit();
            logger.finer("List of submissions retrieved.");
        } catch (DataNotCompleteException e) {
            logger.warning("Data not complete: " + e.getMessage());
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("warning.dataNotComplete"),
                    MessageCategory.WARNING));
        } catch (NotFoundException e) {
            logger.severe("User or ScientificForum to get submissions for not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.findingSubmissionListFailed"),
                    MessageCategory.ERROR));
            t.abort();
        }

        return result;
    }

    /**
     * Gets a list all submissions that belong to a given scientific forum.
     *
     * @param scientificForum A {@link ScientificForum} with a valid id.
     * @param resultParams    The {@link ResultListParameters} that results
     *                        parameters from the pagination like
     *                        filtering, sorting or number of elements.
     * @return A list of all {@link Submission}s that belong to a given scientific forum.
     */
    public List<Submission> getList(ScientificForum scientificForum,
                                    ResultListParameters resultParams) {
        if (scientificForum.getId() == null) {
            logger.severe("ScientificForum id must not be null.");
            throw new IllegalArgumentException("ScientificForum id must not be null.");
        }

        List<Submission> result = null;
        Transaction t = new Transaction();
        try {
            result = SubmissionRepository.getList(scientificForum, t, resultParams);
            t.commit();
            logger.finer("List of submissions retrieved.");
        } catch (DataNotCompleteException e) {
            logger.warning("Data not complete: " + e.getMessage());
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("warning.dataNotComplete"),
                    MessageCategory.WARNING));
        } catch (NotFoundException e) {
            logger.severe("ScientificForum to get submissions for not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.findingSubmissionListFailed"),
                    MessageCategory.ERROR));
            t.abort();
        }

        return result;
    }

    /**
     * The amount of submissions where the given user is a direct author to.
     *
     * @param privilege The role, to which submissions belong, in relation to a user
     *                  Meaning, the user can request to receive the submissions which
     *                  he is an editor to, reviews or has submitted himself.
     * @param user      A {@link User}-DTO with a valid id.
     * @return The number of submission the specified user authored and -1
     * if retrieving the amount of submissions failed.
     */
    public int countSubmissions(User user, Privilege privilege, ResultListParameters resultParams) {
        if (user.getId() == null) {
            logger.severe("The passed User-DTO has no id.");
            throw new IllegalArgumentException("The passed User-DTO has no id.");
        }

        Transaction t = new Transaction();
        try {
            return SubmissionRepository.countSubmissions(user, privilege, t, resultParams);
        } catch (NotFoundException e) {
            logger.severe("User to count submissions for not found.");
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.findingSubmissionListFailed"),
                    MessageCategory.ERROR));
            t.abort();
            return -1;
        }
    }
}