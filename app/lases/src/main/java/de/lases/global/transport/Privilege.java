package de.lases.global.transport;

/**
 * Represents a user privilege.
 */
public enum Privilege {
    /**
     * An administrator can do anything in the system.
     */
    ADMIN,

    /**
     * An editor has wide-ranging permissions for the submissions and forums
     * he edits. He can for example choose reviewers and set reviews and
     * revisions to visible.
     */
    EDITOR,

    /**
     * A reviewer is a user that can review submissions he is assigned to.
     */
    REVIEWER,

    /**
     * A user that is authenticated in the system.
     */
    AUTHENTICATED,

    /**
     * A user that is an author or a co-author to a certain submission.
     */
    AUTHOR
}
