package de.lases.business.service;

import de.lases.global.transport.Review;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
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

    public void addReview(Submission submission, Review review) {
    }

    public void removeReview(Submission submission, int reviewId, int paperId) {
    }

    public List<Review> getReviews(Submission submission, User user) {
        return null;
    }

    public void uploadFile(byte[] pdf) {
    }

    public byte[] downloadFile() {
        return null;
    }
}
