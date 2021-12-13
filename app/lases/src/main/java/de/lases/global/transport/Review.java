package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Represents a review.
 */
public class Review implements Cloneable {

    private int paperVersion;

    private int submissionId;

    private int reviewerId;

    private LocalDateTime uploadTime;

    private boolean visible;

    private boolean acceptPaper;

    private String comment;

    public int getPaperVersion() {
        return paperVersion;
    }

    /**
     * Set the id of the paper this review belongs to.
     *
     * @param paperVersion Id of the paper.
     */
    public void setPaperVersion(int paperVersion) {
        this.paperVersion = paperVersion;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    /**
     * Set the id of the submission this review belongs to.
     *
     * @param submissionId Id of the submission.
     */
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    /**
     * Set the id of the user that wrote this review.
     *
     * @param reviewerId The list of the user that wrote this review.
     */
    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    /**
     * Set the timestamp that states when this review was uploaded.
     *
     * @param uploadTime Timestamp that states when this review was
     *                           uploaded.
     */
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Set the visibility of the review for the submitter of the paper this
     * review belongs to.
     *
     * @param visible The visibility of the review.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAcceptPaper() {
        return acceptPaper;
    }

    /**
     * Set the acceptance advice the reviewer gave for the paper along with
     * this review.
     *
     * @param acceptPaper The acceptance advice from the reviewer.
     */
    public void setAcceptPaper(boolean acceptPaper) {
        this.acceptPaper = acceptPaper;
    }

    public String getComment() {
        return comment;
    }

    /**
     * Set the comment the reviewer gave along with the review.
     *
     * @param comment The comment from the reviewer.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public Review clone() {
        try {
            Review clone = (Review) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Check equality by comparing reviewer id, paper version and submission id.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this review?
     */
    @Override
    public boolean equals(Object object) {
        return false;
    }
}
