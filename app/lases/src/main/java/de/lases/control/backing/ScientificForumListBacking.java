package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.service.ScientificForumService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import de.lases.persistence.internal.ConfigReader;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.PropertyResourceBundle;

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

    @Inject
    ConfigPropagator config;

    private Pagination<ScientificForum> scientificForumPagination;

    /**
     * Initialize the scientific forum pagination and load the first page from
     * the datasource.
     */
    @PostConstruct
    public void init() {
        scientificForumPagination = new Pagination<>("title") {

            @Override
            public void loadData() {
                scientificForumPagination.getResultListParameters().setDateSelect(DateSelect.ALL);
                scientificForumPagination.setEntries(scientificForumService.getList(getResultListParameters()));
            }


            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(config.getProperty("MAX_PAGINATION_LIST_LENGTH"));

                return (int) Math.ceil((double) scientificForumService.getList(this.getResultListParameters()).size()
                        / itemsPerPage);
            }
        };
        scientificForumPagination.applyFilters();
        scientificForumPagination.loadData();
    }

    /**
     * Get the pagination for the search results with scientific forums.
     *
     * @return The pagination for scientific forums.
     */
    public Pagination<ScientificForum> getScientificForumPagination() {
        return scientificForumPagination;
    }

    public void setScientificForumPagination(Pagination<ScientificForum> scientificForumPagination) {
        this.scientificForumPagination = scientificForumPagination;
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
