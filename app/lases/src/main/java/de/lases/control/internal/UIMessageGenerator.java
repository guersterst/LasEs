package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

/**
 * Handles the delivery of ui-messages to the application's user.
 */
public class UIMessageGenerator {

    FacesContext facesContext;

    /**
     * Construct an {@link UIMessageGenerator}.
     *
     * @param facesContext The {@link FacesContext}.
     */
    @Inject
    public UIMessageGenerator(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    /**
     * Creates {@link FacesMessage}s from {@link UIMessage}s and inserts them into
     * the {@link FacesContext} to be displayed to the user.
     * <p>
     * This is a listener method and does not need to be called.
     * It listens to each {@link jakarta.enterprise.event.Event} of the type {@code Event<UIMessage>}.
     * </p>
     * @param uiMessage The {@code UIMessage} contained in the {@code Event<UIMessage>},
     *                  holding information about the message's severity and content.
     */
    public void display(@Observes @Any UIMessage uiMessage) {
        FacesMessage facesMessage = new FacesMessage(convertCategory(uiMessage.getCategory()), uiMessage.getMessage(),
                uiMessage.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    private FacesMessage.Severity convertCategory(MessageCategory messageCategory) {
        return switch (messageCategory) {
            case ERROR -> FacesMessage.SEVERITY_ERROR;
            case INFO -> FacesMessage.SEVERITY_INFO;
            case WARNING -> FacesMessage.SEVERITY_WARN;
            case FATAL -> FacesMessage.SEVERITY_FATAL;
        };
    }
}
