package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
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
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
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

    private Submission submissionInstance;
    private ScientificForum scientificForum;
    private User author1;
    private User author2;
    private User reviewer1;

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() throws Exception {
        FileDTO file = new FileDTO();

        Class<UserRepositoryGetListNoMocksTest> clazz = UserRepositoryGetListNoMocksTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        transaction = new Transaction();

        createData();
    }

    void createData() throws Exception {
        author1 = new User();
        author1.setFirstName("Otto");
        author1.setLastName("Motor");
        author1.setEmailAddress("otto.motor@example.com");
        author1.setAdmin(false);

        author1 = UserRepository.add(author1, transaction);

        author2 = new User();
        author2.setFirstName("Toto");
        author2.setLastName("Wolff");
        author2.setEmailAddress("toto.wolff@example.com");
        author2.setAdmin(false);

        author2 = UserRepository.add(author2, transaction);

        reviewer1 = new User();
        reviewer1.setFirstName("Ronald");
        reviewer1.setLastName("Reviewer");
        reviewer1.setEmailAddress("ronald.reviewer@example.com");
        reviewer1.setAdmin(false);

        reviewer1 = UserRepository.add(reviewer1, transaction);

        scientificForum = new ScientificForum();
        scientificForum.setName("The Future of Motorsports");

        scientificForum = ScientificForumRepository.add(scientificForum, transaction);
        // valid values so the repositories do not complain. Please note author2 is editor.
        ScientificForumRepository.addEditor(scientificForum, author2, transaction);

        Submission submission = new Submission();
        submission.setTitle("Bring the V12s back.");
        submission.setState(SubmissionState.SUBMITTED);
        submission.setRevisionRequired(false);
        submission.setSubmissionTime(LocalDateTime.now());
        // valid values so the repositories do not complain.
        submission.setAuthorId(author1.getId());
        submission.setEditorId(author2.getId());
        submission.setScientificForumId(scientificForum.getId());

        submissionInstance = SubmissionRepository.add(submission, transaction);

        SubmissionRepository.addCoAuthor(submission, author2, transaction);

        ReviewedBy reviewedBy = new ReviewedBy();
        reviewedBy.setHasAccepted(AcceptanceStatus.ACCEPTED);
        reviewedBy.setTimestampDeadline(LocalDateTime.now());
        reviewedBy.setSubmissionId(submissionInstance.getId());
        reviewedBy.setReviewerId(reviewer1.getId());

        SubmissionRepository.addReviewer(reviewedBy, transaction);
    }

    @AfterEach
    void shutDownConnectionPool() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    private static Transaction transaction;

    @Test
    void testGetListUsersForSubmissionAuthors() throws NotFoundException, DataNotCompleteException {
        List<User> userList = UserRepository.getList(transaction, submissionInstance, Privilege.AUTHOR);

        assertAll(
                () -> assertEquals(2, userList.size()),
                () -> assertTrue(userList.contains(author1)),
                () -> assertTrue(userList.contains(author2))
        );
    }

    @Test
    void testGetListUsersForSubmissionReviewers() throws NotFoundException, DataNotCompleteException {
        List<User> userList = UserRepository.getList(transaction, submissionInstance, Privilege.REVIEWER);

        assertAll(
                () -> assertEquals(1, userList.size()),
                () -> assertTrue(userList.contains(reviewer1))
        );
    }

    @Test
    void testGetListOfEditors() throws DataNotCompleteException, NotFoundException {
        List<User> userList = UserRepository.getList(transaction, scientificForum);

        assertAll(
                () -> assertEquals(1, userList.size()),
                () -> assertTrue(userList.contains(author2))
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
