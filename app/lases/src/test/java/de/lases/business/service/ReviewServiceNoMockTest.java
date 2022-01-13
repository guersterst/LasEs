package de.lases.business.service;

import de.lases.business.util.EmailUtil;
import de.lases.global.transport.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

/**
 * @author Johann Schicho
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(WeldJunit5Extension.class)
class ReviewServiceNoMockTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    @Mock
    private PropertyResourceBundle bundle;

    @Mock
    private Event<UIMessage> uiMessageEvent;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private PaperService paperService;

    private MockedStatic<EmailUtil> emailUtilMockedStatic;

    private ReviewService reviewService;

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() throws Exception {
        FileDTO file = new FileDTO();

        Class<ReviewServiceNoMockTest> clazz = ReviewServiceNoMockTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        // mock resource bundle
        lenient().when(bundle.getString(any())).thenReturn("");
        reviewService = new ReviewService();

        Field bundleFieldReview = reviewService.getClass().getDeclaredField("resourceBundle");
        bundleFieldReview.setAccessible(true);
        bundleFieldReview.set(reviewService, bundle);

        // mock submisson service
        submissionService = new SubmissionService();
        Field bundleFieldSubmission = submissionService.getClass().getDeclaredField("resourceBundle");
        bundleFieldSubmission.setAccessible(true);
        bundleFieldSubmission.set(submissionService, bundle);

        //mock paper service
        paperService = new PaperService();
        Field bundleFieldPaper = paperService.getClass().getDeclaredField("resourceBundle");
        bundleFieldPaper.setAccessible(true);
        bundleFieldPaper.set(paperService, bundle);

        // mock ui message event
        Field uiMessageEventField = reviewService.getClass().getDeclaredField("uiMessageEvent");
        uiMessageEventField.setAccessible(true);
        uiMessageEventField.set(reviewService, uiMessageEvent);

        // mock email util
        emailUtilMockedStatic = Mockito.mockStatic(EmailUtil.class);
        emailUtilMockedStatic.when(() -> EmailUtil.generateLinkForEmail(any(), any())).thenReturn("");
        emailUtilMockedStatic.when(() -> EmailUtil.sendEmail(any(), any(), any(), any())).then(invocationOnMock -> null);
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
        emailUtilMockedStatic.close();
    }

    private Submission addTestSubmission() {
        Submission submission = new Submission();
        submission.setScientificForumId(1);
        submission.setAuthorId(420);
        submission.setEditorId(1);
        submission.setSubmissionTime(LocalDateTime.now());
        submission.setTitle("Bastis Test Einreichung");
        submission.setState(SubmissionState.ACCEPTED);
        submission.setSubmissionTime(LocalDateTime.now());

        Paper paper = new Paper();
        paper.setUploadTime(LocalDateTime.now());

        FileDTO file = new FileDTO();
        file.setFile(new byte[]{1, 2, 3, 4, 5, 6});

        return submissionService.add(submission, new ArrayList<>(), paper, file);
    }

    private void deleteTestSubmission(Submission submission) {
        submissionService.remove(submission);
    }

    private void addTestPaper(Submission submission) {
        Paper paper = new Paper();
        paper.setVisible(true);
        paper.setSubmissionId(submission.getId());
        paper.setUploadTime(LocalDateTime.now());

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFile(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        paperService.add(fileDTO, paper);
    }


    @Test
    void testAddGetReviewAdmin() {
        User user = new User();
        user.setId(420);
        user.setPrivileges(List.of(Privilege.ADMIN));

        Submission submission = addTestSubmission();
        addTestPaper(submission);

        Review review = new Review();
        review.setSubmissionId(submission.getId());
        review.setVisible(false);
        review.setPaperVersion(1);
        review.setReviewerId(702);
        review.setComment("Wichtiger Comment");

        reviewService.add(review, new FileDTO());

        List<Review> reviewList = reviewService.getList(submission, user, new ResultListParameters());

        assertEquals(1, reviewList.size());

        deleteTestSubmission(submission);
    }

    @Test
    void testAddGetReviewAuthor() {
        User user = new User();
        user.setId(420);
        user.setPrivileges(List.of(Privilege.AUTHOR));

        Submission submission = addTestSubmission();
        addTestPaper(submission);

        Review review = new Review();
        review.setSubmissionId(submission.getId());
        review.setVisible(false);
        review.setPaperVersion(1);
        review.setReviewerId(702);
        review.setComment("Wichtiger Comment");

        reviewService.add(review, new FileDTO());

        List<Review> reviewList = reviewService.getList(submission, user, new ResultListParameters());

        assertEquals(0, reviewList.size());

        deleteTestSubmission(submission);
    }

}