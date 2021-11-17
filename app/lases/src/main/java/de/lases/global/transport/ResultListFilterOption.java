package de.lases.global.transport;

/**
 * A single comparison that can be used to construct a ResultListFilter.
 */
public class ResultListFilterOption {

    private ResultListFilterComparator comparator;

    private String comparand;

    public ResultListFilterComparator getComparator() {
        return comparator;
    }

    /**
     * Set the comparator, which is an operation that compares two values. For
     * reference, see {@link ResultListFilterComparator}
     *
     * @param comparator
     */
    public void setComparator(ResultListFilterComparator comparator) {
        this.comparator = comparator;
    }

    public String getComparand() {
        return comparand;
    }

    /**
     * Set the value that should be on the right side of the comparison.
     *
     * @param comparand The comparand.
     */
    public void setComparand(String comparand) {
        this.comparand = comparand;
    }
}
