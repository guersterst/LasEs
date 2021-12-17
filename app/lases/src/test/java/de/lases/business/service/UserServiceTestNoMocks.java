package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Privilege;
import de.lases.global.transport.User;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Johannes Garstenauer
 */
@ExtendWith(WeldJunit5Extension.class)
public class UserServiceTestNoMocks {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */

    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class clazz = UserServiceTestNoMocks.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testGet() {
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
                () -> assertTrue(userService.get(user).getPrivileges().contains(Privilege.EDITOR))
        );
    }
}
