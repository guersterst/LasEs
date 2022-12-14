package de.lases.global.transport;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a paper.
 *
 * @author Sebastian Vogt
 */
public class Paper implements Cloneable {

    private Integer submissionId;

    private Integer versionNumber;

    private LocalDateTime uploadTime;

    private boolean visible;

    public Integer getSubmissionId() {
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

    public Integer getVersionNumber() {
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

            /*
             * Nothing to do here, since all references of Paper are
             * immutable.
             */
            return (Paper) super.clone();
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
        if (object == this) {
            return true;
        }
        if (!(object instanceof Paper paper)) {
            return false;
        }
        return Objects.equals(paper.versionNumber, this.versionNumber)
                && Objects.equals(paper.getSubmissionId(), this.getSubmissionId());
    }
}
