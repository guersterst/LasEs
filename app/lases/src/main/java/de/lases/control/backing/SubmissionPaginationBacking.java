package de.lases.control.backing;

import de.lases.control.internal.Pagination;
import de.lases.global.transport.DateSelect;
import de.lases.global.transport.Submission;
import de.lases.global.transport.SubmissionState;

import java.io.Serializable;

public interface SubmissionPaginationBacking extends Serializable {
    Pagination<Submission> getSubmissionPagination();

    DateSelect[] getDateSelects();

    SubmissionState[] getSubmissionStates();

    String getForumName(Submission sub);
}
