package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;

/**
 * Backing bean for the scientific forum list page.
 */
@ViewScoped
@Named
public class ScientificForumListBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = 288940091897301232L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private ScientificForumService scientificForumService;

    private DateSelect deadlineFilterSelect;

    private Pagination<ScientificForum> scientificForumPagination;

    /**
     * Initialize the list by getting the first page from the data source.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Apply the selected deadline filter.
     */
    public void applyFilters() {
    }

    public DateSelect getDeadlineFilterSelect() {
        return deadlineFilterSelect;
    }

    /**
     * Set the selected filter option for filtering forums after deadline
     *
     * @param dateFilterSelectSub The selected filter option for filtering
     *                            forums after deadline.
     */
    public void setDeadlineFilterSelect(DateSelect deadlineFilterSelect) {
        this.deadlineFilterSelect = deadlineFilterSelect;
    }

    /**
     * Get the pagination for the search results with scientific forums.
     *
     * @return The pagination for scientific forums.
     */
    public Pagination<ScientificForum> getScientificForumPagination() {
        return scientificForumPagination;
    }

    /**
     * Get session information.
     *
     * @return The session information.
     */
    public SessionInformation getSessionInformation() {
        return sessionInformation;
    }

}
