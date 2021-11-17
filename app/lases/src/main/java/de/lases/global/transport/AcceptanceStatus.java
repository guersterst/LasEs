package de.lases.global.transport;

/**
 * Holds the information weather a requested reviewer has accepted or rejected
 * the request or has yet to decide.
 */
public enum AcceptanceStatus {

    /**
     * The reviewer has yet to decide.
     */
    PENDING,

    /**
     * The reviewer accepted.
     */
    ACCEPTED,

    /**
     * The reviewer rejected.
     */
    REJECTED;
}
