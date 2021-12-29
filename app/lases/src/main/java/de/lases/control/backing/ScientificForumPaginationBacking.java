package de.lases.control.backing;

import de.lases.control.internal.Pagination;
import de.lases.global.transport.DateSelect;
import de.lases.global.transport.ScientificForum;

public interface ScientificForumPaginationBacking {

    /**
     * Get the pagination for the search results with scientific forums.
     *
     * @return The pagination for scientific forums.
     */
    Pagination<ScientificForum> getScientificForumPagination();

    /**
     * Return an array of all values the DateSelect enum can have.
     *
     * @return ALl options of DateSelect.
     */
    default DateSelect[] getDateSelects() {
        return DateSelect.values();
    }
}
