package de.lases.persistence.repository;

import de.lases.business.service.SubmissionService;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.ReviewedBy;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.persistence.internal.ConfigReader;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WeldJunit5Extension.class)
public class ReviewedByREpositoryTest {

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

        Class<ScienceFieldRepositoryTest> clazz = ScienceFieldRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testRemoveReviewer() {
        SubmissionService submissionService = new SubmissionService();

        Submission submission = new Submission();
        submission.setId(666);

        User reviewer = new User();
        reviewer.setId(1);

        List<ReviewedBy> reviewedByList = submissionService.getList(submission);

        submissionService.removeReviewer(submission, reviewer);

        List<ReviewedBy> after = submissionService.getList(submission);

        assertEquals(reviewedByList.size(), after.size() + 1);
    }
}
