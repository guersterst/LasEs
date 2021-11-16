package de.lases.control.internal;

import de.lases.global.transport.*;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@SessionScoped
public class SessionInformation implements Serializable {
    private int userID;
    private List<Privilege> roles;
    private Locale locale;

    public void invalidateSession() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<Privilege> getRoles() {
        return roles;
    }

    public void setRoles(List<Privilege> roles) {
        this.roles = roles;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
