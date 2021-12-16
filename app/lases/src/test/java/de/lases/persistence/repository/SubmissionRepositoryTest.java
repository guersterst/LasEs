package de.lases.persistence.repository;

import de.lases.global.transport.*;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WeldJunit5Extension.class)
class SubmissionRepositoryTest {

    private static final int EXAMPLE_SUBMISSION_ID_1 = 1;
    private static final int EXAMPLE_SUBMISSION_ID_2 = 2;
    private static int EXAMPLE_USER_ID;
    private static final String EXAMPLE_SUBMISSION_TITLE_1 = "Submission title";
    private static final String EXAMPLE_SUBMISSION_TITLE_2 = "Different title";


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

        Class clazz = SubmissionRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }


//    @BeforeAll
//    static void addUser() throws Exception {
//        Transaction transaction = new Transaction();
//        UserRepository.add(new User(), transaction);
//        EXAMPLE_USER_ID = UserRepository.getList(transaction, new ResultListParameters()).get(0).getId();
//        transaction.commit();
//    }

    @Test
    void testAddSubmission() throws DataNotWrittenException, SQLException {
        Submission submission = new Submission();
        submission.setScientificForumId(1);
        submission.setAuthorId(4);
        submission.setEditorId(1);
        submission.setTitle("Sebastian testet die add Methode!");
        submission.setState(SubmissionState.ACCEPTED);
        submission.setSubmissionTime(LocalDateTime.now());

        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                """
                        SELECT * FROM submission
                        """);
        ResultSet resultSet = stmt.executeQuery();
        int i = 0;
        while (resultSet.next()) {
            i++;
        }

        SubmissionRepository.add(submission, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }

        assertEquals(1, j - i);
        transaction.abort();
    }

    @Test
    void testAddCoAuthorBasic() throws SQLException, DataNotWrittenException, NotFoundException {
        Submission submission = new Submission();
        submission.setId(671);

        User user = new User();
        user.setId(69);

        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                """
                        SELECT * FROM co_authored
                        """);
        ResultSet resultSet = stmt.executeQuery();
        int i = 0;
        while (resultSet.next()) {
            i++;
        }

        SubmissionRepository.addCoAuthor(submission, user, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }
        transaction.abort();
        assertEquals(1, j - i);
    }

    @Test
    void testAddSubmissionNotFoundException() throws DataNotWrittenException, NotFoundException {
        Submission submission = new Submission();
        Transaction transaction = new Transaction();
        // Diese Submission sollte nicht existieren in der Datenbank
        submission.setId(900);

        User user = new User();
        user.setId(69);

        assertThrows(NotFoundException.class, () -> SubmissionRepository.addCoAuthor(submission, user, transaction));
        transaction.abort();
    }

    @Test
    void testAddCoAuthorNotFoundException() throws DataNotWrittenException, NotFoundException {
        Submission submission = new Submission();
        Transaction transaction = new Transaction();
        // Diese Submission sollte nicht existieren in der Datenbank
        submission.setId(666);

        User user = new User();
        user.setId(2000);

        assertThrows(NotFoundException.class, () -> SubmissionRepository.addCoAuthor(submission, user, transaction));
        transaction.abort();
    }

    @Test
    void testAddReviewer() throws SQLException, DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Connection connection = transaction.getConnection();

        PreparedStatement statement = connection.prepareStatement("""
                SELECT * FROM reviewed_by
                """);

        ResultSet resultSetBefore = statement.executeQuery();
        int before = 0;
        while (resultSetBefore.next()){
            before++;
        }

        ReviewedBy reviewedBy = new ReviewedBy();
        reviewedBy.setReviewerId(1);
        reviewedBy.setSubmissionId(5);
        reviewedBy.setHasAccepted(AcceptanceStatus.NO_DECISION);
        reviewedBy.setTimestampDeadline(LocalDateTime.now());

        SubmissionRepository.addReviewer(reviewedBy, transaction);

        ResultSet resultSetAfter = statement.executeQuery();
        int after = 0;
        while (resultSetAfter.next()) {
            after++;
        }

        assertEquals(before, after - 1);
        transaction.abort();
    }

    void testAddAndGetList() throws Exception {
        Transaction transaction = new Transaction();

        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        Submission submission1 = new Submission();
        submission1.setId(EXAMPLE_SUBMISSION_ID_1);
        submission1.setAuthorId(EXAMPLE_USER_ID);
        submission1.setTitle(EXAMPLE_SUBMISSION_TITLE_1);

        Submission submission2 = new Submission();
        submission2.setId(EXAMPLE_SUBMISSION_ID_2);
        submission2.setEditorId(EXAMPLE_USER_ID);
        submission2.setTitle(EXAMPLE_SUBMISSION_TITLE_2);

        SubmissionRepository.add(submission1, transaction);
        SubmissionRepository.add(submission2, transaction);

        List<Submission> authoredList = SubmissionRepository
                .getList(user, Privilege.AUTHOR, transaction, null);
        List<Submission> editedList = SubmissionRepository
                .getList(user, Privilege.EDITOR, transaction, null);

        assertAll(
                () -> assertEquals(1, authoredList.size()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE_1, authoredList.get(0).getTitle()),
                () -> assertEquals(1, editedList.size()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE_2, editedList.get(0).getTitle())
        );
        transaction.abort();
    }

    void testAddAndAbort() throws Exception {
        Transaction transaction = new Transaction();
        Submission submission = new Submission();
        submission.setId(EXAMPLE_SUBMISSION_ID_1);
        submission.setAuthorId(EXAMPLE_USER_ID);
        submission.setTitle(EXAMPLE_SUBMISSION_TITLE_1);
        SubmissionRepository.add(submission, transaction);
        transaction.abort();
        List<Submission> authoredList = SubmissionRepository.getList(new User(), Privilege.AUTHOR, transaction, null);
        assertEquals(0, authoredList.size());
    }

    // add two submissions and test countSubmissions()
    void testCountSubmissions() throws Exception {
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        Submission submission1 = new Submission();
        submission1.setId(EXAMPLE_SUBMISSION_ID_1);
        submission1.setAuthorId(EXAMPLE_USER_ID);
        submission1.setTitle(EXAMPLE_SUBMISSION_TITLE_1);

        Submission submission2 = new Submission();
        submission2.setId(EXAMPLE_SUBMISSION_ID_2);
        submission2.setEditorId(EXAMPLE_USER_ID);
        submission2.setTitle(EXAMPLE_SUBMISSION_TITLE_2);

        Transaction transaction = new Transaction();

        SubmissionRepository.add(submission1, transaction);
        SubmissionRepository.add(submission2, transaction);

        assertEquals(1, SubmissionRepository.countSubmissions(user, Privilege.AUTHOR, transaction, null));

        transaction.abort();
    }
    
}