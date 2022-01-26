package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Event;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Backing bean for the new submission page.
 *
 * @author Sebastian Vogt
 */
@ViewScoped
@Named
public class NewSubmissionBacking implements Serializable {

    private static final Logger logger = Logger.getLogger(NewSubmissionBacking.class.getName());

    @Serial
    private static final long serialVersionUID = 7741936440137638673L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private UserService userService;

    @Inject
    private ScientificForumService forumService;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle messageBundle;

    private Submission newSubmission;

    private ScientificForum forumInput;

    private Part uploadedPDF;

    private User coAuthorInput;

    private List<User> coAuthors;

    private List<User> editors;

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
     * </ul>
     * The list of editors for the forum this submission will be submitted in
     * will be directly populated from the database, if the scientific forum is already prefilled.
     */
    @PostConstruct
    public void init() {
        coAuthorInput = new User();
        coAuthors = new ArrayList<>();
        forumInput = new ScientificForum();
        newSubmission = new Submission();
    }

    public void onLoad() {

        if (forumInput.getId() == null || !ScientificForumService.exists(forumInput)) {
            throw new IllegalUserFlowException("The URL parameters are invalid. Possible cause: the forum from the "
                    + "redirect was performed is not valid");
        }

        forumInput = forumService.get(forumInput);
        editors = userService.getList(forumInput);
        newSubmission.setScientificForumId(forumInput.getId());
    }

    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener() {
        if (forumInput.getId() == null) {
            throw new IllegalUserFlowException("NewSubmission page called without a forum id.");
        }
    }

    /**
     * Must be called after forumInput has been initialized.
     */
    private void initNewSubmission() {
        newSubmission = new Submission();
        newSubmission.setScientificForumId(forumInput.getId());
    }

    /**
     * Add the entered co-author to the list of co-authors.
     */
    public void submitCoAuthor() {
        for (User coAuthor : coAuthors) {
            if (coAuthor.getEmailAddress().equals(coAuthorInput.getEmailAddress())) {
                coAuthor.setTitle(coAuthorInput.getTitle());
                coAuthor.setFirstName(coAuthorInput.getFirstName());
                coAuthor.setLastName(coAuthorInput.getLastName());
                return;
            }
        }
        coAuthors.add(coAuthorInput.clone());
    }

    /**
     * Delete a user from the list of co-authors.
     *
     * @param user The co-author to delete.
     */
    public void deleteCoAuthor(User user) {
        coAuthors.removeIf(current -> current.getEmailAddress().equals(user.getEmailAddress()));
    }

    /**
     * Submit the submission and go to the page of the entered submission.
     *
     * @return The page of the entered submission.
     */
    public String submit() {
        newSubmission.setSubmissionTime(LocalDateTime.now());
        newSubmission.setState(SubmissionState.SUBMITTED);
        newSubmission.setAuthorId(sessionInformation.getUser().getId());

        Paper paper = new Paper();
        paper.setVisible(true);
        paper.setUploadTime(LocalDateTime.now());
        FileDTO file = new FileDTO();

        if (newSubmission.getEditorId() == null) {
            uiMessageEvent.fire(new UIMessage(messageBundle.getString("noEditorForNewSubmission"), MessageCategory.ERROR));
            return "/views/authenticated/scientificForumList.xhtml";
        }

        try {
            file.setFile(uploadedPDF.getInputStream().readAllBytes());
            newSubmission = submissionService.add(newSubmission, coAuthors, paper, file);
        } catch (IOException e) {
            initNewSubmission();

            uiMessageEvent.fire(new UIMessage(messageBundle.getString("failedUpload"), MessageCategory.ERROR));
            logger.log(Level.WARNING, "the submission was not successfully added.");
            return null;
        }

        if (newSubmission == null) {
            initNewSubmission();

            logger.log(Level.WARNING, "the submission was not successfully added.");
            return null;
        } else {
            return "submission?faces-redirect=true&id=" + newSubmission.getId();
        }
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
