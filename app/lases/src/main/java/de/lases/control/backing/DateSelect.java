package de.lases.control.backing;

/**
 * The options on a date filter on data tables.
 */
public enum DateSelect {
    /**
     * Show all dates.
     */
    ALL,

    /**
     * Only show dates that lie in the future.
     */
    FUTURE,

    /**
     * Only show dates that lie in the past.
     */
    PAST;
}
