package de.lases.business.service;

import de.lases.global.transport.*;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Johann Schicho
 */
@ExtendWith(WeldJunit5Extension.class)
class ReviewServiceNoMockTest {

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

        Class reviewService = ReviewServiceNoMockTest.class;
        InputStream inputStream = reviewService.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    private Submission addTestSubmission() {
        Submission submission = new Submission();
        submission.setScientificForumId(1);
        submission.setAuthorId(420);
        submission.setEditorId(1);
        submission.setTitle("Bastis Test Einreichung");
        submission.setState(SubmissionState.ACCEPTED);
        submission.setSubmissionTime(LocalDateTime.now());

        SubmissionService submissionService = new SubmissionService();
        return submissionService.add(submission, new ArrayList<>());
    }

    private void deleteTestSubmission(Submission submission) {
        SubmissionService submissionService = new SubmissionService();
        submissionService.remove(submission);
    }

    private void addTestPaper(Submission submission) {
        Paper paper = new Paper();
        paper.setVisible(true);
        paper.setSubmissionId(submission.getId());
        paper.setUploadTime(LocalDateTime.now());

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFile(new byte[]{1,2,3,4,5,6,7,8,9});

        PaperService paperService = new PaperService();
        paperService.add(fileDTO, paper);
    }


    @Test
    void testAddGetReviewAdmin() {
        User user = new User();
        user.setId(420);
        user.setPrivileges(List.of(Privilege.ADMIN));

        Submission submission = addTestSubmission();
        addTestPaper(submission);

        Review review = new Review();
        review.setSubmissionId(submission.getId());
        review.setVisible(false);
        review.setPaperVersion(1);
        review.setReviewerId(702);
        review.setComment("Wichtiger Comment");

        ReviewService reviewService = new ReviewService();
        reviewService.add(review, new FileDTO());

        List<Review> reviewList = reviewService.getList(submission, user, new ResultListParameters());

        assertEquals(1, reviewList.size());

        deleteTestSubmission(submission);
    }

    @Test
    void testAddGetReviewAuthor() {
        User user = new User();
        user.setId(420);
        user.setPrivileges(List.of(Privilege.AUTHOR));

        Submission submission = addTestSubmission();
        addTestPaper(submission);

        Review review = new Review();
        review.setSubmissionId(submission.getId());
        review.setVisible(false);
        review.setPaperVersion(1);
        review.setReviewerId(702);
        review.setComment("Wichtiger Comment");

        ReviewService reviewService = new ReviewService();
        reviewService.add(review, new FileDTO());

        List<Review> reviewList = reviewService.getList(submission, user, new ResultListParameters());

        assertEquals(0, reviewList.size());

        deleteTestSubmission(submission);
    }

}