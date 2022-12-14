package de.lases.control.internal;

import de.lases.global.transport.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serial;
import java.io.Serializable;

/**
 * Encapsulates all information that is needed for the management of the
 * session. More specifically, it holds the role of the user and his locale. It
 * also offers a way to invalidate a session.
 */
@SessionScoped
@Named
public class SessionInformation implements Serializable {

    @Serial
    private static final long serialVersionUID = 3862713300005056314L;

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
     * @param user A filled user dto.
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
