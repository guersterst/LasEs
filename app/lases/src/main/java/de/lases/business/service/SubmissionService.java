package de.lases.business.service;

import de.lases.business.util.EmailUtil;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.*;

import de.lases.persistence.exception.*;

import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.NotFoundException;

import de.lases.persistence.repository.*;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * Provides functionality regarding the management and handling of submissions.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 *
 * @author Thomas Kirz
 * @author Stefanie Gürster
 * @author Sebastian Vogt
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

    @Inject
    private FacesContext facesContext;

    @Inject
    private SessionInformation sessionInformation;


    /**
     * Gets a submission.
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @return The submission's data.
     * @author Thomas Kirz
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
        } catch (NotFoundException | DataNotCompleteException e) {
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
     * @param paper The first paper of this new submission.
     * @param file File DTO for the submitted paper.
     *
     * @return The submission that was added, but filled with its id.
     * @author Sebastian Vogt
     */
    public Submission add(Submission submission, List<User> coAuthors, Paper paper, FileDTO file) {
        if (!coAuthors.stream().allMatch((user) -> user.getEmailAddress() != null)) {
            throw new IllegalArgumentException("The co author's email address must not be null");
        }

        Transaction transaction = new Transaction();

        try {
            SubmissionRepository.add(submission, transaction);
            paper.setSubmissionId(submission.getId());
            PaperRepository.add(paper, file, transaction);
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("dataNotWritten"),
                    MessageCategory.ERROR));
            logger.log(Level.WARNING, e.getMessage());
            transaction.abort();
            return null;
        }

        List<User> coAuthorsFilled = new ArrayList<>();
        for (User user : coAuthors) {
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
                coAuthorsFilled.add(userWithId);
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

        if (sendEmailsForAddSubmission(submission, transaction, coAuthorsFilled)) {
            transaction.commit();
            return submission;
        } else {
            transaction.abort();
            return null;
        }
    }

    /**
     * @author Sebastian Vogt
     */
    private boolean sendEmailsForAddSubmission(Submission submission, Transaction transaction, List<User> coAuthors) {
        User editor = new User();
        editor.setId(submission.getEditorId());
        try {
            editor = UserRepository.get(editor, transaction);
        } catch (NotFoundException ex) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString(
                    "dataNotWritten"), MessageCategory.ERROR));
            logger.log(Level.WARNING, "Editor was not found when adding a submission.");
            return false;
        }

        assert editor != null;
        assert editor.getEmailAddress() != null;

        List<String> emailsCoAuthorsRegistered = coAuthors.stream().filter(User::isRegistered)
                .map(User::getEmailAddress).toList();
        List<String> emailsCoAuthorsNotRegistered = coAuthors.stream().filter(user -> !user.isRegistered())
                .map(User::getEmailAddress).toList();

        logger.log(Level.INFO, emailsCoAuthorsNotRegistered.toString());
        logger.log(Level.INFO, emailsCoAuthorsRegistered.toString());

        String emailEditor = editor.getEmailAddress();

        try {
            if (!emailsCoAuthorsNotRegistered.isEmpty()) {
                EmailUtil.sendEmail(emailsCoAuthorsNotRegistered.toArray(new String[0]), null,
                        resourceBundle.getString("email.assignedCoAuthor.subject"),
                        resourceBundle.getString("email.assignedCoAuthor.body") + "\n"
                                +  submission.getTitle() + "\n" + EmailUtil.generateLinkForEmail(facesContext,
                                "views/anonymous/register.xhtml"));
            }
            if (!emailsCoAuthorsRegistered.isEmpty()) {
                EmailUtil.sendEmail(emailsCoAuthorsRegistered.toArray(new String[0]), null,
                        resourceBundle.getString("email.assignedCoAuthor.subject"),
                        resourceBundle.getString("email.assignedCoAuthor.body") + "\n"
                                + submission.getTitle() + "\n" + EmailUtil.generateSubmissionURL(submission,
                                facesContext));
            }
            EmailUtil.sendEmail(new String[]{emailEditor}, null,
                    resourceBundle.getString("email.assignedEditor.subject"),
                    resourceBundle.getString("email.assignedEditor.body") + "\n" + submission.getTitle()
                                + "\n" + EmailUtil.generateSubmissionURL(submission, facesContext));
        } catch (EmailTransmissionFailedException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("emailNotSent") + " "
                    + String.join(", ", e.getInvalidAddresses()), MessageCategory.ERROR));
            return false;
        }
        return true;
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

        if (submission.getId() == null) {
            logger.severe("The id of the submission is not valid . Submission can't be deleted");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));
        } else {
            Transaction transaction = new Transaction();

            List<User> coAuthorList = getCoAuthors(transaction, submission);

            try {
                SubmissionRepository.remove(submission, transaction);
            } catch (DataNotWrittenException e) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.WARNING));
                transaction.abort();
                return;

            } catch (NotFoundException e) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
                transaction.abort();
                return;

            }


            if (submission.getEditorId() != null) {
                String subject = resourceBundle.getString("email.removeSubmission.subject");
                String body = resourceBundle.getString("email.removeSubmission.body")
                        .concat("\n").concat(submission.getTitle());

                User user = sessionInformation.getUser();
                if (user.getId().equals(submission.getEditorId())) {

                    User author = coAuthorList.get(0);
                    coAuthorList.remove(0);
                    List<String> emailAddress = coAuthorList.stream().map(User::getEmailAddress).toList();

                    if (sendEmail(author, subject, emailAddress.toArray(new String[0]), body)) {
                        transaction.commit();
                    } else {
                        transaction.abort();
                    }

                } else {

                    User editor = new User();
                    editor.setId(submission.getEditorId());

                    try {
                        editor = UserRepository.get(editor, transaction);
                    } catch (NotFoundException e) {
                        uiMessageEvent.fire(new UIMessage(resourceBundle.getString("userNotFound"), MessageCategory.ERROR));
                        transaction.abort();
                        return;
                    }

                    if (sendEmail(editor, subject, null, body)) {
                        transaction.commit();
                    } else {
                        transaction.abort();
                    }

                }
            } else {
                transaction.commit();
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
     * <li> When changed, the new editor and the old will be informed.</li>
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
     * @return false if an error occurred.
     */
    public boolean change(Submission newSubmission) {

        if (newSubmission.getId() == null) {

            logger.severe("The id of the submission is not valid. Therefore no submission can be changed. ");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));
        } else {
            Transaction transaction = new Transaction();
            Submission oldSubmission = null;

            try {
                oldSubmission = SubmissionRepository.get(newSubmission, transaction);
                SubmissionRepository.change(newSubmission, transaction);
            } catch (DataNotWrittenException e) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.ERROR));
                transaction.abort();
                return false;

            } catch (NotFoundException e) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("submissionNotFound"), MessageCategory.ERROR));
                transaction.abort();
                return false;
            } catch (DataNotCompleteException e) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("notComplete"), MessageCategory.ERROR));
                transaction.abort();
                return false;
            }

            String url = EmailUtil.generateSubmissionURL(newSubmission, facesContext);

            // Inform the old editor and the new editor about this change.
            if (oldSubmission != null && (!oldSubmission.getEditorId().equals(newSubmission.getEditorId()))) {

                User newEditor = new User();
                newEditor.setId(newSubmission.getEditorId());

                User oldEditor = new User();
                oldEditor.setId(oldSubmission.getEditorId());

                try {
                    newEditor = UserRepository.get(newEditor, transaction);
                    oldEditor = UserRepository.get(oldEditor, transaction);
                } catch (NotFoundException e) {
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("userNotFound"), MessageCategory.ERROR));
                    transaction.abort();
                    return false;
                }

                String subjectNewEditor = resourceBundle.getString("email.assignedEditor.subject");
                String bodyNewEditor = resourceBundle.getString("email.assignedEditor.body")
                        + "\n" + newSubmission.getTitle() + "\n"
                        + url;

                String subjectOldEditor = resourceBundle.getString("email.removeEditor.subject");
                String bodyOldEditor = resourceBundle.getString("email.removeEditor.body")
                        .concat("\n").concat(newSubmission.getTitle());

                if (sendEmail(newEditor, subjectNewEditor, null, bodyNewEditor) && sendEmail(oldEditor, subjectOldEditor, null, bodyOldEditor)) {
                    transaction.commit();
                    return true;
                } else {
                    transaction.abort();
                    return false;
                }
            } else if (newSubmission.getState() == SubmissionState.ACCEPTED || newSubmission.getState() == SubmissionState.REJECTED) {

                // Inform submitter a co-authors about a changed submission state.
                String subject = "";
                String body = "";
                if (newSubmission.getState() == SubmissionState.ACCEPTED) {

                    subject = resourceBundle.getString("email.submissionAccepted.subject");
                    body = resourceBundle.getString("email.submissionAccepted.body")
                            + "\n" + newSubmission.getTitle() + "\n"
                            + url;

                }

                if (newSubmission.getState() == SubmissionState.REJECTED) {

                    subject = resourceBundle.getString("email.submissionRejected.subject");
                    body = resourceBundle.getString("email.submissionRejected.body")
                            + "\n" + newSubmission.getTitle() + "\n"
                            + url;

                }
                return informAboutState(transaction, newSubmission, subject, body);
            } else if (newSubmission.getState() == SubmissionState.REVISION_REQUIRED) {

                String subject = resourceBundle.getString("email.requireRevision.subject");
                String body = resourceBundle.getString("email.requireRevision.body")
                        + "\n" + newSubmission.getTitle() + "\n"
                        + url;

                informAboutState(transaction, newSubmission, subject, body);

                Transaction trans = new Transaction();
                List<User> reviewerList = new ArrayList<>();
                try {
                    reviewerList = UserRepository.getList(trans, newSubmission, Privilege.REVIEWER);
                } catch (NotFoundException e) {
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("submissionNotFound"), MessageCategory.ERROR));
                    transaction.abort();
                    return false;
                } catch (DataNotCompleteException e) {
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("notComplete"), MessageCategory.ERROR));
                    transaction.abort();
                    return false;
                }

                String reviewerSub = resourceBundle.getString("email.requireRevisionReviewer.subject");
                String reviewerBody = resourceBundle.getString("email.requireRevisionReviewer.body")
                        + "\n" + newSubmission.getTitle() + "\n"
                        + url;

                return sendMultipleEmails(null, reviewerList, reviewerSub, reviewerBody);
            } else if (newSubmission.getState() == SubmissionState.SUBMITTED) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("newPaper"), MessageCategory.INFO));
                transaction.commit();
                return true;
            }
        }
        return false;

    }

    private List<User> getCoAuthors(Transaction transaction, Submission submission) {
        List<User> coAuthors = new ArrayList<>();
        try {
            coAuthors = UserRepository.getList(transaction, submission, Privilege.AUTHOR);
        } catch (DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("notComplete"), MessageCategory.ERROR));
            transaction.abort();
        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
            transaction.abort();
        }

        return coAuthors;
    }

    private boolean informAboutState(Transaction transaction, Submission submission, String subject, String body) {
        List<User> coAuthors = getCoAuthors(transaction, submission);

        if (coAuthors == null) {
            transaction.abort();
            return false;
        }

        // Fill user DTO for submitter.
        User submitter = coAuthors.get(0);
        coAuthors.remove(0);


        sendMultipleEmails(submitter, coAuthors, subject, body);
        uiMessageEvent.fire(new UIMessage(resourceBundle.getString("changeData"), MessageCategory.INFO));
        transaction.commit();
        return true;

    }

    public void manageReviewer(User newReviewer, ReviewedBy reviewedBy, List<User> reviewerList) {
        Transaction transaction = new Transaction();

        if (newReviewer.getEmailAddress() == null) {
            logger.severe("The email address is null");
            throw new InvalidFieldsException("The email address is null.");
        }

        User reviewer = new User();

        try {
            if (!UserRepository.emailExists(newReviewer, transaction)) {
                newReviewer.setRegistered(false);
                newReviewer.setFirstName(" ");
                newReviewer.setLastName(" ");
                reviewer = UserRepository.add(newReviewer, transaction);
            } else {
                reviewer = UserRepository.get(newReviewer, transaction);
            }
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("dataNotWritten"),
                    MessageCategory.ERROR));
            transaction.abort();
            return;
        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("dataNotFound"),
                    MessageCategory.ERROR));
            transaction.abort();
            return;
        }

        reviewedBy.setReviewerId(reviewer.getId());
        Submission submission = new Submission();
        submission.setId(reviewedBy.getSubmissionId());

        List<User> coAuthors = getCoAuthors(transaction, submission);
        boolean isAuthor = false;

        // If you are author or co-author you should not review your own submission.
        for (User user : coAuthors) {
            if (user.getId() == reviewer.getId()) {
                isAuthor = true;
                break;
            }
        }

        // An admin is always allowed to review.
        if (!isAuthor || reviewer.isAdmin()) {
            if (reviewerList.contains(reviewer)) {
                try {
                    ReviewedByRepository.change(reviewedBy, transaction);
                    transaction.commit();
                } catch (DataNotWrittenException e) {
                    transaction.abort();
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reviewRequestDoesNotExist"), MessageCategory.ERROR));
                } catch (NotFoundException e) {
                    transaction.abort();
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reviewReviewedByCouldNotUpdate"), MessageCategory.ERROR));
                }
            } else {
                if (addReviewer(reviewer, reviewedBy, transaction)) {
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("addReviewerToSub"), MessageCategory.INFO));
                } else {
                    uiMessageEvent.fire(new UIMessage(resourceBundle.getString("couldNotInformReviewer"), MessageCategory.WARNING));
                    transaction.abort();
                }
            }
        } else {
            // In case of a user is already author he could not be reviewer except he is an admin.
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("alreadyAuthor"), MessageCategory.WARNING));
            transaction.abort();
        }
    }

    /**
     * Adds a reviewer to a submission.
     * <p>
     * If successful the reviewer is informed by email using the
     * {@code EmailUtil} utility.
     * </p>
     *
     * @param reviewer   The reviewer to be added to the submission.
     * @param reviewedBy Information about the review-request relationship.
     */
    private boolean addReviewer(User reviewer, ReviewedBy reviewedBy, Transaction transaction) {
        if (reviewer.getId() == null || !reviewer.getId().equals(reviewedBy.getReviewerId())) {
            logger.severe("The id in the reviewed_by DTO and the id in the user DTO does not match or at least one of them is null");
            throw new InvalidFieldsException("The id's need to be equal and must not be null.");
        }

        try {
            SubmissionRepository.addReviewer(reviewedBy, transaction);
            transaction.commit();
        } catch (DataNotWrittenException e) {

            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.WARNING));
            transaction.abort();
            return false;

        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.WARNING));
            transaction.abort();
            return false;
        }

        Submission submission = new Submission();
        submission.setId(reviewedBy.getSubmissionId());

        submission = get(submission);

        assert submission != null : "Submission is null";

        String subject = resourceBundle.getString("email.assignedReviewer.subject");
        String body = "";

        if (reviewer.isRegistered()) {
            body = resourceBundle.getString("email.assignedReviewer.body") + "\n"
                    + submission.getTitle() + "\n" + EmailUtil.generateSubmissionURL(submission, facesContext);
        } else {
            body = resourceBundle.getString("email.assignedNewReviewer.body") + "\n" + submission.getTitle() + "\n"
                    + resourceBundle.getString("email.assignedNewReviewerRegister.body") + "\n"
                    + EmailUtil.generateLinkForEmail(facesContext, "views/anonymous/register.xhtml\"");
        }

        return sendEmail(reviewer, subject, null, body);
    }

    private boolean sendEmail(User user, String subject, String[] cc, String body) {

        assert user != null;
        assert user.getEmailAddress() != null;

        String emailAddress = user.getEmailAddress();

        logger.log(Level.INFO, emailAddress);

        try {
            EmailUtil.sendEmail(new String[]{emailAddress}, cc, subject, body);
        } catch (EmailTransmissionFailedException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("emailNotSent") + " " + String.join(", ", e.getInvalidAddresses()), MessageCategory.ERROR));
            return false;
        }

        return true;
    }

    private boolean sendMultipleEmails(User user, List<User> userList, String subject, String body) {

        assert !userList.isEmpty();

        String userEmail = "";
        if (user != null) {
            userEmail = user.getEmailAddress();
        }

        List<String> emailAddressList = userList.stream().map(User::getEmailAddress).toList();
        try {
            if(user != null) {
                EmailUtil.sendEmail(new String[]{userEmail}, emailAddressList.toArray(new String[0]), subject, body);
            } else {
                EmailUtil.sendEmail(emailAddressList.toArray(new String[0]), null, subject, body);
            }
        } catch (EmailTransmissionFailedException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("emailNotSent") + " " + String.join(", ", e.getInvalidAddresses()), MessageCategory.ERROR));
            return false;
        }
        return true;
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
     * Gets a list of all relations between the specific {@code submission} and the reviewer of it.
     *
     * @param submission The submission which is requested to be reviewed by a reviewer.
     * @return A List of {@link ReviewedBy}-DTO containing information about the review-request relationship of a
     * submission and the reviewer.
     */
    public List<ReviewedBy> getList(Submission submission) {
        if (submission.getId() == null) {
            logger.severe("The id of the submission is null. The requested list could not be loaded.");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));
        }

        Transaction transaction = new Transaction();

        List<ReviewedBy> resultList = new ArrayList<>();

        try {
            resultList = ReviewedByRepository.getList(submission, transaction);
            transaction.commit();
        } catch (NotFoundException e) {

            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
            transaction.abort();

        }
        return resultList;
    }

    /**
     * Updates the given {@code ReviewedBy} in the repository.
     *
     * @param reviewedBy The fully filled {@link ReviewedBy}.
     */
    public void changeReviewedBy(ReviewedBy reviewedBy) {
        Transaction transaction = new Transaction();
        try {
            ReviewedByRepository.change(reviewedBy, transaction);
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("changeData"), MessageCategory.INFO));
            transaction.commit();
        } catch (NotFoundException e) {
            transaction.abort();
            logger.info("Could not update ReviewedBy: " + reviewedBy + e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reviewRequestDoesNotExist"), MessageCategory.ERROR));
        } catch (DataNotWrittenException e) {
            transaction.abort();
            logger.info("Could not update ReviewedBy: " + reviewedBy + e.getMessage());
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reviewReviewedByCouldNotUpdate"), MessageCategory.ERROR));
        }
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
        if (submission.getId() == null) {
            logger.severe("The id of the submission is null.");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));
        }
        if (reviewer.getId() == null) {
            logger.severe("The id of the reviewer is null.");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));
        }

        Transaction transaction = new Transaction();

        try {
            ReviewedByRepository.removeReviewer(submission, reviewer, transaction);
            transaction.commit();
        } catch (DataNotWrittenException e) {

            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.WARNING));
            transaction.abort();
            return;
        } catch (NotFoundException e) {

            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.ERROR));
            transaction.abort();
            return;
        }

        String subject = resourceBundle.getString("email.removeReviewer.subject");

        StringBuilder body = new StringBuilder(resourceBundle.getString("email.removeReviewer.body"));
        body.append(" ");
        body.append(submission.getTitle());

        sendEmail(reviewer, subject, null, body.toString());
    }

    /**
     * Determines whether a user has permission to view a submission
     *
     * @param submission The {@link Submission}, with a valid id, to be viewed.
     * @param user       The {@link User} whose view access is being determined.
     *                   Must contain an id.
     * @return {@code false} if view access is restricted, {@code true} otherwise.
     * @author Thomas Kirz
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
        } catch (NotFoundException | DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.loadingSubmissionFailed"),
                    MessageCategory.ERROR));
            t.abort();
            return false;
        }

        ScientificForum forum = new ScientificForum();
        forum.setId(submission.getScientificForumId());

        t.commit();

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
     * @author Thomas Kirz
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
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("warning.dataNotComplete"),
                    MessageCategory.WARNING));
        } catch (NotFoundException e) {
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
     * @author Thomas Kirz
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
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("warning.dataNotComplete"),
                    MessageCategory.WARNING));
        } catch (NotFoundException e) {
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
     * @author Thomas Kirz
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
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("warning.dataNotComplete"),
                    MessageCategory.WARNING));
        } catch (NotFoundException e) {
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
     * @author Thomas Kirz
     */
    public int countSubmissions(User user, Privilege privilege, ResultListParameters resultParams) {
        if (user.getId() == null) {
            logger.severe("The passed User-DTO has no id.");
            throw new IllegalArgumentException("The passed User-DTO has no id.");
        }

        Transaction t = new Transaction();
        int count = 0;
        try {
            count = SubmissionRepository.countSubmissions(user, privilege, t, resultParams);
            t.commit();
        } catch (NotFoundException | DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("error.findingSubmissionListFailed"),
                    MessageCategory.ERROR));
            t.abort();
        }
        return count;
    }
}