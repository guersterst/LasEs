package de.lases.control.backing;

import de.lases.business.service.ReviewService;
import de.lases.business.service.SubmissionService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class SubmissionBacking implements Serializable {

    private SessionInformation sessionInformation;

    private UserService userService;

    private Submission submission;

    private Pagination<Paper> paperPagination;

    private Pagination<Review> reviewPagination;

    // Get these value directly from the view:

    private SubmissionService submissionService;

    private ReviewService reviewService;

    private Part uploadedRevisionPDF;

    private String forumName;

    private User author;

    private List<User> coAuthors;

    private DateSelect paper;

    private DateSelect review;

    private boolean visibleFilterInputPaper;
    private boolean visibleFilterInputReview;

    private boolean recommendationFilterInputReview;

    @PostConstruct
    public void init() {
    }

    public void setState(SubmissionState submissionState) {
    }

    public void downloadReview(int paperId) {
    }

    public void releaseReview(int reviewId, int paperId) {
    }

    public void downloadPaper(int paperId) {
    }

    public void uploadReview() {
    }

    public void acceptReviewing() {
    }

    public void declineReviewing() {
    }

    public void applyFilters() {
    }

    public void releaseRevision(int revisionId, int paperId) {
    }

    public void getReviewerForReview(Submission submission) {
    }

    public void getAuthorForPaper(Paper paper) {
    }

    public void uploadPDF() {
    }


}
