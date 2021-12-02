package de.lases.control.internal;

import de.lases.business.service.SubmissionService;
import de.lases.global.transport.MessageCategory;
import de.lases.global.transport.Submission;
import de.lases.global.transport.UIMessage;
import jakarta.enterprise.event.Event;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UIMessageGeneratorTest {

    @Mock
    SubmissionService mockedSubmissionService;

    @Mock
    FacesContext facesContext;

    @Mock
    Event<UIMessage> uiMessageEvent;

    static UIMessage uiMessage;

    @BeforeAll
    static void setEventBehaviour() {
        //todo is this correct here, mock resbundle?
        uiMessage = new UIMessage("Internal Error", MessageCategory.ERROR);
    }

    @Test
    void eventFiredTest() {
        when(mockedSubmissionService.get(Mockito.any(Submission.class))).thenAnswer(
                invocationOnMock -> {
                    uiMessageEvent.fire(uiMessage);
                    return null;
                }
        );

        SubmissionService submissionService = new SubmissionService();
        Submission submission = new Submission();
        submission.setId(1);
        submissionService.get(submission);

        List<FacesMessage> messageList = facesContext.getMessageList();
        assertEquals(messageList.get(0).getDetail(), uiMessage.getMessage());
    }
}
