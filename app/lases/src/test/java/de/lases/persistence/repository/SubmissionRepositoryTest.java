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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WeldJunit5Extension.class)
class SubmissionRepositoryTest {

    private static final int EXAMPLE_SUBMISSION_ID_1 = 1;
    private static final int EXAMPLE_SUBMISSION_ID_2 = 2;
    private static int EXAMPLE_USER_ID;
    private static int EXAMPLE_USER_ID_2;
    private static final String EXAMPLE_SUBMISSION_TITLE_1 = "Submission title";
    private static final String EXAMPLE_SUBMISSION_TITLE_2 = "Different title";

    private static Submission submission;

    @WeldSetup
    public WeldInitiator weld  = WeldInitiator.from(ConnectionPool.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    @BeforeAll
    static void initSubmission() {
        submission = new Submission();
        submission.setScientificForumId(1);
        submission.setAuthorId(4);
        submission.setEditorId(1);
        submission.setTitle("Sebastian testet die add Methode!");
        submission.setState(SubmissionState.ACCEPTED);
        submission.setSubmissionTime(LocalDateTime.now());
    }

    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class c = SubmissionRepositoryTest.class;
        InputStream inputStream = c.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddSubmission() throws DataNotWrittenException, SQLException {

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

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddCoAuthorBasic() throws SQLException, DataNotWrittenException, NotFoundException {
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

        submission = SubmissionRepository.add(submission, transaction);
        SubmissionRepository.addCoAuthor(submission, user, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }
        transaction.abort();
        assertEquals(1, j - i);
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddSubmissionNotFoundException() {
        Submission submission = new Submission();
        Transaction transaction = new Transaction();
        // Diese Submission sollte nicht existieren in der Datenbank
        submission.setId(900);

        User user = new User();
        user.setId(69);

        assertThrows(NotFoundException.class, () -> SubmissionRepository.addCoAuthor(submission, user, transaction));
        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddCoAuthorNotFoundException() {
        Submission submission = new Submission();
        Transaction transaction = new Transaction();
        // Diese Submission sollte nicht existieren in der Datenbank
        submission.setId(-666);

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

    static void addReferences(Transaction transaction) throws Exception {
        User user = new User();
        user.setFirstName("Joseph");
        user.setLastName("Adenuga");
        user.setEmailAddress("test@test.ts");
        user = UserRepository.add(user, transaction);
        EXAMPLE_USER_ID = user.getId();

        User user2 = new User();
        user2.setFirstName("Joseph");
        user2.setLastName("Adenuga");
        user2.setEmailAddress("test2@test.ts");
        user2 = UserRepository.add(user2, transaction);
        EXAMPLE_USER_ID_2 = user2.getId();
    }

    @Test
    void testAddAndGetList() throws Exception {
        Transaction transaction = new Transaction();
        addReferences(transaction);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        Submission submission1 = new Submission();
        submission1.setId(EXAMPLE_SUBMISSION_ID_1);
        submission1.setAuthorId(EXAMPLE_USER_ID);
        submission1.setEditorId(EXAMPLE_USER_ID_2);
        submission1.setTitle(EXAMPLE_SUBMISSION_TITLE_1);
        submission1.setScientificForumId(1);
        submission1.setState(SubmissionState.ACCEPTED);
        submission1.setSubmissionTime(LocalDateTime.now());

        Submission submission2 = new Submission();
        submission2.setId(EXAMPLE_SUBMISSION_ID_2);
        submission2.setAuthorId(EXAMPLE_USER_ID_2);
        submission2.setEditorId(EXAMPLE_USER_ID);
        submission2.setTitle(EXAMPLE_SUBMISSION_TITLE_2);
        submission2.setScientificForumId(1);
        submission2.setState(SubmissionState.SUBMITTED);
        submission2.setSubmissionTime(LocalDateTime.now());

        SubmissionRepository.add(submission1, transaction);
        SubmissionRepository.add(submission2, transaction);

        List<Submission> authoredList = SubmissionRepository
                .getList(user, Privilege.AUTHOR, transaction, new ResultListParameters());
        List<Submission> editedList = SubmissionRepository
                .getList(user, Privilege.EDITOR, transaction, new ResultListParameters());

        assertAll(
                () -> assertEquals(1, authoredList.size()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE_1, authoredList.get(0).getTitle()),
                () -> assertEquals(1, editedList.size()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE_2, editedList.get(0).getTitle())
        );
        transaction.abort();
    }

    @Test
    void testAddAndAbort() throws Exception {
        Transaction transaction = new Transaction();
        addReferences(transaction);
        Submission submission = new Submission();
        submission.setId(EXAMPLE_SUBMISSION_ID_1);
        submission.setAuthorId(EXAMPLE_USER_ID);
        submission.setEditorId(EXAMPLE_USER_ID_2);
        submission.setTitle(EXAMPLE_SUBMISSION_TITLE_1);
        submission.setScientificForumId(1);
        submission.setState(SubmissionState.ACCEPTED);
        submission.setSubmissionTime(LocalDateTime.now());
        SubmissionRepository.add(submission, transaction);
        transaction.abort();

        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        Transaction transaction2 = new Transaction();
        List<Submission> authoredList = SubmissionRepository.getList(user, Privilege.AUTHOR, transaction2,
                new ResultListParameters());
        assertEquals(0, authoredList.size());
        transaction2.abort();
    }

    @Test
    void testCountSubmissions() throws Exception {
        Transaction transaction = new Transaction();
        addReferences(transaction);

        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        Submission submission1 = new Submission();
        submission1.setId(EXAMPLE_SUBMISSION_ID_1);
        submission1.setAuthorId(EXAMPLE_USER_ID);
        submission1.setEditorId(EXAMPLE_USER_ID_2);
        submission1.setTitle(EXAMPLE_SUBMISSION_TITLE_1);
        submission1.setScientificForumId(1);
        submission1.setState(SubmissionState.ACCEPTED);
        submission1.setSubmissionTime(LocalDateTime.now());

        Submission submission2 = new Submission();
        submission2.setId(EXAMPLE_SUBMISSION_ID_2);
        submission2.setAuthorId(EXAMPLE_USER_ID_2);
        submission2.setEditorId(EXAMPLE_USER_ID);
        submission2.setTitle(EXAMPLE_SUBMISSION_TITLE_2);
        submission2.setScientificForumId(1);
        submission2.setState(SubmissionState.SUBMITTED);
        submission2.setSubmissionTime(LocalDateTime.now());

        SubmissionRepository.add(submission1, transaction);
        SubmissionRepository.add(submission2, transaction);

        assertEquals(1, SubmissionRepository.countSubmissions(user, Privilege.AUTHOR, transaction,
                new ResultListParameters()));

        transaction.abort();
    }
    
}