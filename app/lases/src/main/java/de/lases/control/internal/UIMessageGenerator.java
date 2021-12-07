package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.io.Serial;
import java.io.Serializable;

/**
 * Handles the delivery of ui-messages to the application's user.
 */
@SessionScoped
public class UIMessageGenerator implements Serializable {

    @Serial
    private static final long serialVersionUID = 1796702584306481539L;

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
                null);
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
