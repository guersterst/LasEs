package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.control.internal.Pagination;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;

/**
 * Backing bean for the homepage.
 */
@ViewScoped
@Named
public class HomepageBacking implements SubmissionPaginationBacking {

    @Serial
    private static final long serialVersionUID = -3666609342938323378L;



    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW;
    }
    @Inject
    private SubmissionService submissionService;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private ConfigPropagator configPropagator;

    private Tab tab;

    private Pagination<Submission> submissionPagination;

    private User user;

    private DateSelect submissionDateSelect;

    private DateSelect deadlineDateSelect;

    /**
     * Initialize the dtos and load data from the datasource where possible.
     * Create objects for:
     * <ul>
     *     <li>
     *         the current tab the user is on.
     *     </li>
     *     <li>
     *         the own submission pagination.
     *     </li>
     *     <li>
     *         the reviewed submission pagination.
     *     </li>
     *     <li>
     *         the edited submission pagination.
     *     </li>
     * </ul>
     * Load the following data from the datasource:
     * <ul>
     *     <li>
     *         the first page of the own submission pagination.
     *     </li>
     *     <li>
     *         the fist page of the reviewed submission pagination.
     *     </li>
     *     <li>
     *         the first page of the edited submission pagination.
     *     </li>
     * </ul>
     */
    @PostConstruct
    public void init() {
        user = sessionInformation.getUser();
        showOwnSubmissionsTab();
    }

    /**
     * Switch to the tab that shows the user's own submissions.
     */
    public void showOwnSubmissionsTab() {
        submissionPagination = new Pagination<>("title") {
            @Override
            public void loadData() {
                setEntries(submissionService.getList(
                        Privilege.AUTHOR, user, getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(configPropagator.getProperty("MAX_PAGINATION_LIST_LENGTH"));
                return (int) Math.ceil((double) submissionService.countSubmissions(user, Privilege.AUTHOR,
                        getResultListParameters()) / itemsPerPage);
            }
        };
        tab = Tab.OWN_SUBMISSIONS;
        submissionPagination.loadData();
    }

    /**
     * Switch to the tab that shows the submissions where the user is an
     * editor.
     */
    public void showSubmissionsToEditTab() {
        submissionPagination = new Pagination<>("title") {
            @Override
            public void loadData() {
                setEntries(submissionService.getList(
                        Privilege.EDITOR, user, getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(configPropagator.getProperty("MAX_PAGINATION_LIST_LENGTH"));
                return (int) Math.ceil((double) submissionService.countSubmissions(user, Privilege.EDITOR,
                        getResultListParameters()) / itemsPerPage);
            }
        };
        tab = Tab.SUBMISSIONS_TO_EDIT;
        submissionPagination.loadData();
    }

    /**
     * Switch to the tab that shows the submissions where the user is a
     * reviewer.
     */
    public void showSubmissionsToReviewTab() {
        submissionPagination = new Pagination<>("title") {
            @Override
            public void loadData() {
                setEntries(submissionService.getList(
                        Privilege.REVIEWER, user, getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(configPropagator.getProperty("MAX_PAGINATION_LIST_LENGTH"));
                return (int) Math.ceil((double) submissionService.countSubmissions(user, Privilege.REVIEWER,
                        getResultListParameters()) / itemsPerPage);
            }
        };
        tab = Tab.SUBMISSIONS_TO_REVIEW;
        submissionPagination.loadData();
    }

    /**
     * Apply the selected date and state filters.
     */
    public void applyFilters() {
    }


    /**
     * Get the tab the user is on.
     *
     * @return The tab the user is on.
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * Set the tab the user is on.
     *
     * @param tab the tab the user is on.
     */
    public void setTab(Tab tab) {
        this.tab = tab;
    }

    /**
     * Get the pagination for the submissions submitted by the user.
     *
     * @return The pagination for the submissions submitted by the user.
     */
    @Override
    public Pagination<Submission> getSubmissionPagination() {
        return submissionPagination;
    }

    /**
     * Get the name of the forum the provided submission is part of.
     *
     * @param sub The submission to which the forum name should be received.
     * @return The name of the scientific forum the given submission was submitted into.
     */
    @Override
    public String getForumName(Submission sub) {
        ScientificForum forum = new ScientificForum();
        forum.setId(sub.getScientificForumId());
        return scientificForumService.get(forum).getName();
    }

    public String getOwnCssClassSuffix() {
        return tab == Tab.OWN_SUBMISSIONS ? " active" : "";
    }

    public String getReviewCssClassSuffix() {
        return tab == Tab.SUBMISSIONS_TO_REVIEW ? " active" : "";
    }

    public String getEditCssClassSuffix() {
        return tab == Tab.SUBMISSIONS_TO_EDIT ? " active" : "";
    }

}
