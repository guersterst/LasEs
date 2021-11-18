package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Provides functionality for dealing with reviews of submission papers.
 * In case of an unexpected state, a {@link UIMessage} event will be fired.
 */
@Dependent
public class ReviewService {

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private Transaction transaction;

    //todo no use of reviewedByDTO?
    /**
     * Adds a review to a submission.
     *          <p>
     * The review will always be added to the latest paper. It has to be submitted
     * within the given timeline and one reviewer can onl submit one review per paper.
     *          </p>
     *
     * @param submission The submission to which the review is submitted.
     * @param review The review that is submitted.
     * @param file The file containing the pdf review itself.
     * @param reviewer The reviewer attempting to submit.
     */
    public void addReview(Submission submission, Review review, File file, User reviewer) {

    }

    /**
     * Removes a review.
     *
     * @param review The review that is to be removed.
     */
    public void removeReview(Review review) {
    }

    /**
     * Gets all reviews that can be viewed by a given user.
     * For example, a reviewer can only review his own reviews.
     *
     * @param submission The submission from which the reviews are requested.
     * @param user The user who
     * @return
     */
    public List<Review> getReviews(Submission submission, User user) {
        return null;
    }

    /**
     * Gets the file containing the reviews pdf.
     *
     * @param review The review containing the requested file.
     * @return The requested file.
     */
    public File downloadFile(Review review) {
        return null;
    }
}
