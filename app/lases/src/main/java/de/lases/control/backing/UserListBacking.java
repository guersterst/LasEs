package de.lases.control.backing;

import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Backing bean for the user list page.
 */
@ViewScoped
@Named
public class UserListBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = -1656064422555982220L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private UserService userService;

    private Pagination<User> userPagination;

    /**
     * Initialize the user pagination and load the first page from the
     * datasource.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Get the pagination for the search results with users.
     *
     * @return The pagination for users.
     */
    public Pagination<User> getUserPagination() {
        return userPagination;
    }

    /**
     * Get session information.
     *
     * @return The session information.
     */
    public SessionInformation getSessionInformation() {
        return sessionInformation;
    }

}
