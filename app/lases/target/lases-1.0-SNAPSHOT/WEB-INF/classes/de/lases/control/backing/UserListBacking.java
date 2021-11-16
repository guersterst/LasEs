package de.lases.control.backing;

import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class UserListBacking implements Serializable {

    private SessionInformation sessionInformation;

    private Pagination<User> userPagination;

    private UserService userService;


    @PostConstruct
    public void init() {
    }

    public String applyFilter() {
        return null;
    }

    public void nameUp() {
    }

    public void nameDown() {
    }

    public void roleUp() {
    }

    public void roleDown() {
    }

    public List<User> getUserList() {
        return null;
    }


}
