@ExtendWith(MockitoExtension.class)
class SubmissionBackingTest {

    private static final int EXAMPLE_SUBMISSION_ID = 2;
    private static final int EXAMPLE_USER_ID = 6;

    @Mock SessionInformation sessionInformation;

    @Mock SubmissionService submissionService;

    @Mock UserService userService;

    @Mock PaperService paperService;

    @Mock ReviewService reviewService;

    @InjectMocks SubmissionBacking submissionBacking;

    @Test
    void testInitAndOnLoad() {
        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID);
        User user = new User();
        user.setId(EXAMPLE_USER_ID);

        when(sessionInformation.getUser()).thenReturn(user);
        when(submissionService.get(sub)).thenReturn(sub);

        SubmissionBacking submissionBacking = new SubmissionBacking();
        submissionBacking.init();
        submissionBacking.onLoad();

        verify(submissionService).get(sub);
        verify(userService).getList(sub, Privilege.AUTHOR);
        verify(paperService).getList(eq(sub), eq(user), any());
        verify(reviewService).getList(eq(sub), eq(user), any());
        verify(submissionService).getReviewedBy(eq(sub), eq(user));
        verify(paperService).getLatest(eq(sub), eq(user));
    }

    @Test
    void testInitAndOneLoadIllegal() {
        Submission sub = new Submission();
        sub.setId(EXAMPLE_SUBMISSION_ID);
        // User with no rights to view the submission
        User user = new User();
        user.setId(EXAMPLE_USER_ID + 1);

        when(sessionInformation.getUser()).thenReturn(user);
        when(submissionService.get(sub)).thenReturn(sub);

        submissionBacking.init();

        assertThrows(IllegalAccessException.class, () -> submissionBacking.onLoad());
    }
}