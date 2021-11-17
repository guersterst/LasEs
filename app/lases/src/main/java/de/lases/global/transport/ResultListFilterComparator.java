package de.lases.global.transport;

/**
 * The four comparators equal to, less than or equal, greater than or equal and
 * like.
 */
public enum ResultListFilterComparator {

    /**
     * Equals.
     */
    EQ,

    /**
     * Greater than or equal.
     */
    GTE,

    /**
     * Less than or equal.
     */
    LTE,

    /**
     * This represents an operator like the SQL 'LIKE' operator (but the
     * implementation of the usage is not locked to SQL of course).
     */
    LIKE;
}
