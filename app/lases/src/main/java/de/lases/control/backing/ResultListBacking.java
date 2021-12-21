package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Backing bean for the result list.
 */
@ViewScoped
@Named
public class ResultListBacking implements SubmissionPaginationBacking {


    private enum Tab {
        OWN_SUBMISSIONS, SUBMISSIONS_TO_EDIT, SUBMISSIONS_TO_REVIEW
    }
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

    @Inject
    private ConfigPropagator configPropagator;

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private Pagination<User> userPagination;

    private Pagination<ScientificForum> scientificForumPagination;

    private Tab tab;

    private User user;

    private String searchWord;

    /**
     * Initialize the dtos and load data from the datasource where possible.
     * Create objects for:
     * <ul>
     *     <li>
     *         the own submission pagination
     *     </li>
     *     <li>
     *         the reviewed submission pagination
     *     </li>
     *     <li>
     *         the reviewed submission pagination
     *     </li>
     *     <li>
     *         the user pagination
     *     </li>
     *     <li>
     *         the scientific forum pagination
     *     </li>
     * </ul>
     * Load the following data from the datasource:
     * <ul>
     *     <li>
     *         the first page of the own submission pagination
     *     </li>
     *     <li>
     *         the fist page of the reviewed submission pagination
     *     </li>
     *     <li>
     *         the first page of the reviewed submission pagination
     *     </li>
     *     <li>
     *         the first page of the user pagination
     *     </li>
     *     <li>
     *         the first page of the scientific forum pagination
     *     </li>
     * </ul>
     */
    @PostConstruct
    public void init() {
        user = sessionInformation.getUser();
    }

    public void onLoad() {
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
        submissionPagination.getResultListParameters().setGlobalSearchWord(searchWord);
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
        submissionPagination.getResultListParameters().setGlobalSearchWord(searchWord);
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
        submissionPagination.getResultListParameters().setGlobalSearchWord(searchWord);
        tab = Tab.SUBMISSIONS_TO_REVIEW;
        submissionPagination.loadData();
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

    /**
     * Set the global search word to search for.
     *
     * @param searchWord The search word.
     */
    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    /**
     * Get the global search word to search for.
     *
     * @returns The search word.
     */
    public String getSearchWord() {
        return searchWord;
    }

}
