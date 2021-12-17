package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.control.internal.Pagination;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.DateSelect;
import de.lases.global.transport.ScientificForum;
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

    private Pagination<ScientificForum> scientificForumPagination;

    private void initPagination() {
        scientificForumPagination = new Pagination<>("name") {

            @Override
            public void loadData() {
                setEntries(scientificForumService.getList(getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                return scientificForumService.getListCountPages(scientificForumPagination.getResultListParameters());
            }
        };
        scientificForumPagination.applyFilters();
        scientificForumPagination.loadData();
    }

    /**
     * Initializes the pagination.
     */
    @PostConstruct
    public void init() {
        initPagination();
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

    /**
     * Return an array of all values the DateSelect enum can have.
     *
     * @return ALl options of DateSelect.
     */
    public DateSelect[] getDateSelects() {
        return DateSelect.values();
    }
}
