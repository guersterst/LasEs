package de.lases.persistence.repository;

import de.lases.global.transport.Privilege;
import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.persistence.exception.DataNotWrittenException;
import org.junit.jupiter.api.Test;

import java.util.List;

class SubmissionRepositoryTest {

    private static final int EXAMPLE_SUBMISSION_ID_1 = 1;
    private static final int EXAMPLE_SUBMISSION_ID_2 = 2;
    private static final int EXAMPLE_REVIEW_VERSION = 3;
    private static final int EXAMPLE_USER_ID = 6;
    private static final String EXAMPLE_SUBMISSION_TITLE_1 = "Submission title";
    private static final String EXAMPLE_SUBMISSION_TITLE_2 = "Different title";

    @Test
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

        transaction.commit();

        transaction = new Transaction();
        List<Submission> authoredList = SubmissionRepository.getList(user, Privilege.AUTHOR, transaction, null)
    }
}