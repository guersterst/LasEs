package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WeldJunit5Extension.class)
class SubmissionServiceTestNoMock {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    private static Paper paper;

    private static FileDTO fileDTO;

    @BeforeAll
    static void initPaper() {
        paper = new Paper();
        paper.setVisible(false);
        paper.setUploadTime(LocalDateTime.now());
        fileDTO = new FileDTO();
        fileDTO.setFile(new byte[]{});
    }

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class clazz = SubmissionServiceTestNoMock.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

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
    void testAddBasic() throws SQLException {
        Submission submission = new Submission();
        submission.setScientificForumId(1);
        submission.setAuthorId(4);
        submission.setEditorId(1);
        submission.setTitle("Sebastian testet die add Methode!");
        submission.setState(SubmissionState.ACCEPTED);
        submission.setSubmissionTime(LocalDateTime.now());

        SubmissionService submissionService = new SubmissionService();

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
        conn.commit();

        submissionService.add(submission, new ArrayList<>(), paper, fileDTO);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }

        assertEquals(1, j - i);
        submissionService.remove(submission);
        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddWithExistentCoAuthors() throws SQLException {
        Transaction transaction = new Transaction();

        try {
            Submission submission = new Submission();
            submission.setScientificForumId(1);
            submission.setAuthorId(4);
            submission.setEditorId(1);
            submission.setTitle("Sebastian testet die add Methode!");
            submission.setState(SubmissionState.ACCEPTED);
            submission.setSubmissionTime(LocalDateTime.now());

            User user = new User();
            // TODO hier koennte man den user adden, dann ist es nicht vom zustand der Datenbank abhaengig
            user.setEmailAddress("michael@gmail.com");

            SubmissionService submissionService = new SubmissionService();

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
            conn.commit();

            submissionService.add(submission, List.of(user), paper, fileDTO);

            ResultSet resultSet2 = stmt.executeQuery();
            int j = 0;
            while (resultSet2.next()) {
                j++;
            }

            assertEquals(1, j - i);
            submissionService.remove(submission);
        } catch (Exception e) {
            throw e;
        } finally {
            transaction.abort();
        }
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddWithNonExistentCoAuthors() throws SQLException {
        Transaction transaction = new Transaction();

        try {
            Submission submission = new Submission();
            submission.setScientificForumId(1);
            submission.setAuthorId(4);
            submission.setEditorId(1);
            submission.setTitle("Sebastian testet die add Methode!");
            submission.setState(SubmissionState.ACCEPTED);
            submission.setSubmissionTime(LocalDateTime.now());

            User user = new User();
            user.setEmailAddress("denUserGibtsNed@gmail.com");
            user.setFirstName("Ignaz");
            user.setLastName("Hanslmaier");

            SubmissionService submissionService = new SubmissionService();

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
            conn.commit();

            submissionService.add(submission, List.of(user), paper, fileDTO);

            ResultSet resultSet2 = stmt.executeQuery();
            int j = 0;
            while (resultSet2.next()) {
                j++;
            }

            assertEquals(1, j - i);
            submissionService.remove(submission);
        } finally {
            transaction.abort();
        }
    }

    @Test
    @Disabled
    void testAddReviewer() throws SQLException {
        User user = new User();
        user.setEmailAddress("schicho@fim.uni.passau.de");

        User reviewer = new User();
        reviewer.setId(420);

        List<User> reviewerList = new ArrayList<>();
        reviewerList.add(reviewer);

        ReviewedBy reviewedBy = new ReviewedBy();
        reviewedBy.setSubmissionId(5);
        reviewedBy.setReviewerId(user.getId());
        reviewedBy.setTimestampDeadline(LocalDateTime.now());
        reviewedBy.setHasAccepted(AcceptanceStatus.NO_DECISION);

        SubmissionService submissionService = new SubmissionService();

        Transaction transaction = new Transaction();
        Connection connection = transaction.getConnection();
        PreparedStatement statement = connection.prepareStatement("""
                SELECT * FROM reviewed_by
                """);

        ResultSet before = statement.executeQuery();
        int i = 0;
        while(before.next()){
            i++;
        }

        submissionService.manageReviewer(user, reviewedBy,reviewerList);

        ResultSet after= statement.executeQuery();
        int j = 0;
        while (after.next()) {
            j++;
        }

        assertEquals(i, j - 1);
        transaction.abort();
    }

}