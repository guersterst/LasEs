package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Stores Information about the review-request relationship of a Submission and
 * a User (the reviewer).
 */
public class ReviewedBy {

    private int reviewerId;

    private int submissionId;

    private LocalDateTime timestampDeadline;

    private AcceptanceStatus hasAccepted;

    public LocalDateTime getTimestampDeadline() {
        return timestampDeadline;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    /**
     * Set the id of the (designated) reviewer.
     *
     * @param reviewerId Id of the reviewer.
     */
    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    /**
     * Set the id of the submission
     *
     * @param submissionId Id of the submission.
     */
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * Set the deadline the (designated) reviewer has to answer to the review
     * request.
     *
     * @param timestampDeadline Deadline for the reviewer to answer the
     *                          request.
     */
    public void setTimestampDeadline(LocalDateTime timestampDeadline) {
        this.timestampDeadline = timestampDeadline;
    }

    public AcceptanceStatus getHasAccepted() {
        return hasAccepted;
    }

    /**
     * Set the acceptance state of the (designated) reviewer. For reference for
     * the possible options see {@link AcceptanceStatus}.
     *
     * @param hasAccepted The acceptance state of the reviewer.
     */
    public void setHasAccepted(AcceptanceStatus hasAccepted) {
        this.hasAccepted = hasAccepted;
    }

}
