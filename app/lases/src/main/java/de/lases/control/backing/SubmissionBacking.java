package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.exception.IllegalAccessException;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.PropertyResourceBundle;

/**
 * @author Stefanie Gürster
 *
 * Backing bean for the submission page.
 */
@ViewScoped
@Named
public class SubmissionBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -5192234942963029661L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private UserService userService;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private ReviewService reviewService;

    @Inject
    private PaperService paperService;

    @Inject
    private NewReviewBacking newReviewBacking;

    @Inject
    private ToolbarBacking toolbarBacking;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle resourceBundle;

    private Part uploadedRevisionPDF;

    private Submission submission;

    private ScientificForum scientificForum;

    private List<User> coAuthors;

    private User author;

    private Pagination<Paper> paperPagination;

    private Pagination<Review> reviewPagination;

    private ReviewedBy reviewedBy;

    private Paper newestPaper;

    /**
     * Initialize the dtos.
     * Create the objects for:
     * <ul>
     *     <li>
     *         the revision upload pdf
     *     </li>
     *     <li>
     *         The submission this page is about
     *     </li>
     *     <li>
     *         the scientific forum this paper was submitted into
     *     </li>
     *     <li>
     *         the list of co-authors for this submission
     *     </li>
     *     <li>
     *         the pagination for papers
     *     </li>
     *     <li>
     *         the pagination for reviews
     *     </li>
     *     <li>
     *         the reviewed by object
     *     </li>
     *     <li>
     *         the newest paper in the submission
     *     </li>
     * </ul>
     * No data can be loaded form the datasource at this point. (See the
     * {@code onLoad() method}).
     */
    @PostConstruct
    public void init() {
        submission = new Submission();
        scientificForum = new ScientificForum();
        coAuthors = new LinkedList<>();
        author = new User();

        paperPagination = new Pagination<>("version") {
            @Override
            public void loadData() {
               setEntries(paperService.getList(submission, sessionInformation.getUser(), getResultListParameters()));
                }

            @Override
            protected Integer calculateNumberPages() {
                return 1;
            }
        };

    }

    /**
     * Get the correct submission id from the view param (will be called in a
     * view action) and load all data that should be displayed from the
     * datasource:
     * <ul>
     *     <li>
     *         The submission this page is about
     *     </li>
     *     <li>
     *         the scientific forum this submission was submitted into
     *     </li>
     *     <li>
     *         the list of co-authors for this submission
     *     </li>
     *     <li>
     *         the pagination for papers
     *     </li>
     *     <li>
     *         the pagination for reviews
     *     </li>
     *     <li>
     *         the reviewed by object
     *     </li>
     *     <li>
     *         the newest paper in the submission
     *     </li>
     * </ul>
     *
     * @throws IllegalAccessException If the user has no access rights for this
     *                                submission
     */
    public void onLoad() {/*
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback()) {
            //TODO: rausnehmen.

        }*/
        submission = submissionService.get(submission);

        author.setId(submission.getAuthorId());
        author = userService.get(author);

        scientificForum.setId(submission.getScientificForumId());
        scientificForum = scientificForumService.get(scientificForum);

        coAuthors = userService.getList(submission, Privilege.AUTHOR);

        coAuthors.removeIf(user -> user.getId().equals(author.getId()));

        paperPagination.loadData();

        toolbarBacking.onLoad(submission);
    }



    /**
     * Checks if the view param is an integer and throws an exception if it is
     * not
     *
     * @throws IllegalUserFlowException If there is no integer provided as view
     *                                  param
     */
    public void preRenderViewListener() {
    }

    /**
     * Set the state of the submission, which can be SUBMITTED,
     * REVISION_REQUIRED, REJECTED, ACCEPTED.
     */
    public void setState(SubmissionState submissionState) {
        submission.setState(submissionState);
    }


    /**
     * Download the PDF belonging to a specific review.
     *
     * @param review The review to download.
     * @throws IOException If the download fails.
     */
    public void downloadReview(Review review) throws IOException {
    }

    /**
     * Release a specific review so that it can be viewed by the submitter.
     *
     * @param review The review to release.
     */
    public void releaseReview(Review review) {
    }

    /**
     * Download the PDF belonging to a specific paper.
     *
     * @param paper The paper to download.
     * @throws IOException If the download fails.
     */
    public void downloadPaper(Paper paper) {
        FileDTO file = paperService.getFile(paper);
        byte[] pdf = file.getFile();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(pdf.length);
        baos.write(pdf, 0, pdf.length);

        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType(submission.getTitle() + "/pdf");
        response.setContentLength(pdf.length);
        response.setHeader("Content-disposition","attachment;filename="+ submission.getTitle() + ".pdf");

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);
            outputStream.flush();
            response.flushBuffer();
        } catch (IOException exception) {
            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("failedDownload"), MessageCategory.WARNING));
        }


        facesContext.responseComplete();
    }

    /**
     * Go to the page for a new review for this specific submission.
     *
     * @return The page for a new review
     */
    public String uploadReview() {
        return null;
    }

    /**
     * Is called when the specified user accepted to be a reviewer.
     *
     * @param user The user that accepted to be a reviewer.
     */
    public void acceptReviewing(User user) {
    }

    /**
     * Is called when the specified user declined to be a reviewer.
     *
     * @param user The user that declined to be a reviewer.
     */
    public void declineReviewing(User user) {
    }

    /**
     * Apply all the filters that are specified outside the pagination to
     * the table.
     */
    public void applyFilters() {
    }

    /**
     * Release a specific revision so that it can be viewed by the reviewers.
     *
     * @param paper The revision (which is a {@code paper}) to release
     */
    public void releaseRevision(Paper paper) {
       if (loggedInUserIsEditor()) {
           paper.setVisible(true);
           paperService.change(paper);
       } else {
           uiMessageEvent.fire(new UIMessage(resourceBundle.getString("releaseRevision"), MessageCategory.WARNING));
       }
    }

    /**
     * Gets the reviewer that submitted a specific review.
     *
     * @param review Review to which the reviewer should be returned.
     * @return The reviewer who submitted the review.
     */
    public User getReviewerForReview(Review review) {
        return null;
    }

    /**
     * Gets the author that submitted a specific paper.
     *
     * @param paper Paper to which the submitter should be returned.
     * @return The submitter of the paper.
     */
    public User getAuthorForPaper(Paper paper) {
        return author;
    }


    /**
     * Upload a new revision as a pdf.
     */
    public void uploadPDF() {

        try {
            // Get the file as byte[].
            byte[] input = uploadedRevisionPDF.getInputStream().readAllBytes();
            FileDTO file = new FileDTO();
            file.setFile(input);

            // Put revision pdf into a new paper.
            Paper revision = new Paper();
            revision.setVisible(false);
            revision.setSubmissionId(submission.getId());
            revision.setUploadTime(LocalDateTime.now());

            paperService.add(file,revision);

            Submission newSubmission = submission.clone();
            newSubmission.setState(SubmissionState.SUBMITTED);
            newSubmission.setRevisionRequired(newSubmission.getState() == SubmissionState.REVISION_REQUIRED);
            newSubmission.setDeadlineRevision(null);

            submissionService.change(newSubmission);
            submission = newSubmission;
            toolbarBacking.onLoad(submission);

        }catch (IOException e) {

            uiMessageEvent.fire(new UIMessage(resourceBundle.getString("failedUpload"), MessageCategory.WARNING));

        }
        paperPagination.loadData();
    }

    /**
     * Delete this submission and go to the homepage.
     *
     * @return Go to the homepage.
     */
    public String deleteSubmission() {
        if (isViewerSubmitter() || sessionInformation.getUser().isAdmin()) {
            submissionService.remove(submission.clone());
            return "/views/authenticated/homepage";
        }
        uiMessageEvent.fire(new UIMessage(resourceBundle.getString("failedDelete"), MessageCategory.WARNING));
        return "/views/authenticated/submission";
    }

    /**
     * Get the {@code Part} file where the revision PDF can be uploaded.
     *
     * @return The file where the revision can be uploaded.
     */
    public Part getUploadedRevisionPDF() {
        return uploadedRevisionPDF;
    }

    /**
     * Set the {@code Part} file where the revision PDF can be uploaded.
     *
     * @param uploadedRevisionPDF The file where the revision can be uploaded.
     */
    public void setUploadedRevisionPDF(Part uploadedRevisionPDF) {
        this.uploadedRevisionPDF = uploadedRevisionPDF;
    }

    /**
     * Apply changes for the submission state.
     */
    /*
    public void applyState(Submission submission) {

        if (submission.getState() != SubmissionState.REVISION_REQUIRED) {
            submission.setDeadlineRevision(null);
        }
        submissionService.change(submission);
    }

     */

    /**
     * Get the submission this page belongs to.
     *
     * @return The dto for this submission.
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * Get the scientific forum this submission was submitted into.
     *
     * @return The dto for the scientific forum for this submission.
     */
    public ScientificForum getScientificForum() {
        return scientificForum;
    }

    /**
     * Get the list of co-authors for this submission.
     *
     * @return List of co-authors for this submission.
     */
    public List<User> getCoAuthors() {
        return coAuthors;
    }

    /**
     * Get the author of this submission.
     *
     * @return The author of this submission.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Get the pagination filled with papers.
     *
     * @return The pagination for papers.
     */
    public Pagination<Paper> getPaperPagination() {
        return paperPagination;
    }

    /**
     * Get the pagination filled with reviews.
     *
     * @return The pagination for reviews.
     */
    public Pagination<Review> getReviewPagination() {
        return reviewPagination;
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
     * Checks whether the viewer is the submitter.
     *
     * @return true if the viewer is the submitter of this submission.
     */
    public boolean isViewerSubmitter() {
        return submission.getAuthorId() == sessionInformation.getUser().getId();
    }

    public boolean isAdmin() {
        return sessionInformation.getUser().isAdmin();
    }

    /**
     * Get the options of the SubmissionState enum as an array.
     *
     * @return Array of SubmissionState.
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
     * Returns an array of all values the Visibility enum can have.
     *
     * @return All options.
     */
    public Visibility[] getVisibility() {
        return Visibility.values();
    }

    /**
     * Get the possible reviewed-by relationship between this submission and
     * the logged-in user. May be null if there is none.
     *
     * @return Reviewed-by between the logged-in user and this submission.
     */
    public ReviewedBy getReviewedBy() {
        return reviewedBy;
    }

    /**
     * Get the newest paper for this submission.
     *
     * @return The newest paper for this submission.
     */
    public Paper getNewestPaper() {
        return newestPaper;
    }

    /**
     * Return if the logged-in user is editor of this submission.
     *
     * @return Is the logged-in user editor of this submission?
     */
    public boolean loggedInUserIsEditor() {
        return sessionInformation.getUser().getId().equals(submission.getEditorId());
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
