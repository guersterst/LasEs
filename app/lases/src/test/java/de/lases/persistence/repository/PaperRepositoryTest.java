package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Paper;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PaperRepositoryTest {

    private static Paper paper;

    private static FileDTO pdf;

    private static Transaction transaction;

    @BeforeAll
    static void initPaper() {
        paper = new Paper();
        paper.setSubmissionId(5);
        paper.setUploadTime(LocalDateTime.now());
        paper.setVersionNumber(3);
        paper.setVisible(false);
        pdf = new FileDTO();
        pdf.setFile(new byte[]{1, 2, 3, 4});
    }

    @BeforeAll
    static void initConnectionPool() {
        ConnectionPool.init();
    }

    @BeforeAll
    static void startTransaction() {
        transaction = new Transaction();
    }

    @AfterAll
    static void rollbackTransaction() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

//    @AfterAll
//    static void shutdownConnectionPool() {
//        ConnectionPool.shutDown();
//    }

    @Test
    void testAdd() throws SQLException, DataNotWrittenException {
        Connection conn = transaction.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                """
                SELECT * FROM paper
                """);
        ResultSet resultSet = stmt.executeQuery();
        int i = 0;
        while (resultSet.next()) { i++; }

        PaperRepository.add(paper, pdf, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) { j++; }

        assertEquals(1, j - i);
    }

    @Test
    void testNull() {
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
    }

}