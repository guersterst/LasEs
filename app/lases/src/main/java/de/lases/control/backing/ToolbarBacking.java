package de.lases.control.backing;

import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Backing bean for the toolbar view. This view belongs to the submission page.
 */
@ViewScoped
@Named
public class ToolbarBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -2791102953586117163L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private UserService userService;

    private static final Logger logger = Logger.getLogger(ToolbarBacking.class.getName());

    private Submission submission;

    private User reviewerInput;

    private ReviewedBy reviewedByInput;

    private User editorInput;

    private User currentEditor;

    private List<User> reviewer;

    private List<User> editors;

    /**
     * Initialize the dtos.
     * Create the dtos for:
     * <ul>
     *     <li> the submission </li>
     *     <li> the reviewer input </li>
     *     <li> the "reviewed by" input </li>
     *     <li> the editor input </li>
     *     <li> current editor </li>
     *     <li>
     *         the list of reviewers of the submission this toolbar is
     *         about
     *     </li>
     *     <li>
     *         the list of editors of the scientific forum for this
     *         submission
     *     </li>
     * </ul>
     * No data can be loaded form the datasource at this point. (See the
     * {@code onLoad() method}).
     */
    @PostConstruct
    public void init() {
        submission = new Submission();
        reviewerInput = new User();
        reviewedByInput = new ReviewedBy();
        reviewer = new ArrayList<>();
        editors = new ArrayList<>();
        currentEditor = new User();
    }

    /**
     * Load data that was not loaded in the {@code init()} method from the
     * datasource:
     * <ul>
     *     <li> The current editor of the submission. </li>
     *     <li> The list of editors of the submission. </li>
     *     <li>
     *         The list of reviewers for this submission
     *     </li>
     *     <li>
     *         The list of editors of the scientific forum this submission is in
     *     </li>
     * </ul>
     * This method is called after the id of our submission was received via
     * view params by the {@code SubmissionBacking}. This method is <b>not</b>
     * called by a view action but rather by the onLoad method in the
     * {@code SubmissionBacking}.
     *
     */
    public void onLoad(Submission sub) {
        submission = sub;
        reviewer = userService.getList(submission, Privilege.REVIEWER);
        editors = userService.getList(submission, Privilege.EDITOR);

        currentEditor.setId(submission.getEditorId());
        currentEditor = userService.get(currentEditor);
    }

    /**
     * The currently entered user will be added as a reviewer.
     */
    public void addReviewer() {
        User newUser = new User();
        newUser.setEmailAddress(reviewerInput.getEmailAddress());
        User newReviewer = userService.get(newUser);

        if (newReviewer != null) {
            logger.finest("Reviewer is an existing person.");
        }

        ReviewedBy reviewedBy = reviewedByInput.clone();
        reviewedBy.setReviewerId(newReviewer.getId());
        reviewedBy.setSubmissionId(submission.getId());
        reviewedBy.setHasAccepted(AcceptanceStatus.NO_DECISION);

        submissionService.addReviewer(newReviewer, reviewedBy);

        for (User user : reviewer) {
            if (newReviewer.getId().equals(user.getId())) {
                return;
            }
        }

        getReviewer().add(newReviewer);
    }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @param event The component system event that happens before rendering
     *              the view param.
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener() {
    }

    /**
     * Remove the specified user form the list of reviewers.
     *
     * @param user The user to remove from the list of reviewers.
     */
    public void removeReviewer(String user) {
    }

    /**
     * Add the entered user as managing editor, replacing the old editor.
     */
    public void chooseNewManagingEditor() {
    }

    /**
     * State that a revision is required on the submission belonging to this
     * page.
     */
    public void requireRevision() {
    }

    /**
     * Accept the submission belonging to this page.
     */
    public void acceptSubmission() {
    } // y

    /**
     * Reject the submission belonging to this page.
     */
    public void rejectSubmission() {
    } // y

    /**
     * Get the submission belonging to this page.
     *
     * @return The submission belonging to this page.
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * Set the submission belonging to this page.
     *
     * @param submission The new submissions.
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    /**
     * Get the user that is input as a reviewer.
     *
     * @return The new reviewer.
     */
    public User getReviewerInput() {
        return reviewerInput;
    }

    /**
     * Set the user that is input as a reviewer.
     *
     * @param reviewerInput The new reviewer.
     */
    public void setReviewerInput(User reviewerInput) {
        this.reviewerInput = reviewerInput;
    }

    /**
     * Get the user that is input as a reviewer.
     *
     * @return The new reviewer.
     */
    public ReviewedBy getReviewedByInput() {
        return reviewedByInput;
    }

    /**
     * Set meta information about the reviewer that is supposed to be added.
     * This is used to set a deadline for the new reviewer.
     *
     * @param reviewedByInput Meta information about the new reviewer.
     */
    public void setReviewedByInput(ReviewedBy reviewedByInput) {
        this.reviewedByInput = reviewedByInput;
    }

    /**
     * Get the user that is currently entered as the new editor.
     *
     * @return The user that is supposed to become the new editor.
     */
    public User getEditorInput() {
        return editorInput;
    }

    /**
     * Set the user that is currently entered as the new editor.
     *
     * @param editorInput User that is supposed to become the new editor.
     */
    public void setEditorInput(User editorInput) {
        this.editorInput = editorInput;
    }

    /**
     * Get the current editor of the submission.
     *
     * @return The editor of the submission.
     */
    public User getCurrentEditor() {
        return currentEditor;
    }

    /**
     * Get the list of reviewers of the submission.
     *
     * @return The list of reviewers.
     */
    public List<User> getReviewer() {
        return reviewer;
    }

    /**
     * Get the list of editors for the scientific forum this submission belongs
     * to.
     *
     * @return The list of editors.
     */
    public List<User> getEditors() {
        return editors;
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
     * Return if the logged-in user is editor of this submission.
     *
     * @return Is the logged-in user editor of this submission?
     */
    public boolean loggedInUserIsEditor() {
        return false;
    }

    /**
     * Return if the logged-in user is reviewer of this submission.
     *
     * @return Is the logged-in user reviewer of this submission?
     */
    public boolean loggedInUserIsReviewer() {
        return false;
    }

}
