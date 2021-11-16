package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Backing bean for the homepage.
 */
@ViewScoped
@Named
public class HomepageBacking implements Serializable {

    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
    }

    @Inject
    private SubmissionService submissionService;

    @Inject
    private SessionInformation sessionInformation;



    private DateSelect dateFilterSelectSub;

    private DateSelect dateFilterSelectReview;

    private DateSelect dateFilterSelectEdit;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private Tab tab;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private ResultListParameters resultListParameters;

    /**
     * Load submissions from the database for the first time to fill the first
     * page of the submission lists.
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


    public DateSelect getDateFilterSelectSub() {
        return dateFilterSelectSub;
    }

    /**
     * Set the selected filter option for filtering own submissions after
     * date.
     *
     * @param dateFilterSelectSub The selected filter option for filtering
     *                            own submissions after date.
     */
    public void setDateFilterSelectSub(DateSelect dateFilterSelectSub) {
        this.dateFilterSelectSub = dateFilterSelectSub;
    }

    public DateSelect getDateFilterSelectReview() {
        return dateFilterSelectReview;
    }

    /**
     * Set the selected filter option for filtering reviewed submissions after
     * date.
     *
     * @param dateFilterSelectReview The selected filter option for filtering
     *                               reviewed submissions after date.
     */
    public void setDateFilterSelectReview(DateSelect dateFilterSelectReview) {
        this.dateFilterSelectReview = dateFilterSelectReview;
    }

    public DateSelect getDateFilterSelectEdit() {
        return dateFilterSelectEdit;
    }

    /**
     * Set the selected filter option for filtering edited submissions after
     * date.
     *
     * @param dateFilterSelectEdit The selected filter option for filtering
     *                             edited submissions after date.
     */
    public void setDateFilterSelectEdit(DateSelect dateFilterSelectEdit) {
        this.dateFilterSelectEdit = dateFilterSelectEdit;
    }

    public SubmissionState getStateFilterSelectSub() {
        return stateFilterSelectSub;
    }

    /**
     * Set the selected filter option for filtering own submissions after
     * the submission status.
     *
     * @param stateFilterSelectSub The selected filter option for filtering
     *                             own submissions after submission status.
     */
    public void setStateFilterSelectSub(SubmissionState stateFilterSelectSub) {
        this.stateFilterSelectSub = stateFilterSelectSub;
    }

    public SubmissionState getStateFilterSelectEdit() {
        return stateFilterSelectEdit;
    }

    /**
     * Set the selected filter option for filtering edited submissions after
     * the submission status.
     *
     * @param stateFilterSelectEdit The selected filter option for filtering
     *                              edited submissions after submission status.
     */
    public void setStateFilterSelectEdit(
            SubmissionState stateFilterSelectEdit) {
        this.stateFilterSelectEdit = stateFilterSelectEdit;
    }

    public SubmissionState getStateFilterSelectReview() {
        return stateFilterSelectReview;
    }

    /**
     * Set the selected filter option for filtering reviewed submissions after
     * the submission status.
     *
     * @param stateFilterSelectReview The selected filter option for filtering
     *                              reviewed submissions after submission
     *                              status.
     */
    public void setStateFilterSelectReview(
            SubmissionState stateFilterSelectReview) {
        this.stateFilterSelectReview = stateFilterSelectReview;
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
}
