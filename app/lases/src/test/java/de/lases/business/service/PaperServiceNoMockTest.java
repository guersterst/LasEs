package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Paper;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaperServiceNoMockTest {
    private static Paper paper;

    private static FileDTO pdf;

    private static PaperService paperService;

    @BeforeAll
    static void init() {
        paper = new Paper();
        paper.setSubmissionId(5);
        paper.setUploadTime(LocalDateTime.now());
        paper.setVersionNumber(2);
        paper.setVisible(false);

        pdf = new FileDTO();
        pdf.setFile(new byte[]{1, 2, 3, 4});

        paperService = new PaperService();
    }

    @BeforeAll
    static void initConnectionPool() {
        ConnectionPool.init();
    }

    @AfterAll
    static void rollbackTransaction() {
        ConnectionPool.shutDown();
    }

    @Test
    void testGet() {
        //paperService.add(pdf, paper);

        Paper gotten = paperService.get(paper);

        assertEquals(paper, gotten);
    }
}
