package de.lases.business.service;

import de.lases.global.transport.Privilege;
import de.lases.global.transport.Review;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    private static final int EXAMPLE_SUBMISSION_ID = 2;
    private static final int EXAMPLE_REVIEW_VERSION = 3;
    private static final int EXAMPLE_USER_ID = 6;
    private static final String EXAMPLE_SUBMISSION_TITLE = "Submission title";

    static MockedStatic<SubmissionRepository> subRepo;
    static MockedStatic<ReviewRepository> reviewRepo;

    @BeforeAll
    static void mockRepositories() {
        Submission submissionFromRepo = new Submission();
        submissionFromRepo.setId(EXAMPLE_SUBMISSION_ID);
        submissionFromRepo.setTitle(EXAMPLE_SUBMISSION_TITLE);
        submissionFromRepo.setAuthorId(EXAMPLE_USER_ID);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        subRepo = mockStatic(SubmissionRepository.class);
        subRepo.when(() -> SubmissionRepository.get(eq(submissionFromRepo), any(Transaction.class)))
                .thenReturn(submissionFromRepo);
        subRepo.when(() -> SubmissionRepository.getList(eq(user), Privilege.AUTHOR, any(Transaction.class), any()))
                .thenReturn(new Submission[]{submissionFromRepo});

        reviewRepo = mockStatic(ReviewRepository.class);
    }

    @AfterAll
    static void closeRepositoryMocks() {
        subRepo.close();
        reviewRepo.close();
    }

    @Test
    void testGet() {
        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID);

        SubmissionService submissionService = new SubmissionService();
        Submission gotten = submissionService.get(sub);

        assertAll(
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, gotten.getId()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE, gotten.getTitle())
        );
    }

    @Test
    void testGetNotFound() {
        Submission submissionFromRepo = new Submission();
        submissionFromRepo.setId(EXAMPLE_SUBMISSION_ID);
        submissionFromRepo.setTitle(EXAMPLE_SUBMISSION_TITLE);

        // Submission with different id
        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID + 1);

        subRepo.when(() -> SubmissionRepository.get(eq(sub), any())).thenReturn(submissionFromRepo);

        SubmissionService submissionService = new SubmissionService();

        assertThrows(NotFoundException.class, () -> submissionService.get(sub));
    }

    @Test
    void testReleaseReview() {
        Submission submission = new Submission();
        submission.setId(EXAMPLE_SUBMISSION_ID);
        Review review = new Review();
        review.setPaperVersion(EXAMPLE_REVIEW_VERSION);
        review.setSubmissionId(EXAMPLE_SUBMISSION_ID);
        SubmissionService submissionService = new SubmissionService();

        submissionService.releaseReview(review, submission);

        reviewRepo.verify(() -> ReviewRepository.change(eq(review), any(Transaction.class)), times(1));
    }

    @Test
    void testGetOwnSubmissions() {
        User user = new User();
        user.setId(EXAMPLE_USER_ID);
        Submission submission = new Submission();
        submission.setId(EXAMPLE_SUBMISSION_ID);

        SubmissionService submissionService = new SubmissionService();
        List<Submission> result = submissionService.getList(Privilege.AUTHOR, user, null);
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(submission, result.get(0))
        );
    }

    @Test
    void testGetOwnSubmissionEmpty() {
        // different user to example user
        User user = new User();
        user.setId(EXAMPLE_USER_ID + 1);

        SubmissionService submissionService = new SubmissionService();
        List<Submission> result = submissionService.getList(Privilege.AUTHOR, user, null);
        assertEquals(0, result.size());
    }

}