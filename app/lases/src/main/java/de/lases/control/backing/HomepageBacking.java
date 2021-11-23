package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
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
public class HomepageBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -3666609342938323378L;

    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
    }

    @Inject
    private SubmissionService submissionService;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private ScientificForumService scientificForumService;

    private Tab tab;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    /**
     * Initialize the dtos and load data from the datasource where possible.
     * Create objects for:
     * <ul>
     *     <li>
     *         the date filter option for own submissions
     *     </li>
     *     <li>
     *         the date filter option for reviewed submissions
     *     </li>
     *     <li>
     *         the date filter option for edited submissions
     *     </li>
     *     <li>
     *         the submission state filter option for own submissions
     *     </li>
     *     <li>
     *         the submission state filter for reviewed submissions
     *     </li>
     *     <li>
     *         the submission state filter for edited submissions
     *     </li>
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
     *         the reviewed submission pagination.
     *     </li>
     *     <li>
     *         The result list parameters.
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
     *         the first page of the reviewed submission pagination.
     *     </li>
     * </ul>
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Switch to the tab that shows the user's own submissions.
     */
    public void showOwnSubmissionsTab() {
    }

    /**
     * Switch to the tab that shows the submissions where the user is an
     * editor.
     */
    public void showSubmissionsToEditTab() {
    }

    /**
     * Switch to the tab that shows the submissions where the user is a
     * reviewer.
     */
    public void showSubmissionsToReviewTab() {
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
     * Get the pagination for the submissions edited by the user.
     *
     * @return The pagination for the submission edited by the user.
     */
    public Pagination<Submission> getEditedPagination() {
        return editedPagination;
    }

    /**
     * Get the pagination for the submissions submitted by the user.
     *
     * @return The pagination for the submissions submitted by the user.
     */
    public Pagination<Submission> getSubmissionPagination() {
        return submissionPagination;
    }

    /**
     * Get the pagination for the submissions reviewed by the user.
     *
     * @return The pagination for the submissions reviewed by the user.
     */
    public Pagination<Submission> getReviewedPagination() {
        return reviewedPagination;
    }

    /**
     * Get the options of the DateSelect enum as an array.
     *
     * @return Array of DateSelect.
     */
    public DateSelect[] getDateSelects() {
        return DateSelect.values();
    }

    /**
     * Get the options of the SubmissionState enum as an array.
     *
     * @return Array of SubmissionState.
     */
    public SubmissionState[] getSubmissionStates() {
        return SubmissionState.values();
    }


}
