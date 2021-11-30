package de.lases.business.service;

import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.global.transport.Verification;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import io.opentelemetry.sdk.metrics.data.SumData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    private static final int EXAMPLE_SUBMISSION_ID = 0;
    private static final String EXAMPLE_SUBMISSION_TITLE = "Submission title";

    @Test
    void testGet() throws NotFoundException {
        Submission submissionFromRepo = new Submission();
        submissionFromRepo.setId(EXAMPLE_SUBMISSION_ID);
        submissionFromRepo.setTitle(EXAMPLE_SUBMISSION_TITLE);

        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID);

        try (MockedStatic<SubmissionRepository> repo = mockStatic(SubmissionRepository.class)) {
            repo.when(() -> SubmissionRepository.get(sub, any())).thenReturn(submissionFromRepo);
        }

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

        try (MockedStatic<SubmissionRepository> repo = mockStatic(SubmissionRepository.class)) {
            repo.when(() -> SubmissionRepository.get(sub, any())).thenReturn(submissionFromRepo);
        }

        SubmissionService submissionService = new SubmissionService();

        assertThrows(NotFoundException.class, () -> submissionService.get(sub));
    }
}