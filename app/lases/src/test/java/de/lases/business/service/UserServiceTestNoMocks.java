package de.lases.business.service;

import de.lases.global.transport.Privilege;
import de.lases.global.transport.User;
import de.lases.persistence.repository.ConnectionPool;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTestNoMocks {

    @Test
    void testGet() {
        //TODO insert into

        ConnectionPool.init();
        User user = new User();
        user.setId(1);
        UserService userService = new UserService();

        assertAll(
                () -> assertEquals(1, userService.get(user).getId()),
                () -> assertEquals("Peter", userService.get(user).getFirstName()),
                () -> assertEquals("Peterson", userService.get(user).getLastName()),
                () -> assertEquals("peter@gmail.com", userService.get(user).getEmailAddress()),
                () -> assertNull(userService.get(user).getEmployer()),
                () -> assertFalse(userService.get(user).isAdmin()),
                () -> assertTrue(userService.get(user).isRegistered()),
                () -> assertTrue(userService.get(user).getPrivileges().contains(Privilege.EDITOR)),
                () -> assertEquals(1, userService.get(user).getNumberOfSubmissions())
        );
    }
}
