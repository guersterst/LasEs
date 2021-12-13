package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Stores information about the review-request relationship of a submission and
 * a user (the reviewer).
 */
public class ReviewedBy implements Cloneable {

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
     * Set the deadline the (designated) reviewer has to review.
     *
     * @param timestampDeadline Deadline for the reviewer to review.
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

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public ReviewedBy clone() {
        try {
            ReviewedBy clone = (ReviewedBy) super.clone();
            /*
             * Nothing to do here, since all references of Submission are
             * immutable.
             */
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Check equality by comparing reviewerId and submissionId.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this ReviewedBy?
     */
    @Override
    public boolean equals(Object object) {
        return false;
    }
}
