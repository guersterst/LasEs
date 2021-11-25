package de.lases.control.backing;

import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ComponentSystemEvent;
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

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private List<User> editors;

    private List<ScienceField> selectedScieneFields;

    private List<ScienceField> scienceFields;

    /**
     * Initialize the dtos and load data from the datasource where possible.
     * Create objects for:
     * <ul>
     *     <li>
     *         this scientific forum
     *     </li>
     *     <li>
     *         The user input for a new editor
     *     </li>
     *     <li>
     *         the science field input
     *     </li>
     *     <li>
     *         the list of editors for this scientific forum
     *     </li>
     *     <li>
     *         the list of science fields for this scientific forum
     *     </li>
     *     <li>
     *         the list of global science fields
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
     * Additionally, load the global list of science fields from the datasource.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Gets the id of the scientific forum in question from view params (after
     * being called by a view action) and loads the data needed to display the
     * page from the datasource:
     * <ul>
     *     <li>
     *         th dto for this scientific forum
     *     </li>
     *     <li>
     *         the list of editors for this scientific forum
     *     </li>
     *     <li>
     *         the list of science fields for this scientific forum
     *     </li>
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
    public void onLoad() {
    }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @param event The component system event that happens before rendering
     *              the view param. Can be used to check the view param for
     *              validity
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener(ComponentSystemEvent event) {}

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

    /**
     * Return an array of all values the SubmissionState enum can have.
     *
     * @return ALl options of SubmissionState.
     */
    public SubmissionState[] getSubmissionStates() {
        return SubmissionState.values();
    }

    /**
     * Return an array of all values the DateSelect enum can have.
     *
     * @return ALl options of DateSelect.
     */
    public DateSelect[] getDateSelects() {
        return DateSelect.values();
    }

    /**
     * Return if the logged-in user is editor of this scientific forum.
     *
     * @return Is the logged-in user editor of this scientific forum?
     */
    public boolean loggedInUserIsEditor() {
        return false;
    }

    /**
     * Return if the logged-in user is reviewer of this scientific forum.
     *
     * @return Is the logged-in user reviewer of this scientific forum?
     */
    public boolean loggedInUserIsReviewer() {
        return false;
    }

}
