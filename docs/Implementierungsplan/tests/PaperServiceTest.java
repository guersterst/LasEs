@ExtendWith(MockitoExtension.class)
public class PaperServiceTest {

    // The required repository.
    private static MockedStatic<PaperRepository> paperRepoMocked;

    // The required service.
    private static PaperService paperService;

    // The required DTOs and their values
    private static Paper paper;
    private static final Integer EXAMPLE_SUBMISSION_ID = 1;
    private static final Integer EXAMPLE_VERSION_NUMBER = 10;
    private static FileDTO fileDTO;
    private static final byte[] EXAMPLE_PDF = new byte[]{};


    @BeforeAll
    static void init() {

        // Mock repositories and initialize services.
        paperRepoMocked = mockStatic(PaperRepository.class);
        paperService = new PaperService();

        // Mock get to return a paper or file if the ids are correct.
        paperRepoMocked.when(() -> PaperRepository.get(eq(paper), any(Transaction.class))).thenReturn(paper);
        paperRepoMocked.when(() -> PaperRepository.getPDF(eq(paper), any(Transaction.class))).thenReturn(fileDTO);
    }

    @BeforeEach
    void resetDTOS() {

        // Reset the dto's value's after each test.
        paper = new Paper();
        paper.setSubmissionId(EXAMPLE_SUBMISSION_ID);
        paper.setVersionNumber(EXAMPLE_VERSION_NUMBER);
        fileDTO = new FileDTO();
        fileDTO.setFile(EXAMPLE_PDF);
    }

    @AfterAll
    static void close() {

        // Close the mocks
        paperRepoMocked.close();
    }

    @Test
    void testGet() {
        paperService.add(fileDTO, paper);

        Paper gotten = paperService.get(paper);
        assertAll(
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, gotten.getSubmissionId()),
                () -> assertEquals(EXAMPLE_VERSION_NUMBER, gotten.getVersionNumber())
        );
    }

    @Test
    void testAdd() {
        paperService.add(fileDTO, paper);

        Paper gotten = paperService.get(paper);
        FileDTO gottenFile = paperService.getFile(paper);
        assertAll(
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, gotten.getSubmissionId()),
                () -> assertEquals(EXAMPLE_VERSION_NUMBER, gotten.getVersionNumber()),
                () -> assertEquals(EXAMPLE_PDF, gottenFile.getFile())
        );
    }

    @Test
    void testChange() {

        // A paper containing an old visibility.
        paper.setVisible(true);
        paperService.add(fileDTO, paper);

        // Create a paper with a new visibility but the same ids.
        Paper newPaper = new Paper();
        newPaper.setSubmissionId(EXAMPLE_SUBMISSION_ID);
        newPaper.setVersionNumber(EXAMPLE_VERSION_NUMBER);
        newPaper.setVisible(false);

        // Change the paper to contain the new comment.
        paperService.change(newPaper);

        // Mock the repository request to return the new paper.
        paperRepoMocked.when(() -> PaperRepository
                .get(eq(paper), any(Transaction.class))).thenReturn(newPaper);

        // Request the changed paper.
        Paper result = paperService.get(paper);
        assertAll(
                () -> assertEquals(EXAMPLE_VERSION_NUMBER, result.getVersionNumber()),
                () -> assertEquals(EXAMPLE_SUBMISSION_ID, result.getSubmissionId()),
                () -> assertFalse(result.isVisible())
        );
    }
}
