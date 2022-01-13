package de.lases.persistence.repository;

import de.lases.business.service.SubmissionService;
import de.lases.business.util.EmailUtil;
import de.lases.global.transport.*;
import de.lases.persistence.internal.ConfigReader;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WeldJunit5Extension.class)
public class ReviewedByREpositoryTest {

    private PropertyResourceBundle resourceBundle;

    private Event<UIMessage> uiMessageEvent;

    private MockedStatic<EmailUtil> emailUtilMockedStatic;

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

        Class clazz = ScienceFieldRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    /**
     * @author Stefanie Gürster
     */
    @Test
    void testRemoveReviewer() throws IllegalAccessException, NoSuchFieldException {
        SubmissionService submissionService = new SubmissionService();

        emailUtilMockedStatic = Mockito.mockStatic(EmailUtil.class);

        resourceBundle = Mockito.mock(PropertyResourceBundle.class);
        Mockito.when(resourceBundle.getString(Mockito.anyString())).thenReturn("placeholder");
        Field bundleField = submissionService.getClass().getDeclaredField("resourceBundle");
        bundleField.setAccessible(true);
        bundleField.set(submissionService, resourceBundle);

        uiMessageEvent =  Mockito.mock(jakarta.enterprise.event.Event.class);
        Field eventField = submissionService.getClass().getDeclaredField("uiMessageEvent");
        eventField.setAccessible(true);
        eventField.set(submissionService, uiMessageEvent);

        Submission submission = new Submission();
        submission.setId(742);

        User reviewer = new User();
        reviewer.setId(169);
        reviewer.setEmailAddress("guerster@fim.uni-passau.de");

        ReviewedBy reviewedBy = new ReviewedBy();
        reviewedBy.setReviewerId(reviewer.getId());
        reviewedBy.setSubmissionId(submission.getId());
        reviewedBy.setTimestampDeadline(LocalDateTime.now());
        reviewedBy.setHasAccepted(AcceptanceStatus.REJECTED);

        submissionService.manageReviewer(reviewer,reviewedBy, new ArrayList<>());

        List<ReviewedBy> reviewedByList = submissionService.getList(submission);

        submissionService.removeReviewer(submission, reviewer);

        List<ReviewedBy> after = submissionService.getList(submission);

        assertEquals(reviewedByList.size(), after.size() + 1);
    }
}
