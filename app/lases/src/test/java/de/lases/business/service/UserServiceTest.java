package de.lases.business.service;

import de.lases.global.transport.Privilege;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.global.transport.Verification;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private String EXAMPLE_VALIDATION_RANDOM="123457890abc";
    private static final int EXAMPLE_USER_ID = 6;

    private static MockedStatic<UserRepository> userRepo;

    @Mock
    UserRepository userRepository;

    @BeforeAll
    static void mockRepositories() {
        userRepo = mockStatic(UserRepository.class);
    }

    @AfterAll
    static void closeRepositoryMocks() {
        userRepo.close();
    }

    @Test
    void testGetVerification() {
        Verification verification = new Verification();
        verification.setValidationRandom(EXAMPLE_VALIDATION_RANDOM);
        verification.setVerified(true);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        userRepo.when(() -> UserRepository.get(eq(user), any())).thenReturn(verification);

        assertEquals(verification, new UserService().getVerification(user));
    }
}