package de.lases.control.backing;

import de.lases.business.service.*;
import de.lases.control.exception.IllegalAccessException;
import de.lases.control.internal.SessionInformation;
import de.lases.global.transport.Privilege;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionBackingTest {

    private static final int EXAMPLE_SUBMISSION_ID = 2;
    private static final int EXAMPLE_USER_ID = 6;

    @Mock SessionInformation sessionInformation;

    @Mock SubmissionService submissionService;

    @Mock UserService userService;

    @Mock PaperService paperService;

    @Mock ReviewService reviewService;

    @Mock ScientificForumService scientificForumService;

    SubmissionBacking submissionBacking = new SubmissionBacking();

    @BeforeEach
    void setBackingDependencies() throws Exception{
        Field submissionServiceField = submissionBacking.getClass().getDeclaredField("submissionService");
        submissionServiceField.setAccessible(true);
        submissionServiceField.set(submissionBacking, submissionService);

        Field userServiceField = submissionBacking.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(submissionBacking, userService);

        Field paperServiceField = submissionBacking.getClass().getDeclaredField("paperService");
        paperServiceField.setAccessible(true);
        paperServiceField.set(submissionBacking, paperService);

        Field reviewServiceField = submissionBacking.getClass().getDeclaredField("reviewService");
        reviewServiceField.setAccessible(true);
        reviewServiceField.set(submissionBacking, reviewService);

        Field scientificForumServiceField = submissionBacking.getClass().getDeclaredField("scientificForumService");
        scientificForumServiceField.setAccessible(true);
        scientificForumServiceField.set(submissionBacking, scientificForumService);

        Field sessionInformationField = submissionBacking.getClass().getDeclaredField("sessionInformation");
        sessionInformationField.setAccessible(true);
        sessionInformationField.set(submissionBacking, sessionInformation);
    }

    @Test
    void testInitAndOnLoad() throws Exception{
        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        when(sessionInformation.getUser()).thenReturn(user);

        when(submissionService.get(sub)).thenReturn(sub);

        submissionBacking.init();
        // Fake view params
        Field submissionField = submissionBacking.getClass().getDeclaredField("submission");
        submissionField.setAccessible(true);
        submissionField.set(submissionBacking, sub);
        submissionBacking.onLoad();

        verify(submissionService).get(sub);
        verify(userService).getList(sub, Privilege.AUTHOR);
        verify(paperService).getList(eq(sub), eq(user), any());
        // TODO mergen von basti
        verify(reviewService).getList(eq(sub), eq(user), any());
        // TODO steffi
        verify(submissionService).getReviewedBy(eq(sub), eq(user));
    }

    @Test
    void testInitAndOnLoadIllegal() throws Exception {
        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID);
        // User with no rights to view the submission
        User user = new User();
        user.setId(EXAMPLE_USER_ID + 1);

        when(sessionInformation.getUser()).thenReturn(user);
        when(submissionService.get(sub)).thenReturn(sub);

        submissionBacking.init();

        // Fake view params
        Field submissionField = submissionBacking.getClass().getDeclaredField("submission");
        submissionField.setAccessible(true);
        submissionField.set(submissionBacking, sub);

        assertThrows(IllegalAccessException.class, () -> submissionBacking.onLoad());
    }
}