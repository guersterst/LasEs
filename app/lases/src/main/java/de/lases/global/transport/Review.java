package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Represents a review.
 */
public class Review {

    private int paperId;

    private int submissionid;

    private int reviewerId;

    private LocalDateTime timestampUpdloaded;

    private boolean visible;

    private boolean acceptPaper;

    private String comment;

    public int getPaperId() {
        return paperId;
    }

    /**
     * Set the id of the paper this review belongs to.
     *
     * @param paperId Id of the paper.
     */
    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public int getSubmissionid() {
        return submissionid;
    }

    /**
     * Set the id of the submission this review belongs to.
     *
     * @param submissionid Id of the submission.
     */
    public void setSubmissionid(int submissionid) {
        this.submissionid = submissionid;
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

    public LocalDateTime getTimestampUpdloaded() {
        return timestampUpdloaded;
    }

    /**
     * Set the timestamp that states when this review was uploaded.
     *
     * @param timestampUpdloaded Timestamp that states when this review was
     *                           uploaded.
     */
    public void setTimestampUpdloaded(LocalDateTime timestampUpdloaded) {
        this.timestampUpdloaded = timestampUpdloaded;
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
}
