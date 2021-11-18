package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;

/**
 * Backing bean for the result list.
 */
@ViewScoped
@Named
public class ResultListBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -111481772034202599L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private UserService userService;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private DateSelect dateFilterSelectSub;

    private DateSelect dateFilterSelectEdit;

    private DateSelect dateFilterSelectReview;

    private DateSelect dateFilterSelectForum;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private Pagination<User> userPagination;

    private Pagination<ScientificForum> scientificForumPagination;

    /**
     * Show the first page of search results for each pagination.
     */
    @PostConstruct
    public void init() {
    }

    public SubmissionState getStateFilterSelectSub() {
        return stateFilterSelectSub;
    }

    /**
     * Set the {@code SubmissionState} which the user wants to filter for the
     * table with his own submissions.
     *
     * @param stateFilterSelectSub The submission state the user wants to filter
     *                             after.
     */
    public void setStateFilterSelectSub(SubmissionState stateFilterSelectSub) {
        this.stateFilterSelectSub = stateFilterSelectSub;
    }

    public SubmissionState getStateFilterSelectEdit() {
        return stateFilterSelectEdit;
    }

    /**
     * Set the {@code SubmissionState} which the user wants to filter for the
     * table with his edited submissions.
     *
     * @param stateFilterSelectEdit The submission state the user wants to
     *                              filter after.
     */
    public void setStateFilterSelectEdit(SubmissionState stateFilterSelectEdit) {
        this.stateFilterSelectEdit = stateFilterSelectEdit;
    }

    public SubmissionState getStateFilterSelectReview() {
        return stateFilterSelectReview;
    }

    /**
     * Set the {@code SubmissionState} which the user wants to filter for the
     * table with his reviewed submissions.
     *
     * @param stateFilterSelectReview The submission state the user wants to
     *                                filter after.
     */
    public void setStateFilterSelectReview(
            SubmissionState stateFilterSelectReview) {
        this.stateFilterSelectReview = stateFilterSelectReview;
    }

    public DateSelect getDateFilterSelectSub() {
        return dateFilterSelectSub;
    }

    /**
     * Set the {@code DateSelect} which the user wants to filter for the
     * table with his own submissions.
     *
     * @param dateFilterSelectSub The date filter option the user wants to
     *                             filter after.
     */
    public void setDateFilterSelectSub(DateSelect dateFilterSelectSub) {
        this.dateFilterSelectSub = dateFilterSelectSub;
    }

    public DateSelect getDateFilterSelectEdit() {
        return dateFilterSelectEdit;
    }

    /**
     * Set the {@code DateSelect} which the user wants to filter for the
     * table with his edited submissions.
     *
     * @param dateFilterSelectEdit The date filter option the user wants to
     *                             filter after.
     */
    public void setDateFilterSelectEdit(DateSelect dateFilterSelectEdit) {
        this.dateFilterSelectEdit = dateFilterSelectEdit;
    }

    public DateSelect getDateFilterSelectReview() {
        return dateFilterSelectReview;
    }

    /**
     * Set the {@code DateSelect} which the user wants to filter for the
     * table with his reviewed submissions.
     *
     * @param dateFilterSelectReview The date filter option the user wants to
     *                             filter after.
     */
    public void setDateFilterSelectReview(DateSelect dateFilterSelectReview) {
        this.dateFilterSelectReview = dateFilterSelectReview;
    }

    public DateSelect getDateFilterSelectForum() {
        return dateFilterSelectForum;
    }

    /**
     * Set the {@code DateSelect} which the user wants to filter for the
     * table with scientific forums.
     *
     * @param dateFilterSelectForum The date filter option the user wants to
     *                             filter after.
     */
    public void setDateFilterSelectForum(DateSelect dateFilterSelectForum) {
        this.dateFilterSelectForum = dateFilterSelectForum;
    }

    /**
     * Get the pagination for the search results with submissions submitted by
     * the user.
     *
     * @return The pagination for the submission submitted by the user.
     */
    public Pagination<Submission> getSubmissionPagination() {
        return submissionPagination;
    }

    /**
     * Get the pagination for the search results with submissions reviewed by
     * the user.
     *
     * @return The pagination for the submission reviewed by the user.
     */
    public Pagination<Submission> getReviewedPagination() {
        return reviewedPagination;
    }

    /**
     * Get the pagination for the search results with submissions edited by
     * the user.
     *
     * @return The pagination for the submission edited by the user.
     */
    public Pagination<Submission> getEditedPagination() {
        return editedPagination;
    }

    /**
     * Get the pagination for the search results with users.
     *
     * @return The pagination for users.
     */
    public Pagination<User> getUserPagination() {
        return userPagination;
    }

    /**
     * Get the pagination for the search results with scientific forums.
     *
     * @return The pagination for scientific forums.
     */
    public Pagination<ScientificForum> getScientificForumPagination() {
        return scientificForumPagination;
    }
}
