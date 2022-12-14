package de.lases.control.backing;

import de.lases.business.service.PaperService;
import de.lases.business.service.ReviewService;
import de.lases.business.service.SubmissionService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * Backing bean for the new review page.
 */
@RequestScoped
@Named
public class NewReviewBacking {

    @Inject
    private ReviewService reviewService;

    @Inject
    private SubmissionService submissionService;

    @Inject
    private PaperService paperService;

    @Inject
    private SessionInformation sessionInformation;

    private Part uploadedPDF;

    private Review review;

    /**
     * Checks if this review belongs to a submission and throws an exception
     * if there is none. Also initializes the review dto and the uploaded PDF.
     *
     * @throws IllegalUserFlowException If there is no submission which belongs
     *                                  this new review, which means the user
     *                                  accessed this page via URL.
     */
    @PostConstruct
    public void init() throws IllegalUserFlowException {
        review = new Review();
        review.setReviewerId(sessionInformation.getUser().getId());
        // Submission ID is set per URL parameter.
    }

    public void onLoad() throws IllegalUserFlowException {
        Submission submission = new Submission();
        submission.setId(review.getSubmissionId());
        ReviewedBy reviewedBy = submissionService.getReviewedBy(submission, sessionInformation.getUser());

        if (reviewedBy == null) {
            throw new IllegalUserFlowException("Not a reviewer for this paper.");
        }

        Paper newestPaper = paperService.getLatest(submission);
        if (newestPaper == null) {
            throw new IllegalUserFlowException("Can not submit a review for a submission without a paper.");
        }
        review.setPaperVersion(newestPaper.getVersionNumber());

        if (reviewService.get(review) != null) {
            throw new IllegalUserFlowException("Can not add a second review.");
        }
    }

    /**
     * Save the currently entered review and show the submission page.
     *
     * @return Return to the submission page.
     */
    public String addReview() throws IOException {
        // Get the newest Paper first, so we know the version of the paper for the review.
        Submission submission = new Submission();
        submission.setId(review.getSubmissionId());

        FileDTO file = new FileDTO();
        if (uploadedPDF != null) {
            file.setFile(uploadedPDF.getInputStream().readAllBytes());
        }

        Paper newestPaper = paperService.getLatest(submission);
        if (newestPaper == null) {
            return null;
        }
        review.setPaperVersion(newestPaper.getVersionNumber());

        reviewService.add(review, file);
        return "/views/authenticated/submission.xhtml?faces-redirect=true&id=" + review.getSubmissionId();

    }

    /**
     * Get the {@code Part} file of the uploaded pdf.
     *
     * @return the uploaded pdf.
     */
    public Part getUploadedPDF() {
        return uploadedPDF;
    }

    /**
     * Upload a PDF as a {@code Part} file.
     *
     * @param uploadedPDF The pdf to upload.
     */
    public void setUploadedPDF(Part uploadedPDF) {
        this.uploadedPDF = uploadedPDF;
    }

    /**
     * Get the dto for the review.
     *
     * @return The review.
     */
    public Review getReview() {
        return review;
    }

    /**
     * Set the dto for the review.
     *
     * @param review The new review.
     */
    public void setReview(Review review) {
        this.review = review;
    }
}
