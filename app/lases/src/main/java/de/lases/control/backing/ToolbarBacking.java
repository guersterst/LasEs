package de.lases.control.backing;

import de.lases.business.service.SubmissionService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    SubmissionService submissionService;

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
     * @param submission A submission dto that is filled with the id received
     *                   via view params.
     */
    public void onLoad(Submission submission) { }

    /**
     * The currently entered user will be added as a reviewer.
     */
    public void addReviewer() {
    } // y

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

}
