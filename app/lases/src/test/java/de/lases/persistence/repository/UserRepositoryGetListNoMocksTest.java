package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TESTINFO @basti not working
 * @author Johann Schicho
 */
@ExtendWith(WeldJunit5Extension.class)
public class UserRepositoryGetListNoMocksTest {

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

        Class reviewService = UserRepositoryGetListNoMocksTest.class;
        InputStream inputStream = reviewService.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        transaction = new Transaction();
    }

    @AfterEach
    void shutDownConnectionPool() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    private static Transaction transaction;

    @Test
    void testGetListUsersForSubmissionAuthors() throws NotFoundException, DataNotCompleteException {
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
    void testGetListUsersForSubmissionReviewers() throws NotFoundException, DataNotCompleteException {
        Submission submission = new Submission();
        submission.setId(666);

        User carl = new User();
        carl.setId(1003);

        List<User> userList = UserRepository.getList(transaction, submission, Privilege.REVIEWER);

        assertAll(
                () -> assertEquals(2, userList.size()),
                () -> assertTrue(userList.contains(carl))
        );
    }

    @Test
    void testGetListOfEditors() throws DataNotCompleteException, NotFoundException {
        ScientificForum sf = new ScientificForum();
        sf.setId(1);

        User edith = new User();
        edith.setId(1001);
        User peter = new User();
        peter.setId(1);

        List<User> userList = UserRepository.getList(transaction, sf);

        assertAll(
                () -> assertEquals(8, userList.size()),
                () -> assertTrue(userList.contains(edith)),
                () -> assertFalse(userList.contains(peter))
        );
    }

    @Test
    void testGetListOfEditorsInvalidForum() {
        ScientificForum sf = new ScientificForum();
        sf.setId(-1234);

        assertThrows(NotFoundException.class,
                () -> UserRepository.getList(transaction, sf));
    }
}
