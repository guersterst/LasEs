package de.lases.business.service;

import de.lases.global.transport.Submission;
import de.lases.global.transport.SubmissionState;
import de.lases.global.transport.User;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionServiceTestNoMock {

    @BeforeAll
    static void initConnectionPool() {
        ConnectionPool.init();
    }

    @AfterAll
    static void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

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

        submissionService.add(submission, new ArrayList<>());

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }

        assertEquals(1, j - i);
        transaction.commit();
    }

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

            submissionService.add(submission, List.of(user));

            ResultSet resultSet2 = stmt.executeQuery();
            int j = 0;
            while (resultSet2.next()) {
                j++;
            }

            assertEquals(1, j - i);
        } catch(Exception e) {
            throw e;
        } finally {
            transaction.abort();
        }
    }

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

            submissionService.add(submission, List.of(user));

            ResultSet resultSet2 = stmt.executeQuery();
            int j = 0;
            while (resultSet2.next()) {
                j++;
            }

            assertEquals(1, j - i);
        } finally {
            transaction.abort();
        }
    }

}