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
     * Initialize dtos.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * This method should be called when the proper submission was injected
     * by the submission backing bean, and it initializes the entire toolbar.
     */
    public void onLoad() { }

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
     * Add the entered user ad managing editor, replacing the old editor.
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
