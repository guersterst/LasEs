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

    private Pagination<Submission> submissionPagination;

    private Pagination<Submission> reviewedPagination;

    private Pagination<Submission> editedPagination;

    private Pagination<User> userPagination;

    private Pagination<ScientificForum> scientificForumPagination;

    private ResultListParameters resultListParameters;

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
        resultListParameters = new ResultListParameters();
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

    public void setSearchWord(String searchWord) {
        resultListParameters.setGlobalSearchWord(searchWord);
    }
}
