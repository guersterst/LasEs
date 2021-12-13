package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
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
    private ReviewRepository reviewRepository;

    /**
     * Gets a {@link Review}.
     *
     * @param review The requested {@code Review} containing a valid id.
     * @return The fully filled requested {@code Review}.
     */
    public Review get(Review review) {
        return null;
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
     * @param file   The {@link FileDTO} containing the pdf review itself..
     */
    public void add(Review review, FileDTO file) {

    }

    /**
     * Removes a {@link Review}.
     * <p>
     * If the {@code User} removing the review is not
     * the reviewer himself an email will be dispatched to the
     * reviewer informing him about this action.
     * The {@link de.lases.business.util.EmailUtil} utility will
     * be used for this.
     * </p>
     *
     * @param review The {@code Review} that is to be removed. Must be filled with a valid
     *               reviewerId, paperId and submissionId.
     * @param user   The {@link User} who removes the {@code Review}.
     *               Must contain a valid id.
     */
    public void remove(Review review, User user) {
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
            return ReviewRepository.getList(submission, user, transaction, resultListParameters);
        } catch (DataNotCompleteException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
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
            pages =  (int) Math.ceil((double) items / paginationLength);
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
        return null;
    }
}
