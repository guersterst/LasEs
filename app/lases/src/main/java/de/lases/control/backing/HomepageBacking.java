package de.lases.control.backing;

import de.lases.business.service.SubmissionService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the homepage.
 */
@ViewScoped
@Named
public class HomepageBacking {

    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
    }

    @Inject
    private SubmissionService submissionService;

    @Inject
    private SessionInformation sessionInformation;



    private DateSelect submission;

    private DateSelect reviewing;

    private DateSelect editorial;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private Tab tab;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private ResultListParameters resultListParameters;

    @PostConstruct
    public void init() {
    }

    public void showOwnSubmissionsTab() {
    }

    public void showSubmissionsToEditTab() {
    }

    public void showSubmissionsToReviewTab() {
    }

    public void applyFilters() {
    }


    public DateSelect getSubmission() {
        return submission;
    }

    public void setSubmission(DateSelect submission) {
        this.submission = submission;
    }

    public DateSelect getReviewing() {
        return reviewing;
    }

    public void setReviewing(DateSelect reviewing) {
        this.reviewing = reviewing;
    }

    public DateSelect getEditorial() {
        return editorial;
    }

    public void setEditorial(DateSelect editorial) {
        this.editorial = editorial;
    }

    public SubmissionState getStateFilterSelectSub() {
        return stateFilterSelectSub;
    }

    public void setStateFilterSelectSub(SubmissionState stateFilterSelectSub) {
        this.stateFilterSelectSub = stateFilterSelectSub;
    }

    public SubmissionState getStateFilterSelectEdit() {
        return stateFilterSelectEdit;
    }

    public void setStateFilterSelectEdit(SubmissionState stateFilterSelectEdit) {
        this.stateFilterSelectEdit = stateFilterSelectEdit;
    }

    public SubmissionState getStateFilterSelectReview() {
        return stateFilterSelectReview;
    }

    public void setStateFilterSelectReview(SubmissionState stateFilterSelectReview) {
        this.stateFilterSelectReview = stateFilterSelectReview;
    }
}
