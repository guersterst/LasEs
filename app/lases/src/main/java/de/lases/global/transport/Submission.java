package de.lases.global.transport;

import java.time.LocalDateTime;

/**
 * Represents a submission.
 */
public class Submission implements Cloneable {

    private int scientificForumId;

    private Integer id;

    private int authorId;

    private int editorId;

    private String title;

    private SubmissionState state;

    private LocalDateTime deadlineRevision;

    private LocalDateTime submissionTime;

    private boolean revisionRequired;

    public int getScientificForumId() {
        return scientificForumId;
    }

    /**
     * Set the id of the scientific forum this submission belongs to.
     *
     * @param scientificForumId The id of the scientific forum.
     */
    public void setScientificForumId(int scientificForumId) {
        this.scientificForumId = scientificForumId;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Set the id of this submission.
     * Integer is used instead of int in order to be able to determine if the property is set.
     *
     * @param id The id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isRevisionRequired() {
        return revisionRequired;
    }

    /**
     * Set if a revision is required for this submission.
     *
     * @param revisionRequired Is a revision required?
     */
    public void setRevisionRequired(boolean revisionRequired) {
        this.revisionRequired = revisionRequired;
    }

    public int getAuthorId() {
        return authorId;
    }

    /**
     * Set the id of the user that authored this submission.
     *
     * @param authorId Id of the author.
     */
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getEditorId() {
        return editorId;
    }

    /**
     * Set the id of the user that is the editor for this submission.
     *
     * @param editorId Id of the editor.
     */
    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Set the title of this submission.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public SubmissionState getState() {
        return state;
    }

    /**
     * Set the acceptance state of this submission. For all possible states
     * refer to {@link SubmissionState}
     *
     * @param state The state of the submission.
     */
    public void setState(SubmissionState state) {
        this.state = state;
    }

    public LocalDateTime getDeadlineRevision() {
        return deadlineRevision;
    }

    /**
     * Set the deadline for the submission of a revision
     *
     * @param deadlineRevision Deadline for a revision.
     */
    public void setDeadlineRevision(LocalDateTime deadlineRevision) {
        this.deadlineRevision = deadlineRevision;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    /**
     * Set the time when the submission was submitted.
     *
     * @param submissionTime Submission time.
     */
    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public Submission clone() {
        try {
            Submission clone = (Submission) super.clone();

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
     * Check equality by comparing ids.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this submission?
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Submission)) {
            return false;
        }
        Submission submission = (Submission) object;
        return submission.getId().equals(this.getId());
    }
}
