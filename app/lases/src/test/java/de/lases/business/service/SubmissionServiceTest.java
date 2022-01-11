package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.ReviewRepository;
import de.lases.persistence.repository.SubmissionRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(WeldJunit5Extension.class)
@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    // id of submission in mocked repository
    private static final int FIRST_SUBMISSION_ID = 0;
    private static final int EXAMPLE_REVIEW_VERSION = 3;
    private static final int EXAMPLE_USER_ID = 6;
    private static final String EXAMPLE_SUBMISSION_TITLE = "Submission title";

    static MockedStatic<SubmissionRepository> subRepo;
    static MockedStatic<ReviewRepository> reviewRepo;

    private SubmissionService submissionService;

    @Mock
    private PropertyResourceBundle bundle;

    @Mock
    private Event<UIMessage> uiMessageEvent;

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    @BeforeAll
    static void mockRepositories() {
        Submission submissionFromRepo = new Submission();
        submissionFromRepo.setId(FIRST_SUBMISSION_ID);
        submissionFromRepo.setTitle(EXAMPLE_SUBMISSION_TITLE);
        submissionFromRepo.setAuthorId(EXAMPLE_USER_ID);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        subRepo = mockStatic(SubmissionRepository.class);
        subRepo.when(() -> SubmissionRepository.get(eq(submissionFromRepo), any(Transaction.class)))
                .thenReturn(submissionFromRepo);
        subRepo.when(() -> SubmissionRepository.getList(eq(user), eq(Privilege.AUTHOR), any(Transaction.class), any()))
                .thenReturn(List.of(submissionFromRepo));

        reviewRepo = mockStatic(ReviewRepository.class);
    }

    @BeforeEach
    void setServiceDependencies() throws Exception {
        submissionService = new SubmissionService();

        Field bundleField = submissionService.getClass().getDeclaredField("resourceBundle");
        bundleField.setAccessible(true);
        bundleField.set(submissionService, bundle);

        Field uiMessageEventField = submissionService.getClass().getDeclaredField("uiMessageEvent");
        uiMessageEventField.setAccessible(true);
        uiMessageEventField.set(submissionService, uiMessageEvent);
    }

    @AfterAll
    static void closeRepositoryMocks() {
        subRepo.close();
        reviewRepo.close();
    }


    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class c = SubmissionServiceTest.class;
        InputStream inputStream = c.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testGet() {
        Submission sub = new Submission();
        sub.setId(FIRST_SUBMISSION_ID);

        Submission gotten = submissionService.get(sub);

        assertAll(
                () -> assertEquals(FIRST_SUBMISSION_ID, gotten.getId()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE, gotten.getTitle())
        );
    }

    @Test
    void testGetNotFound() throws Exception {
        Submission submissionFromRepo = new Submission();
        submissionFromRepo.setId(FIRST_SUBMISSION_ID);
        submissionFromRepo.setTitle(EXAMPLE_SUBMISSION_TITLE);

        // Submission with different id
        Submission sub = new Submission();
        sub.setId(FIRST_SUBMISSION_ID + 1);

        subRepo.when(() -> SubmissionRepository.get(eq(sub), any())).thenThrow(new NotFoundException());

        Field field = submissionService.getClass().getDeclaredField("resourceBundle");
        field.setAccessible(true);
        field.set(submissionService, bundle);

        assertNull(submissionService.get(sub));
    }

    @Test
    void testGetOwnSubmissions() {
        User user = new User();
        user.setId(EXAMPLE_USER_ID);
        Submission submission = new Submission();
        submission.setId(FIRST_SUBMISSION_ID);

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

        List<Submission> result = submissionService.getList(Privilege.AUTHOR, user, null);
        assertEquals(0, result.size());
    }

}