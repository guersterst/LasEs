package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@SessionScoped
public class SessionInformation implements Serializable {
    private User user;
    private Locale locale;

    public void invalidateSession() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
