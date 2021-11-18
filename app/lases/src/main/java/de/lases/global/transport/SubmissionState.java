package de.lases.global.transport;

/**
 * Represents a submission state.
 */
public enum SubmissionState {

    /**
     * The submission was submitted and its state was modified no further.
     */
    SUBMITTED,

    /**
     * The submission can still be accepted, but a revision is required.
     */
    REVISION_REQUIRED,

    /**
     * The submission was rejected and will not be further considered.
     */
    REJECTED,

    /**
     * The submission was accepted as is.
     */
    ACCEPTED;
}
