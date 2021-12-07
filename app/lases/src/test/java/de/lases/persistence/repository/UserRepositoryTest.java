package de.lases.persistence.repository;

import de.lases.business.service.UserService;
import de.lases.global.transport.User;
import de.lases.persistence.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

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
}