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
}
