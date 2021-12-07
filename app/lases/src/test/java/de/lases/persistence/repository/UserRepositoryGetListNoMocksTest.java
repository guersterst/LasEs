package de.lases.persistence.repository;

import de.lases.global.transport.Privilege;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.persistence.exception.NotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryGetListNoMocksTest {

    private static Transaction transaction;

    @BeforeAll
    static void init() {
        ConnectionPool.init();
        transaction = new Transaction();
    }

    @AfterAll
    static void destruct() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    @Test
    void testGetListUsersForSubmissionAuthors() throws NotFoundException {
        Submission submission = new Submission();
        submission.setId(666);

        User basti = new User();
        basti.setId(420);

        User alfred = new User();
        alfred.setId(69);

        List<User> userList = UserRepository.getList(transaction, submission, Privilege.AUTHOR);

        assertAll(
                () -> assertEquals(2, userList.size()),
                () -> assertTrue(userList.contains(basti)),
                () -> assertTrue(userList.contains(alfred))
        );
    }

    @Test
    void testGetListUsersForSubmissionReviewers() throws NotFoundException {
        Submission submission = new Submission();
        submission.setId(666);

        User edith = new User();
        edith.setId(1001);

        List<User> userList = UserRepository.getList(transaction, submission, Privilege.REVIEWER);

        assertAll(
                () -> assertEquals(1, userList.size()),
                () -> assertTrue(userList.contains(edith))
        );
    }
}
