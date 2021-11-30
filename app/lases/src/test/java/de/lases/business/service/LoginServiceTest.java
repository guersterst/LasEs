package de.lases.business.service;

import de.lases.global.transport.Submission;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final String SECURE_EXAMPLE_PASSWORD = "Affe123!";
    private static final String VERY_RANDOM_SALT_BASE64 = "QWZmZW4gc2luZCBzdXBlcg==";
    // PBKDF2WithHmacSHA256, 128 bit, 75000 iterations. https://gist.github.com/schicho/616fb9529b624000a577344388ce3f3b
    private static final String SECURE_PASSWORD_HASH_BASE64 = "YXirI3Xsv1hhne/qI+um6Q==";

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