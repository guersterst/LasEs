package de.lases.control.backing;

import de.lases.business.service.ScientificForumService;
import de.lases.business.service.UserService;
import de.lases.business.util.EmailUtil;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Backing bean for the user list page.
 */
@ViewScoped
@Named
public class UserListBacking implements Serializable, UserPaginationBacking {

    @Serial
    private static final long serialVersionUID = -1656064422555982220L;

    @Inject
    private SessionInformation sessionInformation;

    @Inject
    private UserService userService;

    private Pagination<User> userPagination;

    private void initPagination() {
        //todo works?
        userPagination = new Pagination<>("lastname") {

            @Override
            public void loadData() {
                setEntries(userService.getList(getResultListParameters()));
            }

            @Override
            protected Integer calculateNumberPages() {
                return userService.getListCountPages(userPagination.getResultListParameters());
            }
        };
        userPagination.applyFilters();
        userPagination.loadData();
    }

    /**
     * Initialize the user pagination and load the first page from the
     * datasource.
     *
     * @throws de.lases.control.exception.IllegalAccessException If the user
     *                                                           accessing this
     *                                                           page is not
     *                                                           an admin or
     *                                                           editor.
     */
    @PostConstruct
    public void init() {
        initPagination();
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

    public String generateMailTo(String recipient) {
        return EmailUtil.generateMailToLink(new String[]{recipient}, null, null, null);
    }

}
