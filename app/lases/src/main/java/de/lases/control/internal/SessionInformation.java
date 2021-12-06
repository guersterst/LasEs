package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

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
    private Locale locale;

    /**
     * Get the user that is currently logged in. The user dto must be filled
     * out with his id and privileges.
     *
     * @return A user dto with an id.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user that is currently logged in. This will also change the
     * sessionId to avoid session fixation.
     *
     * @param user A user dto filled with an id for the user that is logged in.
     */
    public void setUser(User user) {
        this.user = user;
    }

}
