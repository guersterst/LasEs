package de.lases.control.backing;

import de.lases.business.service.ReviewService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.exception.IllegalAccessException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
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
    private SubmissionService submissionService;

    @Inject
    private ReviewService reviewService;

    @Inject
    private NewReviewBacking newReviewBacking;

    @Inject ToolbarBacking toolbarBacking;

    private Part uploadedRevisionPDF;

    private DateSelect dateFilterSelectPaper;

    private DateSelect dateFilterSelectReview;

    private boolean visibleFilterInputPaper;

    private boolean visibleFilterInputReview;

    private boolean recommendationFilterInputReview;

    private Submission submission;

    private ScientificForum scientificForum;

    private List<User> coAuthors;

    private User author;

    private Pagination<Paper> paperPagination;

    private Pagination<Review> reviewPagination;

    private ReviewedBy reviewedBy;

    private Paper newestPaper;

    /**
     * Initialize the dtos needed for displaying this page.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Get the correct submission from the view param and load all data that
     * should be displayed.
     *
     * @throws IllegalAccessException If the user has no access rights for this
     *                                submission
     */
    public void onLoad() throws IllegalAccessException { }

    /**
     * Set the state of the submission, which can be SUBMITTED,
     * REVISION_REQUIRED, REJECTED, ACCEPTED.
     */
    public void setState() {
    }

    // TODO: hier IOException? Ueberhaupt an den Service deligieren?
    /**
     * Download the PDF belonging to a specific review.
     *
     * @param review The review to download.
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
     */
    public void downloadPaper(Paper paper) throws IOException {
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
        return null;
    }

    /**
     * Upload a new revision as a pdf.
     */
    public void uploadPDF() {
    }

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

    public DateSelect getDateFilterSelectPaper() {
        return dateFilterSelectPaper;
    }

    /**
     * Set the selected filter option for filtering papers after date.
     *
     * @param dateFilterSelectPaper The selected filter option for filtering
     *                              papers after date.
     */
    public void setDateFilterSelectPaper(DateSelect dateFilterSelectPaper) {
        this.dateFilterSelectPaper = dateFilterSelectPaper;
    }

    public DateSelect getDateFilterSelectReview() {
        return dateFilterSelectReview;
    }

    /**
     * Set the selected filter option for filtering reviews after date.
     *
     * @param dateFilterSelectReview The selected filter option for filtering
     *                               reviews after date.
     */
    public void setDateFilterSelectReview(DateSelect dateFilterSelectReview) {
        this.dateFilterSelectReview = dateFilterSelectReview;
    }

    public boolean isVisibleFilterInputPaper() {
        return visibleFilterInputPaper;
    }

    /**
     * Set the selected filter option for filtering papers after visibility.
     *
     * @param visibleFilterInputPaper The selected filter option for filtering
     *                                papers after visibility.
     */
    public void setVisibleFilterInputPaper(boolean visibleFilterInputPaper) {
        this.visibleFilterInputPaper = visibleFilterInputPaper;
    }

    public boolean isVisibleFilterInputReview() {
        return visibleFilterInputReview;
    }

    /**
     * Set the selected filter option for filtering reviews after visibility.
     *
     * @param visibleFilterInputReview The selected filter option for filtering
     *                                 reviews after visibility.
     */
    public void setVisibleFilterInputReview(boolean visibleFilterInputReview) {
        this.visibleFilterInputReview = visibleFilterInputReview;
    }

    public boolean isRecommendationFilterInputReview() {
        return recommendationFilterInputReview;
    }

    /**
     * Set the selected filter option for filtering reviews after reviewer
     * recommendation.
     *
     * @param recommendationFilterInputReview The selected filter option for
     *                                        filtering reviews after reviewer
     *                                        recommendation.
     */
    public void setRecommendationFilterInputReview(
            boolean recommendationFilterInputReview) {
        this.recommendationFilterInputReview = recommendationFilterInputReview;
    }

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

}
