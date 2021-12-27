package de.lases.control.backing;

import de.lases.business.service.PaperService;
import de.lases.business.service.ScientificForumService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Backing bean for the new submission page.
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
    private PaperService paperService;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private UserService userService;

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
        newSubmission = new Submission();
        coAuthorInput = new User();
        coAuthors = new ArrayList<>();
        if (forumInput != null) {
            editors = userService.getList(forumInput);
        } else {
            forumInput = new ScientificForum();
            editors = new ArrayList<>();
        }

        // TODO: Wenn das Scientific forume existiert muss das hier vorausgefuellt sein und eine illegal user flow
        // exception kommen falls nicht!
        forumInput.setName("Mathematik Konferenz 2022");
        forumInput.setId(1);
        newSubmission.setScientificForumId(forumInput.getId());
        editors = userService.getList(forumInput);
    }

    /**
     * Add the entered co-author to the list of co-authors.
     */
    public void submitCoAuthor() {
        for (User coAuthor: coAuthors) {
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
        for (int i = 0; i < coAuthors.size(); i++) {
            if (coAuthors.get(i).getEmailAddress().equals(user.getEmailAddress())) {
                coAuthors.remove(i);
            }
        }
    }

    /**
     * Submit the submission and go to the page of the entered submission.
     *
     * @return The page of the entered submission.
     */
    public String submit() throws IOException {
        newSubmission.setSubmissionTime(LocalDateTime.now());
        newSubmission.setState(SubmissionState.SUBMITTED);
        // TODO: Was, wenn der User nicht angemeldet ist?
        newSubmission.setAuthorId(sessionInformation.getUser().getId());
        newSubmission = submissionService.add(newSubmission, coAuthors);

        if (newSubmission == null) {
            logger.log(Level.WARNING, "the submission was not successfully added.");
            return null;
        } else {
            Paper paper = new Paper();
            logger.log(Level.INFO, "Adding paper to the submission with id: " + newSubmission.getId());
            paper.setSubmissionId(newSubmission.getId());
            paper.setVisible(false);
            paper.setUploadTime(LocalDateTime.now());
            FileDTO file = new FileDTO();
            file.setFile(uploadedPDF.getInputStream().readAllBytes());
            paperService.add(file, paper);
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
