package de.lases.business.service;

import de.lases.business.util.EmailUtil;
import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.*;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Provides functionality for handling papers in {@link Submission}s.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 *
 * @author Stefanie Gürster
 * @author Sebastian Vogt
 */
@Dependent
public class PaperService implements Serializable {

    @Serial
    private static final long serialVersionUID = -6799015652446918829L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

    @Inject
    private FacesContext facesContext;

    private static final Logger logger = Logger.getLogger(PaperService.class.getName());

    /**
     * Gets a paper.
     * <p>
     * A {@link Paper} may be the paper associated with a submission or a revision.
     * </p>
     *
     * @param paper A {@code Paper} containing a valid id of the requested paper.
     * @return A fully filled {@link Paper}.
     */
    public Paper get(Paper paper) {
        if (paper.getSubmissionId() == null && paper.getVersionNumber() == null) {

            logger.severe("The id of the paper is not valid. Therefore no paper object can be queried.");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));

        } else {

            Transaction transaction = new Transaction();
            Paper resultPaper = null;

            try {

                resultPaper = PaperRepository.get(paper, transaction);
                transaction.commit();

            } catch (NotFoundException exception) {

                String message = resourceBundle.getString("paperNotFound");
                uiMessageEvent.fire(new UIMessage(message, MessageCategory.ERROR));

                transaction.abort();

            }
            return resultPaper;
        }
    }

    /**
     * Adds a {@link Paper} to a submission. The editor will be informed by mail about this,
     * using the {@link de.lases.business.util.EmailUtil}-utility.
     *
     * @param file  The {@link FileDTO} to be added with the paper,
     *              containing a {@code byte[]} with the pdf.
     * @param paper The filled {@link Paper} to be added.
     *
     * @author Sebastian Vogt
     */
    public void add(FileDTO file, Paper paper) {
        Transaction transaction = new Transaction();

        try {
            PaperRepository.add(paper, file, transaction);
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage(
                    resourceBundle.getString("dataNotWritten"),
                    MessageCategory.ERROR));
            logger.log(Level.WARNING, e.getMessage());
            transaction.abort();
            return;
        }

        if (sendEmailsForPaper(paper, transaction)) {
            transaction.commit();
        } else {
            transaction.abort();
        }
    }

    /**
     * @author Sebastian Vogt
     */
    private boolean sendEmailsForPaper(Paper paper, Transaction transaction) {
        Submission submission = new Submission();
        submission.setId(paper.getSubmissionId());

        try {
            submission = SubmissionRepository.get(submission, transaction);
        } catch (NotFoundException | DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString(
                    "dataNotWritten"), MessageCategory.ERROR));
            logger.log(Level.WARNING, "Submission was not found or could not be loaded " +
                    "when adding a paper.");
            return false;
        }

        User editor = new User();
        editor.setId(submission.getEditorId());

        try {
            editor = UserRepository.get(editor, transaction);
        } catch (NotFoundException ex) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString(
                    "dataNotWritten"), MessageCategory.ERROR));
            logger.log(Level.WARNING, "Editor was not found when adding a paper.");
            return false;
        }

        assert editor != null;
        assert editor.getEmailAddress() != null;

        String emailEditor = editor.getEmailAddress();

        try {
            EmailUtil.sendEmail(new String[]{emailEditor}, null,
                    resourceBundle.getString("email.editorRevision.subject"),
                    resourceBundle.getString("email.editorRevision.body") + "\n" + submission.getTitle()
                            + "\n" + EmailUtil.generateSubmissionURL(submission, facesContext));
        } catch (EmailTransmissionFailedException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("emailNotSent") + " "
                    + String.join(", ", e.getInvalidAddresses()), MessageCategory.ERROR));
            return false;
        }
        return true;
    }

    /**
     * Updates a paper.
     *
     * @param paper A fully filled {@link Paper}-DTO.
     *              <p>
     *              All fields filled with legal values will be overwritten.
     *              It should contain an existing id value.
     *              </p>
     */
    public void change(Paper paper) {
        if (paper.getSubmissionId() == null && paper.getVersionNumber() == null) {

            logger.severe("The id of the paper is not valid. Therefore no paper object can be changed.");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));

        } else {
            Transaction transaction = new Transaction();

            try {

                PaperRepository.change(paper, transaction);
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reminder"), MessageCategory.INFO));
                sendEmailForRevisionToReviewer(paper);
                transaction.commit();

            } catch (DataNotWrittenException exception) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.ERROR));
                logger.log(Level.WARNING, exception.getMessage());

                transaction.abort();

            } catch (NotFoundException exception) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("paperNotFound"), MessageCategory.ERROR));
                logger.fine("Error while changing a paper with the submission id: " + paper.getSubmissionId()
                        + " and version number: " + paper.getVersionNumber());

                transaction.abort();

            }
        }

    }

    private boolean sendEmailForRevisionToReviewer(Paper paper) {
        Submission submission = new Submission();
        submission.setId(paper.getSubmissionId());
        List<User> reviewerList = new ArrayList<>();

        Transaction trans = new Transaction();
        try {
            reviewerList = UserRepository.getList(trans, submission, Privilege.REVIEWER);
            trans.commit();
        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("submissionNotFound"), MessageCategory.ERROR));
            trans.abort();
            return false;
        } catch (DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("notComplete"), MessageCategory.ERROR));
            trans.abort();
            return false;
        }

        List<String> emailAddressList = reviewerList.stream().map(User::getEmailAddress).toList();

        try {
            EmailUtil.sendEmail(emailAddressList.toArray(new String[0]), null,
                    resourceBundle.getString("email.editorRevision.subject"),
                    resourceBundle.getString("email.editorRevision.body") + "\n" + submission.getTitle()
                            + "\n" + EmailUtil.generateSubmissionURL(submission, facesContext));
        } catch (EmailTransmissionFailedException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("emailNotSent") + " "
                    + String.join(", ", e.getInvalidAddresses()), MessageCategory.ERROR));
            return false;
        }
        return true;

    }

    /**
     * Removes a paper from a submission.
     * <p>
     * The editor will be informed by mail about this,
     * using the {@link de.lases.business.util.EmailUtil}-utility.
     * </p>
     *
     * @param paper A {@link Paper}-DTO containing a valid paper- and submission id.
     */
    public void remove(Paper paper) {

        if (paper.getSubmissionId() == null && paper.getVersionNumber() == null) {

            logger.severe("The id of the paper is not valid.");
            throw new InvalidFieldsException(resourceBundle.getString("idMissing"));

        } else {
            Transaction transaction = new Transaction();

            try {

                PaperRepository.remove(paper, transaction);
                transaction.commit();

            } catch (DataNotWrittenException exception) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotWritten"), MessageCategory.ERROR));
                transaction.abort();

            } catch (NotFoundException exception) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("paperNotFound"), MessageCategory.ERROR));
                transaction.abort();

            }
        }

    }

    /**
     * Returns a requested file.
     *
     * @param paper The paper whose file is requested. Must contain a valid id.
     * @return The requested file.
     */
    public FileDTO getFile(Paper paper) {
        if (paper.getSubmissionId() == null && paper.getVersionNumber() == null) {

            logger.severe("The id of the paper is not valid. Therefore no paper object can be queried.");
            throw new IllegalArgumentException(resourceBundle.getString("idMissing"));

        } else {
            Transaction transaction = new Transaction();
            FileDTO file = null;

            try {

                file = PaperRepository.getPDF(paper, transaction);
                transaction.commit();

            }  catch (NotFoundException | DataNotCompleteException exception) {

                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("paperNotFound"), MessageCategory.ERROR));
                transaction.abort();

            }
            return file;
        }
    }

    /**
     * Gets all papers associated with this submission.
     *
     * <p>
     * The papers returned are determined by the highest privilege, which that
     * user possesses on that submission.
     * </p>
     *
     * @param submission           The submission for which the submissions are requested. Must contain a valid id.
     * @param user                 The user who requests the papers, with its id and the "isAdmin" information.
     * @param resultListParameters The parameters, that control filtering and sorting of the resulting list.
     * @return The list of papers of this submission the given user has access to.
     */
    public List<Paper> getList(Submission submission, User user,
                               ResultListParameters resultListParameters) {
        Transaction transaction = new Transaction();
        List<Paper> paperList = new ArrayList<>();

        try{
            logger.finest("Getting paper list of a specific submission");
            paperList = PaperRepository.getList(submission, transaction, user, resultListParameters);
            transaction.commit();
        } catch (DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotComplete"), MessageCategory.WARNING));

            transaction.abort();

        } catch (NotFoundException e) {

            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("dataNotFound"), MessageCategory.WARNING));

            transaction.abort();
        }
        return paperList;
    }

    /**
     * Returns the latest visible paper of a submission.
     *
     * <p>
     * If there are no revisions for this submission the original {@link Paper} is the latest.
     * </p>
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @return The submissions paper, which was least recently uploaded.
     */
    public Paper getLatest(Submission submission) {
        Transaction transaction = new Transaction();
        Paper paper = null;
        try {
            paper = PaperRepository.getNewestPaperForSubmission(submission, transaction);
        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage("No review can be uploaded, as no paper was submitted.", MessageCategory.ERROR));
        } finally {
            transaction.commit();
        }
        return paper;
    }

    public int countPaper(Submission submission, User user, ResultListParameters resultListParameters) {
        if (submission.getId() == null || user.getId() == null){
            logger.severe("Submission id: " + submission.getId() + " user id: " + user.getId() + " must not be null.");
            throw new InvalidFieldsException("Id of submission or user should not be null.");
        }

        Transaction transaction = new Transaction();
        int pages = 0;

        try {
            int items = PaperRepository.countPaper(user,submission,transaction,resultListParameters);
            transaction.commit();

            ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
            int maxLengthOfPagination = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LIST_LENGTH"));

            pages = (int) Math.ceil((double) items / maxLengthOfPagination);

        } catch (DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("notComplete"), MessageCategory.ERROR));

            transaction.abort();

        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("getPaperListFailed"), MessageCategory.ERROR));

            transaction.abort();
        }

        return pages;
    }

}