package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class UIMessageGenerator {

    @Inject
    FacesContext facesContext;

    @Inject
    public UIMessageGenerator(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

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
