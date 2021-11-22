package de.lases.control.backing;

import de.lases.business.service.ScienceFieldService;
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
import java.util.Date;
import java.util.List;

/**
 * Backing bean for the scientific forum page.
 */
@ViewScoped
@Named
public class ScientificForumBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -729646507861237388L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionListService;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private ScienceFieldService scienceFieldService;

    @Inject
    private UserService userService;

    private ScientificForum scientificForum;

    private User newEditorInput;

    private ScienceField selectedScienceFieldInput;

    private SubmissionState stateFilterSelectSub;

    private SubmissionState stateFilterSelectEdit;

    private SubmissionState stateFilterSelectReview;

    private DateSelect dateFilterSelectSub;

    private DateSelect dateFilterSelectReview;

    private DateSelect dateFilterSelectEdit;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private List<User> editors;

    private List<ScienceField> selectedScieneFields;

    private List<ScienceField> scienceFields;

    /**
     * Initialized the dtos.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Gets the id of the scientific forum in question from view params and
     * loads the data needed to display the page.
     */
    public void onLoad() { }

    /**
     * Delete the scientific forum and got to the homepage.
     *
     * @return Go to the homepage.
     */
    public String deleteForum() {
        return null;
    }

    /**
     * Add the currently selected user to the list of editors.
     */
    public void addEditor() {
    }

    /**
     * Add the currently selected scienceField to the list of science fields.
     */
    public void addScienceField() {
    }

    /**
     * Remove a specific user form the list of editors.
     *
     * @param user User to remove from editor list.
     */
    public void removeEditor(User user) {
    }

    /**
     * Remove a specific science field from the list of science fields.
     *
     * @param scienceField Field to remove from the list.
     */
    public void removeScienceField(ScienceField scienceField) {
    }

    /**
     * Submit all changes made to the forum that have nothing to do with the
     * editor or scientific forum list.
     */
    public void submitChanges() {
    }

    /**
     * Check if the current viewer of the page is an editor of this forum.
     * Based on users being compared by ids. (Might be different objects).
     * @return true if viewer is a managing editor.
     */
    public boolean isViewerEditor() {
        return editors.contains(sessionInformation.getUser());
    }

    public ScientificForum getScientificForum() {
        return scientificForum;
    }

    /**
     * Set the dto that represents this scientific forum.
     *
     * @param scientificForum The new scientific forum.
     */
    public void setScientificForum(ScientificForum scientificForum) {
        this.scientificForum = scientificForum;
    }

    public User getNewEditorInput() {
        return newEditorInput;
    }

    /**
     * Set the user that is selected to be added as an editor.
     *
     * @param newEditorInput The user that ought to become an editor on submit.
     */
    public void setNewEditorInput(User newEditorInput) {
        this.newEditorInput = newEditorInput;
    }

    public ScienceField getSelectedScienceFieldInput() {
        return selectedScienceFieldInput;
    }

    /**
     * Set the science field that is selected to be added as a science field.
     *
     * @param selectedScienceFieldInput The science field that ought to be
     *                                  added on submit.
     */
    public void setSelectedScienceFieldInput(
            ScienceField selectedScienceFieldInput) {
        this.selectedScienceFieldInput = selectedScienceFieldInput;
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
     * Get the pagination for the submissions edited by the user.
     *
     * @return The pagination for the submission edited by the user.
     */
    public Pagination<Submission> getEditedPagination() {
        return editedPagination;
    }

    /**
     * Get the list of editors belonging to this forum.
     *
     * @return The list of editors belonging to this forum.
     */
    public List<User> getEditors() {
        return editors;
    }

    /**
     * Get the list of science fields belonging to this forum.
     *
     * @return The list of science fields belonging to this forum.
     */
    public List<ScienceField> getSelectedScieneFields() {
        return selectedScieneFields;
    }

    /**
     * Get the list of global science fields.
     *
     * @return The list of science fields.
     */
    public List<ScienceField> getScienceFields() {
        return scienceFields;
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
