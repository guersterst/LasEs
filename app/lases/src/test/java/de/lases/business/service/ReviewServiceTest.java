package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Review;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    // The required repository.
    private static MockedStatic<ReviewRepository> reviewRepoMocked;

    // The required service.
    private static ReviewService reviewService;

    // The required DTOs and their values
    private static Review review;
    private static FileDTO pdf;
    private static final Integer EXAMPLE_SUBMISSION_ID = 1;
    private static final Integer EXAMPLE_REVIEWER_ID = 10;
    private static final Integer EXAMPLE_PAPER_VERSION = 100;
    private static final byte[] EXAMPLE_PDF = new byte[]{};
    private static final String EXAMPLE_OLD_COMMENT = "Very good!";
    private static final String EXAMPLE_NEW_COMMENT = "MY EYES ARE BLEEDING";

    @BeforeAll
    static void init() {
        // Mock repository and initialize service.
        reviewRepoMocked = mockStatic(ReviewRepository.class);
        reviewService = new ReviewService();

        // Mock get to return a review if the ids are correct.
        reviewRepoMocked.when(() -> ReviewRepository.get(eq(review), any(Transaction.class))).thenReturn(review);
    }

    @BeforeEach
    void resetDTOS() {

        // Reset the dto's value's after each test.
        review = new Review();
        review.setPaperVersion(EXAMPLE_PAPER_VERSION);
        review.setSubmissionId(EXAMPLE_SUBMISSION_ID);
        review.setReviewerId(EXAMPLE_REVIEWER_ID);

        pdf = new FileDTO();
        pdf.setFile(EXAMPLE_PDF);
    }

    @AfterAll
    static void close() {

        // Close the mocks
        reviewRepoMocked.close();
    }

    @Test
    void testGet() {
        reviewService.add(review, pdf);

        Review gotten = reviewService.get(review);
        assertAll(
                () -> assertEquals(EXAMPLE_PAPER_VERSION, gotten.getPaperVersion()),
                () -> assertEquals(EXAMPLE_REVIEWER_ID, gotten.getReviewerId()),
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, gotten.getSubmissionId())
        );
    }

    @Test
    void testAdd() {
        reviewService.add(review, pdf);

        Review gotten = reviewService.get(review);
        assertAll(
                () -> assertEquals(EXAMPLE_PAPER_VERSION, gotten.getPaperVersion()),
                () -> assertEquals(EXAMPLE_REVIEWER_ID, gotten.getReviewerId()),
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, gotten.getSubmissionId())
        );
    }

    @Test
    void testGetFile() {
        reviewService.add(review, pdf);

        FileDTO gotten = reviewService.getFile(review);
        assertEquals(EXAMPLE_PDF, gotten.getFile());
    }

    @Test
    void testChange() {

        // A review containing an old comment.
        review.setComment(EXAMPLE_OLD_COMMENT);
        reviewService.add(review, pdf);

        // Create a review with a new comment but the same ids.
        Review newReview = new Review();
        newReview.setPaperVersion(EXAMPLE_PAPER_VERSION);
        newReview.setSubmissionId(EXAMPLE_SUBMISSION_ID);
        newReview.setReviewerId(EXAMPLE_REVIEWER_ID);
        newReview.setComment(EXAMPLE_NEW_COMMENT);

        // Change the review to contain the new comment.
        reviewService.change(newReview);

        // Mock the get request to return the new review.
        reviewRepoMocked.when(() -> ReviewRepository
                .get(eq(review), any(Transaction.class))).thenReturn(newReview);

        // Request the changed review.
        Review result = reviewService.get(review);

        assertAll(
                () -> assertEquals(EXAMPLE_PAPER_VERSION, result.getPaperVersion()),
                () -> assertEquals(EXAMPLE_REVIEWER_ID, result.getReviewerId()),
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, result.getSubmissionId()),
                () -> assertEquals(EXAMPLE_NEW_COMMENT, result.getComment())
        );
    }
}
