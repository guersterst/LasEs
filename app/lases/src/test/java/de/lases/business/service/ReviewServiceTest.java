package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Paper;
import de.lases.global.transport.Review;
import de.lases.global.transport.UIMessage;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

/**
 * @author Johannes Garstenauer
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(WeldJunit5Extension.class)
public class ReviewServiceTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */

    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class clazz = ReviewServiceTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

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
    private static final byte[] EXAMPLE_PDF = new byte[]{1, 2, 3, 4};
    private static final String EXAMPLE_OLD_COMMENT = "Very good!";
    private static final String EXAMPLE_NEW_COMMENT = "MY EYES ARE BLEEDING";

    @BeforeAll
    static void init() throws NoSuchFieldException, IllegalAccessException {

        // Mock repository and initialize service.
        reviewRepoMocked = mockStatic(ReviewRepository.class);
        reviewService = new ReviewService();

        // Mock get to return a review if the ids are correct.
        reviewRepoMocked.when(() -> ReviewRepository.get(eq(review), any(Transaction.class))).thenReturn(review);
        reviewRepoMocked.when(() -> ReviewRepository.getPDF(eq(review), any(Transaction.class))).thenReturn(pdf);

        Event<UIMessage> uiMessageEvent = Mockito.mock(Event.class);
        Field uiMessageField = reviewService.getClass().getDeclaredField("uiMessageEvent");
        uiMessageField.setAccessible(true);
        uiMessageField.set(reviewService, uiMessageEvent);

        PropertyResourceBundle bundle = Mockito.mock(PropertyResourceBundle.class);
        Field bundleField = reviewService.getClass().getDeclaredField("resourceBundle");
        bundleField.setAccessible(true);
        bundleField.set(reviewService, bundle);
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
