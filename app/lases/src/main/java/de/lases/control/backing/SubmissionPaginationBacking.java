package de.lases.control.backing;

import de.lases.control.internal.Pagination;
import de.lases.global.transport.DateSelect;
import de.lases.global.transport.Submission;
import de.lases.global.transport.SubmissionState;

public interface SubmissionPaginationBacking {

    /**
     * Get the pagination for the submissions submitted by the user.
     *
     * @return The pagination for the submissions submitted by the user.
     */
    Pagination<Submission> getSubmissionPagination();

    /**
     * Get the options of the DateSelect enum as an array.
     *
     * @return Array of DateSelect.
     */
    default DateSelect[] getDateSelects() {
        return DateSelect.values();
    }

    /**
     * Get the options of the SubmissionState enum as an array.
     *
     * @return Array of SubmissionState.
     */
    default SubmissionState[] getSubmissionStates() {
        return SubmissionState.values();
    }

    /**
     * Get the name of the forum the provided submission is part of.
     *
     * @param sub The submission to which the forum name should be received.
     * @return The name of the scientific forum the given submission was submitted into.
     */
    String getForumName(Submission sub);
}
