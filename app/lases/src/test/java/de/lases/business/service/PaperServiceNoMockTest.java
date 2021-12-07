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

/**
 * @author Stefanie Gürster
 */
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

    @Test
    void testChange() {
        //paperService.add(pdf, paper);

        Paper changed = paper.clone();
        changed.setVisible(true);

        paperService.change(changed);

        assertEquals(changed, paper);
    }
/*
    @Test
    void testRemove() {
        //paperService.add(pdf, paper);
        paperService.remove(paper);
        Paper find = paperService.get(paper);

        assertEquals(null, find);
    }
    
 */
}
