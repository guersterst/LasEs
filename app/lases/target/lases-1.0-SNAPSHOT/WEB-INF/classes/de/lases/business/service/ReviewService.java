package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.component.UIMessage;
import jakarta.inject.Named;

import java.util.List;

@ApplicationScoped
@Named
public class ReviewService {

    private Event<UIMessage> uiMessageEvent;

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
