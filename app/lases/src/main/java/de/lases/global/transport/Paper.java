package de.lases.global.transport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a paper.
 */
public class Paper {

    private int submissionId;

    private int id;

    private LocalDateTime uploadTime;

    private boolean visible;

    public int getSubmissionId() {
        return submissionId;
    }

    /**
     * Set the id of the submission this paper belongs to.
     *
     * @param submissionId Id of the parent submission.
     */
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getId() {
        return id;
    }

    /**
     * Set the id of this paper.
     *
     * @param id The id of this paper.
     */
    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    /**
     * Set the time when this paper was uploaded.
     *
     * @param uploadTime Time when this paper was uploaded.
     */
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Set visibility of the paper for reviewer.
     *
     * @param visible Visibility of the paper.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
