@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    // id of submission in mocked repository
    private static final int FIRST_SUBMISSION_ID = 0;
    private static final int EXAMPLE_REVIEW_VERSION = 3;
    private static final int EXAMPLE_USER_ID = 6;
    private static final String EXAMPLE_SUBMISSION_TITLE = "Submission title";

    static MockedStatic<SubmissionRepository> subRepo;
    static MockedStatic<ReviewRepository> reviewRepo;

    @BeforeAll
    static void mockRepositories() {
        Submission submissionFromRepo = new Submission();
        submissionFromRepo.setTitle(EXAMPLE_SUBMISSION_TITLE);
        submissionFromRepo.setAuthorId(EXAMPLE_USER_ID);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        subRepo = mockStatic(SubmissionRepository.class);
        subRepo.when(() -> SubmissionRepository.get(eq(submissionFromRepo), any(Transaction.class)))
                .thenReturn(submissionFromRepo);
        subRepo.when(() -> SubmissionRepository.getList(eq(user), Privilege.AUTHOR, any(Transaction.class), any()))
                .thenReturn(new Submission[]{submissionFromRepo});

        reviewRepo = mockStatic(ReviewRepository.class);
    }

    @AfterAll
    static void closeRepositoryMocks() {
        subRepo.close();
        reviewRepo.close();
    }

    @Test
    void testGet() {
        Submission sub = new Submission();
        sub.setId(FIRST_SUBMISSION_ID);

        SubmissionService submissionService = new SubmissionService();
        Submission gotten = submissionService.get(sub);

        assertAll(
                () -> assertEquals(FIRST_SUBMISSION_ID, gotten.getId()),
                () -> assertEquals(EXAMPLE_SUBMISSION_TITLE, gotten.getTitle())
        );
    }

    @Test
    void testReleaseReview() {
        Submission submission = new Submission();
        submission.setId(FIRST_SUBMISSION_ID);
        Review review = new Review();
        review.setPaperVersion(EXAMPLE_REVIEW_VERSION);
        review.setSubmissionId(FIRST_SUBMISSION_ID);
        SubmissionService submissionService = new SubmissionService();

        submissionService.releaseReview(review, submission);

        reviewRepo.verify(() -> ReviewRepository.change(eq(review), any(Transaction.class)), times(1));
    }

    @Test
    void testGetOwnSubmissions() {
        User user = new User();
        user.setId(EXAMPLE_USER_ID);
        Submission submission = new Submission();
        submission.setId(FIRST_SUBMISSION_ID);

        SubmissionService submissionService = new SubmissionService();
        List<Submission> result = submissionService.getList(Privilege.AUTHOR, user, null);
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(submission, result.get(0))
        );
    }

    @Test
    void testGetOwnSubmissionEmpty() {
        // different user to example user
        User user = new User();
        user.setId(EXAMPLE_USER_ID + 1);

        SubmissionService submissionService = new SubmissionService();
        List<Submission> result = submissionService.getList(Privilege.AUTHOR, user, null);
        assertEquals(0, result.size());
    }

}