package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Paper;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.exception.NotFoundException;
import org.junit.jupiter.api.*;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.exception.InvalidFieldsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Stefanie Gürster, Sebastian Vogt
 */
class PaperRepositoryTest {

    /**
     * A paper that is not supposed to be in the test db already.
     */
    private static Paper paper;

    /**
     * A paper that is supposed to be in the test db already.
     */
    private static Paper paperNonExistent;

    private static FileDTO pdf;

    @BeforeAll
    static void initPaper() {
        paper = new Paper();
        paper.setSubmissionId(5);
        paper.setUploadTime(LocalDateTime.now());
        paper.setVersionNumber(2);
        paper.setVisible(false);
        pdf = new FileDTO();
        pdf.setFile(new byte[]{1, 2, 3, 4});

        paperNonExistent = paper.clone();
        paperNonExistent.setVersionNumber(3);
    }

    @BeforeAll
    static void initConnectionPool() {
        ConnectionPool.init();
    }

    @AfterAll
    static void shutdownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testGetPaper() throws SQLException, NotFoundException, DataNotWrittenException {
        Transaction transaction = new Transaction();

        Connection connection = transaction.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                """
                        SELECT * FROM paper
                        WHERE  submission_id = ? AND version = ?
                        """
        );
        statement.setInt(1, paper.getSubmissionId());
        statement.setInt(2, paper.getVersionNumber());

        ResultSet resultSet = statement.executeQuery();

        Paper resultPaper = new Paper();
        if (resultSet.next()) {
            resultPaper.setSubmissionId(resultSet.getInt("submission_id"));
            resultPaper.setVisible(resultSet.getBoolean("is_visible"));
            resultPaper.setVersionNumber(resultSet.getInt("version"));
            resultPaper.setUploadTime(resultSet.getTimestamp("timestamp_upload").toLocalDateTime());
        }

        assertEquals(resultPaper, PaperRepository.get(resultPaper, transaction));
        transaction.abort();
    }

    @Test
    void testChange() throws SQLException, DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Connection connection = transaction.getConnection();
        PaperRepository.add(paperNonExistent, pdf, transaction);

        Paper changed = paperNonExistent.clone();
        changed.setVisible(true);

        PreparedStatement statement = connection.prepareStatement(
                """
                        UPDATE paper
                        SET is_visible = ?
                        WHERE version = ? AND submission_id = ?
                        """
        );
        statement.setBoolean(1, changed.isVisible());
        statement.setInt(2, changed.getVersionNumber());
        statement.setInt(3, changed.getSubmissionId());

        PaperRepository.change(changed, transaction);

        assertEquals(changed, paperNonExistent);
        transaction.abort();
    }

    @Test
    void testAdd() throws SQLException, DataNotWrittenException {
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                """
                        SELECT * FROM paper
                        """);
        ResultSet resultSet = stmt.executeQuery();
        int i = 0;
        while (resultSet.next()) {
            i++;
        }

        PaperRepository.add(paperNonExistent, pdf, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }

        assertEquals(1, j - i);
        transaction.abort();
    }

    @Test
    void testNull() {
        Transaction transaction = new Transaction();
        assertAll(
                () -> {
                    assertThrows(InvalidFieldsException.class,
                            () -> PaperRepository.add(paper, new FileDTO(),
                                    transaction));
                },
                () -> {
                    assertThrows(InvalidFieldsException.class,
                            () -> PaperRepository.add(new Paper(), pdf,
                                    transaction));
                }
        );
        transaction.abort();
    }

    @Test
    void testRemove() throws SQLException, DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();

        PaperRepository.add(paperNonExistent, pdf, transaction);

        PreparedStatement stmt = conn.prepareStatement(
                """
                        SELECT * FROM paper
                        """);
        ResultSet resultSet = stmt.executeQuery();
        int i = 0;
        while (resultSet.next()) {
            i++;
        }

        PaperRepository.remove(paper, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }

        assertEquals(1, i - j);
        transaction.abort();
    }

    @Test
    void testFileSize() throws SQLException, NotFoundException, DataNotWrittenException {
        Transaction transaction = new Transaction();
        PaperRepository.add(paperNonExistent,pdf,transaction);
        FileDTO fileDTO = PaperRepository.getPDF(paperNonExistent,transaction);
        int fileLength = fileDTO.getFile().length;

        assertEquals(pdf.getFile().length, fileLength);
        transaction.abort();
    }

}