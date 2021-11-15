package de.lases.control.backing;

import de.lases.business.service.SubmissionService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named
public class HomepageBacking {


    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
    }

    private Tab tab;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;


    private ResultListParameters resultListParameters;

    private SessionInformation sessionInformation;

    private SubmissionService submissionService;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private DateSelect submission;

    private DateSelect reviewing;

    private DateSelect editorial;

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

}
