package de.lases.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import de.lases.persistence.repository.*;
import de.lases.global.transport.*;
import jakarta.enterprise.event.Event;
import jakarta.inject.Named;

import java.util.List;

@ApplicationScoped
@Named
public class UserService {

    // TODO @Johannes: Irgendwas zum verifien vom User fehlt (wahrscheinlich
    //  auch bei den Repos)

    private Event<UIMessage> uiMessageEvent;

    private Transaction transaction;

    public User getUser(User user) {
        return null;
    }

    public void editUser(User newUser) {
    }

    public void deleteUser(String email) {
    }

    public boolean emailExists(String email) {
        return false;
    }

    // byte[] gut?
    public void setAvatar() {
    }

    public File getAvatar() {
        return null;
    }

    public void deleteAvatar(String email) {
    }

    public List<User> getUsers(User user, ResultListParameters resultListParams) {
        return null;
    }
}
