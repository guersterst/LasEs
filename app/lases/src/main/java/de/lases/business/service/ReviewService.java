package de.lases.business.service;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

/**
 * @author Stefanie Gürster
 *
 * Provides functionality for dealing with reviews of submission papers.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class ReviewService implements Serializable {

    @Serial
    private static final long serialVersionUID = 7609005550975689458L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

    @Inject
    private ConfigPropagator configPropagator;

    private static final Logger logger = Logger.getLogger(PaperService.class.getName());

    /**
     * Gets a {@link Review}.
     *
     * @param review The requested {@code Review} containing submissionId, versionNumber, reviewerId.
     * @return The fully filled requested {@code Review}, null if not found.
     */
    public Review get(Review review) {
        Transaction transaction = new Transaction();
        try {
            Review retReview = ReviewRepository.get(review, transaction);
            transaction.commit();
            return retReview;
        } catch (NotFoundException e) {
            transaction.abort();
            return null;
        }
    }

    /**
     * Updates a {@link Review}.
     *
     * @param newReview A fully filled {@link Review}.
     *                  <p>
     *                  All fields filled with legal values will be overwritten.
     *                  It should contain an existing id value.
     *                  </p>
     */
    public void change(Review newReview) {
        if (newReview == null) {
            throw new IllegalArgumentException("Cannot change null review.");
        }
        Transaction transaction = new Transaction();
        try {
            ReviewRepository.change(newReview, transaction);
        } catch (DataNotWrittenException e) {
            logger.severe("Cannot change revision: " + newReview + e.getMessage());
            uiMessageEvent.fire(new UIMessage("Could not update the state of this review.", MessageCategory.FATAL));
            transaction.abort();
        } catch (NotFoundException e) {
            logger.info("Did not find: " + newReview + e.getMessage());
            uiMessageEvent.fire(new UIMessage("This review does not exist.", MessageCategory.ERROR));
            transaction.abort();
        }

        if (newReview.isVisible()) {
            Submission submission = new Submission();
            submission.setId(newReview.getSubmissionId());

            try {
                submission = SubmissionRepository.get(submission, transaction);
            } catch (DataNotCompleteException e) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("notComplete"), MessageCategory.ERROR));
                transaction.abort();
            } catch (NotFoundException e) {
                uiMessageEvent.fire(new UIMessage("submissionNotFound", MessageCategory.ERROR));
                transaction.abort();
            }

            String subject = resourceBundle.getString("email.releaseReview.subject");
            String body = resourceBundle.getString("email.releaseReview.body")
                    + "\n" + submission.getTitle() + "\n"
                    + configPropagator.getProperty("BASE_URL") + "/views/authenticated/submission.xhtml?id=" + submission.getId();

            SubmissionService submissionService = new SubmissionService();
            submissionService.informAboutState(transaction, submission, subject, body);
        }
    }

    /**
     * Adds a {@link Review} to a {@link Submission}.
     * <p>
     * The {@code Review} will always be added to the latest {@link Paper}.
     * It has to be submitted
     * within the given timeline and one reviewer can only submit one {@code Review}
     * per {@code Paper}.
     * </p>
     *
     * @param review The {@code Review} that is submitted.
     *               Must be filled with a valid
     *               reviewerId, paperVersion and submissionId.
     * @param file   The {@link FileDTO} containing the pdf review itself.
     */
    public void add(Review review, FileDTO file) {
        Transaction transaction = new Transaction();
        try {
            ReviewRepository.add(review, file, transaction);
            transaction.commit();
        } catch (DataNotWrittenException e) {
            uiMessageEvent.fire(new UIMessage("Upload of review failed.", MessageCategory.ERROR));
            transaction.abort();
        } catch (NotFoundException e) {
            uiMessageEvent.fire(new UIMessage("This submission does not have a paper to review.", MessageCategory.ERROR));
            transaction.abort();
        }
    }

    /**
     * Gets all {@link Review}s that can be viewed by a given {@link User}.
     * For example, a reviewer can only view his own reviews.
     *
     * @param submission           The {@link Submission} from which the reviews are requested.
     * @param user                 The {@code User} who requests to see the {@code Reviews}.
     * @param resultListParameters The parameters, that control filtering and
     *                             sorting of the resulting list.
     * @return The requested reviews, which the given user is allowed to view.
     */
    public List<Review> getList(Submission submission, User user,
                                ResultListParameters resultListParameters) {
        Transaction transaction = new Transaction();
        try {
            List<Review> reviewsList = ReviewRepository.getList(submission, user, transaction, resultListParameters);
            transaction.commit();
            return reviewsList;
        } catch (DataNotCompleteException | NotFoundException e) {
            uiMessageEvent.fire(new UIMessage("Loading Reviews failed.", MessageCategory.ERROR));
            transaction.abort();
        }
        return new ArrayList<>();
    }

    public int getListCountPages(Submission submission, User user, ResultListParameters resultListParameters) {
        Transaction transaction = new Transaction();
        int items = 0;
        int pages = 0;
        try {
            items = ReviewRepository.getCountItemsList(submission, user, transaction, resultListParameters);
            ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
            int paginationLength = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LIST_LENGTH"));
            // Calculate number of pages.
            pages = (int) Math.ceil((double) items / paginationLength);
        } catch (DataNotCompleteException | NotFoundException e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
        }
        return pages;
    }

    /**
     * Gets the {@link FileDTO} containing the {@code Review}'s pdf.
     *
     * @param review The {@code Review} containing the requested file.
     *               Must contain a valid id.
     * @return The requested {@code File}.
     */
    public FileDTO getFile(Review review) {
        if (review == null) {
            logger.severe("This review is not valid. Therefore no file dto can be queried.");
            throw new IllegalArgumentException(resourceBundle.getString("idMissing"));
        } else {
            Transaction transaction = new Transaction();
            FileDTO file = null;
            try {
                file = ReviewRepository.getPDF(review, transaction);
                transaction.commit();
            } catch (NotFoundException exception) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reviewNotFound"), MessageCategory.ERROR));
                logger.fine("Error while loading a file of a review with the submission id: " + review.getSubmissionId()
                        + " and version number: " + review.getPaperVersion() + "and reviewer id: " + review.getReviewerId());
                transaction.abort();
            }

            if (file.getFile() == null) {
                uiMessageEvent.fire(new UIMessage(resourceBundle.getString("reviewNotFound"), MessageCategory.INFO));
                logger.info("No file gotten for review: " + review);
            }

            return file;
        }
    }
}
