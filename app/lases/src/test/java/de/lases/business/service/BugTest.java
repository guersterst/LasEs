package de.lases.business.service;

import de.lases.global.transport.User;
import de.lases.persistence.repository.ConnectionPool;
import org.junit.jupiter.api.Test;

public class BugTest {

    @Test
    void test() {
        ConnectionPool.init();
        User user = new User();
        user.setId(1);
        UserService userService = new UserService();

        User result = userService.get(user);
    }
}
