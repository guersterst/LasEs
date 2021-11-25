package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Represents a paper.
 */
public class Paper implements Cloneable {

    private int submissionId;

    private int versionNumber;

    private LocalDateTime uploadTime;

    private boolean visible;

    public int getSubmissionId() {
        return submissionId;
    }

    /**
     * Set the versionNumber of the submission this paper belongs to.
     *
     * @param submissionId Id of the parent submission.
     */
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    /**
     * Set the versionNumber of this paper.
     *
     * @param versionNumber The versionNumber of this paper.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
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

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public Paper clone() {
        try {
            Paper clone = (Paper) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Check equality by comparing version and submission id.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this paper?
     */
    @Override
    public boolean equals(Object object) {
        return false;
    }
}
