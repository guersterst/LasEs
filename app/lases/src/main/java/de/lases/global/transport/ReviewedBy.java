package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Stores Information about the relationship of a Submission and a User
 * (the reviewer).
 */
public class ReviewedBy {

    private User reviewer;

    private Submission submission;

    private LocalDateTime timestampDeadline;

    private AcceptanceStatus hasAccepted;

    public LocalDateTime getTimestampDeadline() {
        return timestampDeadline;
    }
}
