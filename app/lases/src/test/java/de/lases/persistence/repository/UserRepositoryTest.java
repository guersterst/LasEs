package de.lases.persistence.repository;

import de.lases.business.service.UserService;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.User;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
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

@ExtendWith(WeldJunit5Extension.class)
class UserRepositoryTest {

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

        Class clazz = ScienceFieldRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    private static final String EXAMPLE_EMAIL_ADDRESS = "john.doe@foo.bar";

    @Test
    void testAddEmailExists() throws Exception {
        User user = new User();
        user.setEmailAddress(EXAMPLE_EMAIL_ADDRESS);

        Transaction transaction = new Transaction();
        UserRepository.add(user, transaction);
        assertTrue(UserRepository.emailExists(user, transaction));
        transaction.abort();
    }

    @Test
    void testEmailDoesNotExist() throws Exception {
        User user = new User();
        user.setEmailAddress(EXAMPLE_EMAIL_ADDRESS);

        Transaction transaction = new Transaction();
        UserRepository.remove(user, transaction);
        assertFalse(UserRepository.emailExists(user, transaction));
        transaction.abort();
    }

    @Test
    void testGetUser() {
        ConnectionPool.init();
        User user = new User();
        user.setId(1);
        Transaction t = new Transaction();
        try {
            assertNotNull(UserRepository.get(user, t));
        } catch (NotFoundException ex) {
            //
        } finally {
            t.commit();
        }
    }

    @Test
    void testSetAvatarUserDoesNotExist() {
        User user = new User();
        user.setId(-100);
        FileDTO file = new FileDTO();
        file.setFile(new byte[]{});
        Transaction transaction = new Transaction();
        assertThrows(NotFoundException.class, () -> UserRepository.setAvatar(user, file, transaction));
        transaction.abort();
    }

}