package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Backing bean for the new submission page.
 */
@RequestScoped
@Named
public class NewSubmissionBacking {

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private UserService userService;

    private Submission newSubmission;

    private ScientificForum forumInput;

    private User editorSelectionInput;

    private Part uploadedPDF;

    private User coAuthorInput;

    private List<User> coAuthors;

    private List<User> editors;

    private User selectedEditor;

    /**
     * Initialize the dtos used in this bean and load the list of possible
     * editors.
     * The following dtos are initialized:
     * <ul>
     *     <li>
     *         the submission that is created on this page
     *     </li>
     *     <li>
     *         the scientific forum input
     *     </li>
     *     <li>
     *         the editor selection input
     *     </li>
     *     <li>
     *         the pdf upload input
     *     </li>
     *     <li>
     *         the co author input
     *     </li>
     *     <li>
     *         the list of co authors
     *     </li>
     *     <li>
     *         the list of editors for the forum this submission will be
     *         submitted in
     *     </li>
     *     <li>
     *         the selected editor
     *     </li>
     * </ul>
     * The list of editors for the forum this submission will be submitted in
     * will be directly populated from the database.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Add the entered co-author to the list of co-authors.
     */
    public void submitCoAuthor() {
    }

    /**
     * Delete a user from the list of co-authors.
     *
     * @param user The co-author to delete.
     */
    public void deleteCoAuthor(User user) {
    }

    /**
     * Submit the submission and go to the page of the entered submission.
     *
     * @return The page of the entered submission.
     */
    public String submit() {
        return null;
    }

    /**
     * Get the dto that stores data about the new submission.
     *
     * @return The dto that holds data about the new submission.
     */
    public Submission getNewSubmission() {
        return newSubmission;
    }

    /**
     * Set the dto that stores data about the new submission.
     *
     * @param newSubmission Holds data about the new submission.
     */
    public void setNewSubmission(Submission newSubmission) {
        this.newSubmission = newSubmission;
    }

    /**
     * Get the input where the user chooses a scientific forum for this
     * submission.
     *
     * @return The input of the scientific forum.
     */
    public ScientificForum getForumInput() {
        return forumInput;
    }

    /**
     * Set the input where the user chooses a scientific forum for this
     * submission.
     *
     * @param forumInput The input of the scientific forum.
     */
    public void setForumInput(ScientificForum forumInput) {
        this.forumInput = forumInput;
    }

    /**
     * Get the selected user that can be added as an editor.
     *
     * @return The selected user that can be added as an editor.
     */
    public User getEditorSelectionInput() {
        return editorSelectionInput;
    }

    /**
     * Set the selected user that can be added as an editor.
     *
     * @param editorSelectionInput Selected user that can be added as an editor.
     */
    public void setEditorSelectionInput(User editorSelectionInput) {
        this.editorSelectionInput = editorSelectionInput;
    }

    /**
     * Get the PDF that is uploaded for submission.
     *
     * @return PDF that is uploaded for the submission.
     */
    public Part getUploadedPDF() {
        return uploadedPDF;
    }

    /**
     * Set the PDF that is uploaded for the submission.
     *
     * @param uploadedPDF PDF that is uploaded for the submission.
     */
    public void setUploadedPDF(Part uploadedPDF) {
        this.uploadedPDF = uploadedPDF;
    }

    /**
     * Get the user that can be submitted as a co-author.
     *
     * @return The user that can be submitted as a co-author.
     */
    public User getCoAuthorInput() {
        return coAuthorInput;
    }

    /**
     * Set the user that can be submitted as a co-author.
     *
     * @param coAuthorInput The user that can be submitted as a co-author.
     */
    public void setCoAuthorInput(User coAuthorInput) {
        this.coAuthorInput = coAuthorInput;
    }

    /**
     * Get the list of users that are currently added as co-authors.
     *
     * @return The list of users that are currently added as co-authors.
     */
    public List<User> getCoAuthors() {
        return coAuthors;
    }

    /**
     * Get the list of users that are possible editors for this submission.
     *
     * @return The list of users that are possible editors for this submission.
     */
    public List<User> getEditors() {
        return editors;
    }

}
