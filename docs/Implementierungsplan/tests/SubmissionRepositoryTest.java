class SubmissionRepositoryTest {

    private static final int EXAMPLE_SUBMISSION_ID_1 = 1;
    private static final int EXAMPLE_SUBMISSION_ID_2 = 2;
    private static int EXAMPLE_USER_ID;
    private static final String EXAMPLE_SUBMISSION_TITLE_1 = "Submission title";
    private static final String EXAMPLE_SUBMISSION_TITLE_2 = "Different title";

    @BeforeAll
    void addUser() throws Exception {
        Transaction transaction = new Transaction();
        UserRepository.add(new User(), transaction);
        EXAMPLE_USER_ID = UserRepository.getList(
                transaction, new ResultListParameters()).get(0).getId();
        transaction.commit();
    }

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
        List<Submission> authoredList = SubmissionRepository
                .getList(user, Privilege.AUTHOR, transaction, null);
        List<Submission> editedList = SubmissionRepository
                .getList(user, Privilege.EDITOR, transaction, null);
        transaction.commit();

        assertAll(
                () -> assertEquals(1, authoredList.size()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE_1, authoredList.get(0).getTitle()),
                () -> assertEquals(1, editedList.size()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE_2, editedList.get(0).getTitle())
        );
    }

    @Test
    void testAddAndAbort() throws Exception {
        Transaction transaction = new Transaction();
        Submission submission = new Submission();
        submission.setId(EXAMPLE_SUBMISSION_ID_1);
        submission.setAuthorId(EXAMPLE_USER_ID);
        submission.setTitle(EXAMPLE_SUBMISSION_TITLE_1);
        SubmissionRepository.add(submission, transaction);
        transaction.abort();
        List<Submission> authoredList = SubmissionRepository.getList(
                new User(), Privilege.AUTHOR, transaction, null);
        assertEquals(0, authoredList.size());
    }

    // add two submissions and test countSubmissions()
    @Test
    void testCountSubmissions() throws Exception {
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

        Transaction transaction = new Transaction();

        SubmissionRepository.add(submission1, transaction);
        SubmissionRepository.add(submission2, transaction);

        assertEquals(1, SubmissionRepository.countSubmissions(user, transaction));

        transaction.commit();
    }

}