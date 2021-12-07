package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Encapsulates all information that is needed for the management of the
 * session. More specifically, it holds the role of the user and his locale. It
 * also offers a way to invalidate a session.
 */
@SessionScoped
@Named
public class SessionInformation implements Serializable {
    private User user;

    /**
     * Get the user that is currently logged in. The user dto must be filled
     * out with his id and privileges.
     *
     * @return A user dto with an id and privileges.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user that is currently logged in. This will also change the
     * sessionId to avoid session fixation.
     *
     * @param user A user dto filled with an id and privileges for the user that is logged.
     */
    public void setUser(User user) {
        this.user = user;

        // Change the session's id in order to prevent session fixation.
       ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
       if (externalContext.getSession(false) != null) {
           ((HttpServletRequest) externalContext.getRequest()).changeSessionId();
       }
    }

}
