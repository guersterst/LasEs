package de.lases.business.service;

import de.lases.global.transport.*;

import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.PaperRepository;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;
import java.util.PropertyResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stefanie Gürster, Sebastian Vogt
 *
 * Provides functionality for handling papers in {@link Submission}s.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class PaperService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private PropertyResourceBundle resourceBundle;

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

            String message = resourceBundle.getString("idMissing");
            throw new IllegalArgumentException(message);

        } else {

            Transaction transaction = new Transaction();
            Paper resultPaper = null;

            try {

                resultPaper = PaperRepository.get(paper, transaction);
                transaction.commit();

            } catch (NotFoundException exception) {

                String message = resourceBundle.getString("paperNotFound");
                uiMessageEvent.fire(new UIMessage(message, MessageCategory.ERROR));

                logger.fine("Error while loading a paper with the submission id: " + paper.getSubmissionId()
                        + " and version number: " + paper.getVersionNumber());

                transaction.abort();

            }
            return resultPaper;
        }
    }

    /**
     * Adds a {@link Paper} to a submission.
     * <p>
     * Whether this is a submission-pdf or a revision-pdf is determined internally.
     * If this is a revision the editor will be informed by mail about this,
     * using the {@link de.lases.business.util.EmailUtil}-utility.
     * </p>
     *
     * @param file The {@link FileDTO} to be added with the paper,
     *             containing a {@code byte[]} with the pdf.
     * @param paper The filled {@link Paper} to be added.
     */
    public void add(FileDTO file, Paper paper) {
        Transaction transaction = new Transaction();

        // Create the submission dto for checking if the added paper is the
        // first one added to the submission.
        Submission submission = new Submission();
        submission.setId(paper.getSubmissionId());

        // Create an admin to get full access to the list of added papers
        User user = new User();
        user.setAdmin(true);

        List<Paper> paperList;

        try {
            paperList = PaperRepository.getList(submission, transaction,
                    user, new ResultListParameters());
        } catch (DataNotCompleteException e) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString(
                    "dataNotWritten"), MessageCategory.ERROR));
            logger.log(Level.WARNING, e.getMessage());
            transaction.abort();
            return;
        } catch (NotFoundException e) {
            throw new IllegalArgumentException("the submission specified in the"
                    + "paper DTO was not found", e);
        }

        assert paperList != null;
        if (!paperList.isEmpty()) {
            logger.log(Level.INFO, "Sending email to an editor.");
            // TODO: Email senden oder a ned in develop mode.
        }

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

    }

    /**
     * Returns a requested file.
     *
     * @param paper The paper whose file is requested. Must contain a valid id.
     * @return The requested file.
     */
    public FileDTO getFile(Paper paper) {
        return null;
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
        return null;
    }

    /**
     * Returns the latest paper of a submission.
     *
     * <p>
     * If there are no revisions for this submission the original {@link Paper} is the latest.
     * The paper returned is determined by the highest privilege, which that
     * user possesses on that submission. Meaning he might not have view access
     * on unreleased papers
     * </p>
     *
     * @param submission A {@link Submission}-DTO containing a valid id.
     * @param user       The user who requests the papers, containing a valid view-privilege.
     * @return The submissions paper, which was least recently uploaded.
     */
    public Paper getLatest(Submission submission, User user) {
        return null;
    }
}