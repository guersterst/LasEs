package de.lases.control.backing;

import de.lases.business.service.ReviewService;
import de.lases.control.exception.IllegalUserFlowException;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

/**
 * Backing bean for the new review page.
 */
@RequestScoped
@Named
public class NewReviewBacking {

    @Inject
    private ReviewService reviewService;

    @Inject
    private SessionInformation sessionInformation;

    private Part uploadedPDF;

    private Review review;

    /**
     * Checks if this review belongs to a submission and throws an exception
     * if there is non.
     *
     * @throws IllegalUserFlowException If there is no submission which belongs
     *                                  this new review, which means the user
     *                                  accessed this page via URL.
     */
    @PostConstruct
    public void init() throws IllegalUserFlowException {
    }

    /**
     * Save the currently entered review and show the submission page.
     *
     * @return Return to the submission page.
     */
    public String addReview() {
        return null;
    }

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
