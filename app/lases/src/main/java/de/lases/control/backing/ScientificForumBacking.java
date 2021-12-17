package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.business.service.*;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Backing bean for the scientific forum page.
 */
@ViewScoped
@Named
public class ScientificForumBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -729646507861287388L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private ReviewService reviewService;

    @Inject
    private ScientificForumService forumService;

    @Inject
    private ScienceFieldService scienceFieldService;

    @Inject
    private UserService userService;

    @Inject
    private ConfigPropagator config;

    private ScientificForum forum;

    private User user;

    private User newEditorInput;

    private User removeEditorInput;

    private ScienceField selectedScienceFieldInput;

    private LocalDate forumDeadLineInput;

    private Pagination<Submission> submissionPagination;

    private List<User> editors;

    private List<ScienceField> currentScieneFields;

    private List<ScienceField> allScienceFields;

    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW;
    }

    private Tab tab;

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
        user = sessionInformation.getUser();
        forum = new ScientificForum();
        editors = new ArrayList<>();
        allScienceFields = new ArrayList<>();

        newEditorInput = new User();
        removeEditorInput = new User();
        currentScieneFields = new ArrayList<>();
        selectedScienceFieldInput = new ScienceField();
        displayOwnSubmissionsTab();
    }

    public void displayOwnSubmissionsTab() {
        //Todo defaultsortedby not working
        submissionPagination = new Pagination<Submission>("submissionTime") {

            @Override
            public void loadData() {
                //.getResultListParameters().setDateSelect(DateSelect.ALL);
                submissionPagination.setEntries(submissionService.getList(forum, user,
                        Privilege.AUTHOR, getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(config.getProperty("MAX_PAGINATION_LIST_LENGTH"));
                return (int) Math.ceil((double) submissionService
                        .countSubmissions(user, Privilege.AUTHOR,
                                getResultListParameters()) / itemsPerPage);
            }
        };
        tab = Tab.OWN_SUBMISSIONS;
    }

    public void displayReviewSubmissionsTab() {
        submissionPagination = new Pagination<>("submissionTime") {

            @Override
            public void loadData() {
                submissionPagination.setEntries(submissionService.getList(forum,
                        sessionInformation.getUser(), Privilege.REVIEWER, getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(config.getProperty("MAX_PAGINATION_LIST_LENGTH"));
                return (int) Math.ceil((double) submissionService
                        .countSubmissions(user, Privilege.REVIEWER,
                                getResultListParameters()) / itemsPerPage);
            }
        };
        tab = Tab.SUBMISSIONS_TO_REVIEW;
        submissionPagination.loadData();
    }

    public void displayEditSubmissionsTab() {
        submissionPagination = new Pagination<>("submissionTime") {

            @Override
            public void loadData() {
                submissionPagination.getResultListParameters().setDateSelect(DateSelect.ALL);
                submissionPagination.setEntries(submissionService.getList(forum,
                        sessionInformation.getUser(), Privilege.EDITOR, getResultListParameters()));
            }


            @Override
            protected Integer calculateNumberPages() {
                int itemsPerPage = Integer.parseInt(config.getProperty("MAX_PAGINATION_LIST_LENGTH"));
                return (int) Math.ceil((double) submissionService
                        .countSubmissions(user, Privilege.EDITOR,
                                getResultListParameters()) / itemsPerPage);
            }
        };
        submissionPagination.applyFilters();
        tab = Tab.SUBMISSIONS_TO_EDIT;
        submissionPagination.loadData();
    }

    /**
     * Gets the id of the scientific forum in question from view params (after
     * being called by a view action) and loads the data needed to display the
     * page from the datasource:
     * <ul>
     *     <li>
     *         the dto for this scientific forum
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
     *         the first page of the reviewed submission pagination.
     *     </li>
     *     <li>
     *         the first page of the edited submission pagination.
     *     </li>
     * </ul>
     */
    public void onLoad() {
        forum = forumService.get(forum);
        editors = userService.getList(forum);

        if (forum.getDeadline() != null) {
            forumDeadLineInput = LocalDate.of(forum.getDeadline().getYear(), forum.getDeadline().getMonth(),
                    forum.getDeadline().getDayOfMonth());
        }

        allScienceFields = scienceFieldService.getList(new ResultListParameters());
        currentScieneFields = scienceFieldService.getList(forum, new ResultListParameters());
        allScienceFields.removeAll(currentScieneFields);
        submissionPagination.loadData();
    }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener() {
        if(forum.getId() == null) {
            throw new IllegalUserFlowException("The view parameter was not transmitted correctly (must not be null).");
        }
    }

    /**
     * Delete the scientific forum and go to the homepage.
     *
     * @return Go to the homepage.
     */
    public String deleteForum() {
        forumService.remove(forum);
        //TODO
        //NavigationHandler navigationHandler = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
        //navigationHandler.handleNavigation();
        return "/views/authenticated/homepage.xhtml?faces-redirect=true";
    }

    /**
     * Add the currently selected user to the list of editors.
     */
    public void addEditor() {
        newEditorInput = userService.get(newEditorInput);
        if (newEditorInput.getId() != null) {
            forumService.addEditor(newEditorInput, forum);
            if (!editors.contains(newEditorInput)) {
                editors.add(newEditorInput);
            }
        }
        newEditorInput = new User();
    }

    /**
     * Add the currently selected scienceField to the list of science fields.
     */
    public void addScienceField() {
        forumService.addScienceField(selectedScienceFieldInput, forum);
        currentScieneFields.add(selectedScienceFieldInput);
        allScienceFields.remove(selectedScienceFieldInput);
    }

    /**
     * Remove a specific user form the list of editors.
     *
     */
    public void removeEditor(User editor) {
        forumService.removeEditor(editor, forum);
        editors.remove(editor);
    }

    /**
     * Remove a specific science field from the list of science fields.
     *
     * @param scienceField Field to remove from the list.
     */
    public void removeScienceField(ScienceField scienceField) {
        forumService.removeScienceField(scienceField, forum);
        currentScieneFields.remove(scienceField);
        allScienceFields.add(scienceField);
    }

    /**
     * Submit all changes made to the forum that have nothing to do with the
     * editor or scientific forum list.
     */
    public void submitChanges() {
        forumService.change(forum);
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public User getRemoveEditorInput() {
        return removeEditorInput;
    }

    public void setRemoveEditorInput(User removeEditorInput) {
        this.removeEditorInput = removeEditorInput;
    }

    public User getNewEditorInput() {
            return newEditorInput;
    }

    public void setNewEditorInput(User newEditorInput) {
        this.newEditorInput = newEditorInput;
    }

    public ScienceField getSelectedScienceFieldInput() {
        return selectedScienceFieldInput;
    }

    public void setSelectedScienceFieldInput(ScienceField selectedScienceFieldInput) {
        this.selectedScienceFieldInput = selectedScienceFieldInput;
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
    public List<ScienceField> getCurrentScienceFields() {
        return currentScieneFields;
    }

    /**
     * Get the list of global science fields.
     *
     * @return The list of science fields.
     */
    public List<ScienceField> getAllScienceFields() {
        return allScienceFields;
    }

    public ScientificForum getForum() {
        return forum;
    }

    public void setForum(ScientificForum forum) {
        this.forum = forum;
    }

    public LocalDate getForumDeadLineInput() {
        return forumDeadLineInput;
    }

    public void setForumDeadLineInput(LocalDate forumDeadLineInput) {
        this.forumDeadLineInput = forumDeadLineInput;
        forum.setDeadline(LocalDateTime.of(forumDeadLineInput, LocalTime.MIDNIGHT));
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
    public boolean loggedInUserIsEditorOrAdmin() {
        return editors.contains(user) || user.isAdmin();
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
