package de.lases.global.transport;

import java.time.LocalDateTime;

public class ReviewedBy {

    private User reviewer;

    private Submission submission;

    private LocalDateTime timestampDeadline;

    private AcceptanceStatus hasAccepted;

    public LocalDateTime getTimestampDeadline() {
        return timestampDeadline;
    }
}
