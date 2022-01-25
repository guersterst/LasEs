package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Stefanie Gürster
 * @author Sebastian Vogt
 */
@ExtendWith(WeldJunit5Extension.class)
class PaperRepositoryTest {

    /**
     * A paper that is not supposed to be in the test db already.
     */
    private static Paper paper;

    /**
     * A paper that is supposed to be in the test db already.
     */
    private static Paper paperNonExistent;

    private static FileDTO pdf;

    private static Transaction transaction;

    private static User user1;

    private static Paper paper1;

    private static Paper paper2;

    private static Paper paper3;

    private static Submission submission1;

    private static Submission submission2;

    private  static Submission submission3;

    private static ScientificForum scientificForum1;

    private static User editor;

    private static ResultListParameters resultListParameters;

    @BeforeAll
    static void initPaper() {
        paper = new Paper();
        paper.setSubmissionId(5);
        paper.setUploadTime(LocalDateTime.now());
        paper.setVersionNumber(2);
        paper.setVisible(false);
        pdf = new FileDTO();
        pdf.setFile(new byte[]{1, 2, 3, 4});

        paperNonExistent = paper.clone();
        paperNonExistent.setSubmissionId(5);
        paperNonExistent.setVersionNumber(3);
    }

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() throws Exception {
        FileDTO file = new FileDTO();

        Class<PaperRepositoryTest> clazz = PaperRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        transaction = new Transaction();
        createData();
    }

    @AfterEach
    void shutDownConnectionPool() {
        transaction.abort();
        ConnectionPool.shutDown();
    }

    void createData() throws Exception {
        user1 = new User();
        user1.setFirstName("Hans");
        user1.setLastName("Mayer");
        user1.setEmailAddress("hans.mayer@example.com");
        user1.setAdmin(false);

        UserRepository.add(user1, transaction);
        user1 = UserRepository.get(user1, transaction);

        editor = new User();
        editor.setFirstName("Hans");
        editor.setLastName("Peter");
        editor.setEmailAddress("hans.peter@example.com");
        editor.setAdmin(false);

        UserRepository.add(editor, transaction);
        editor = UserRepository.get(editor, transaction);

        scientificForum1 = new ScientificForum();
        scientificForum1.setName("Forum1");
        scientificForum1.setDescription("great forum");
        scientificForum1.setReviewManual("just review");

        scientificForum1 = ScientificForumRepository.add(scientificForum1, transaction);
        ScientificForumRepository.addEditor(scientificForum1, editor, transaction);

        submission1 = new Submission();
        submission1.setAuthorId(user1.getId());
        submission1.setScientificForumId(scientificForum1.getId());
        submission1.setTitle("Test submission");
        submission1.setEditorId(editor.getId());
        submission1.setState(SubmissionState.SUBMITTED);
        submission1.setSubmissionTime(LocalDateTime.now());

        SubmissionRepository.add(submission1, transaction);
        submission1 = SubmissionRepository.get(submission1, transaction);

        paper1 = new Paper();
        paper1.setSubmissionId(submission1.getId());
        paper1.setUploadTime(LocalDateTime.now());

        submission2 = new Submission();
        submission2.setAuthorId(user1.getId());
        submission2.setScientificForumId(scientificForum1.getId());
        submission2.setTitle("Test submission");
        submission2.setEditorId(editor.getId());
        submission2.setState(SubmissionState.SUBMITTED);
        submission2.setSubmissionTime(LocalDateTime.now());

        SubmissionRepository.add(submission2, transaction);
        submission2 = SubmissionRepository.get(submission2, transaction);

        submission3 = new Submission();
        submission3.setAuthorId(user1.getId());
        submission3.setScientificForumId(scientificForum1.getId());
        submission3.setTitle("Test submission");
        submission3.setEditorId(editor.getId());
        submission3.setState(SubmissionState.SUBMITTED);
        submission3.setSubmissionTime(LocalDateTime.now());

        SubmissionRepository.add(submission3, transaction);
        submission3 = SubmissionRepository.get(submission3, transaction);

        Paper existingPaper = new Paper();
        existingPaper.setSubmissionId(submission2.getId());
        existingPaper.setUploadTime(LocalDateTime.now());
        existingPaper.setVersionNumber(1);

        PaperRepository.add(existingPaper, pdf, transaction);

        paper2 = PaperRepository.get(existingPaper, transaction);

        Paper addPaper = new Paper();
        addPaper.setSubmissionId(submission2.getId());
        addPaper.setUploadTime(LocalDateTime.now());
        addPaper.setVersionNumber(2);

        PaperRepository.add(addPaper, pdf, transaction);
        paper3 = PaperRepository.get(addPaper, transaction);

        resultListParameters = new ResultListParameters();
        resultListParameters.setVisibleFilter(Visibility.ALL);
        resultListParameters.setDateSelect(DateSelect.ALL);
    }

    /**
     * @author Stefanie Gürster
     */
    @Test
    void testGetAndAddPaper() throws DataNotWrittenException, NotFoundException, DataNotCompleteException {
        paper1.setVersionNumber(1);
        PaperRepository.add(paper1, pdf, transaction);

        Paper getPaper = PaperRepository.get(paper1, transaction);

        assertAll(
                () -> assertEquals(paper1.getSubmissionId(), getPaper.getSubmissionId()),
                () -> assertEquals(1, getPaper.getVersionNumber())
        );
    }

    @Test
    void testAddPaperNoSubmissionId() {
        Paper noId = new Paper();
        paper1.setUploadTime(LocalDateTime.now());

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.add(noId, pdf, transaction));
    }

    @Test
    void testAddPaperNoUploadTime() {
        Paper noTime = new Paper();
        noTime.setSubmissionId(submission1.getId());

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.add(noTime, pdf, transaction));
    }

    @Test
    void testAddPaperNoFile() {
        FileDTO fileDTO = new FileDTO();
        paper1.setVersionNumber(-3);

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.add(paper1, fileDTO, transaction));
    }

    @Test
    void testGetPaperNoVersionNumber() {
        Paper noVersion = new Paper();
        noVersion.setSubmissionId(submission1.getId());
        noVersion.setUploadTime(LocalDateTime.now());

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.get(paper1, transaction));
    }

    @Test
    void testAddPaperNoVersion() {
        Paper noVersion = new Paper();
        noVersion.setSubmissionId(submission1.getId());
        noVersion.setUploadTime(LocalDateTime.now());

        Transaction transaction = new Transaction();
        assertThrows(DatasourceQueryFailedException.class, () -> PaperRepository.add(noVersion, pdf, transaction));
    }

    @Test
    void testGetPaperNotFound() {
        Paper paper = paper2.clone();
        paper.setVersionNumber(-30000);

        assertThrows(NotFoundException.class, () -> PaperRepository.get(paper, transaction));
    }

    @Test
    void testChangePaper() throws DataNotWrittenException, NotFoundException {
        Paper paper = paper2.clone();
        paper.setVisible(true);
        PaperRepository.change(paper, transaction);
        Paper changedPaper = PaperRepository.get(paper, transaction);

        assertEquals(paper.isVisible(), changedPaper.isVisible());
    }

    @Test
    void testChangePaperWithoutChange() throws DataNotWrittenException, NotFoundException {
        PaperRepository.change(paper2, transaction);
        Paper gottenPaper = PaperRepository.get(paper2, transaction);

        assertEquals(paper2, gottenPaper);
    }

    @Test
    void testChangeNotFoundPaper() throws DataNotWrittenException, NotFoundException {
        Paper notFoundPaper = new Paper();
        notFoundPaper.setSubmissionId(-10000000);
        notFoundPaper.setVersionNumber(-1000);

        assertThrows(NotFoundException.class, () -> {
            PaperRepository.change(notFoundPaper, transaction);
        });
    }

    @Test
    void testChangePaperNoSubmissionId() {
        Paper paper = new Paper();
        paper.setVersionNumber(1);
        paper.setVisible(true);

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.change(paper, transaction));
    }

    @Test
    void testChangePaperNoVersion() {
        Paper paper = new Paper();
        paper.setSubmissionId(submission1.getId());
        paper.setVisible(true);

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.change(paper, transaction));
    }


    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAdd() throws SQLException, DataNotWrittenException {
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                """
                        SELECT count(*) FROM paper
                        """);
        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();
        int i = resultSet.getInt(1);

        PaperRepository.add(paperNonExistent, pdf, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        resultSet2.next();
        int j = resultSet2.getInt(1);

        assertEquals(1, j - i);
        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testNull() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        assertAll(
                () -> {
                    assertThrows(InvalidFieldsException.class,
                            () -> PaperRepository.add(paper, new FileDTO(),
                                    transaction1));
                },
                () -> {
                    assertThrows(InvalidFieldsException.class,
                            () -> PaperRepository.add(new Paper(), pdf,
                                    transaction2));
                }
        );
    }


    @Test
    void testRemovePaper() throws DataNotCompleteException, NotFoundException, DataNotWrittenException {
        int countPaperBefore = PaperRepository.countPaper(user1, submission2, transaction, resultListParameters);
        PaperRepository.remove(paper3, transaction);

        int countPaperAfter = PaperRepository.countPaper(user1, submission2, transaction, resultListParameters);

        assertEquals(countPaperAfter, countPaperBefore - 1);
    }

    @Test
    void testRemoveNotFound() {
        Paper paper = new Paper();
        paper.setSubmissionId(-900000);
        paper.setVersionNumber(-9000000);

        assertThrows(NotFoundException.class, () -> PaperRepository.remove(paper, transaction));
    }

    @Test
    void testRemovePaperNoSubmissionId() {
        Paper noId = new Paper();

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.remove(noId, transaction));
    }

    @Test
    void testRemovePaperNoVersion() {
        Paper noVersion = new Paper();
        noVersion.setSubmissionId(submission1.getId());

        Transaction transaction = new Transaction();
        assertThrows(InvalidFieldsException.class, () -> PaperRepository.remove(noVersion, transaction));
    }

    @Test
    void testCountPaper() throws DataNotCompleteException, NotFoundException {
        ResultListParameters parameters = resultListParameters;
        parameters.getFilterColumns().put("version", paper2.getVersionNumber().toString());

        int count = PaperRepository.countPaper(user1, submission2, transaction, parameters);

        assertEquals(1, count);
    }

    @Test
    void testCountPaperParamsNull() {
        Transaction transaction = new Transaction();

        assertThrows(InvalidQueryParamsException.class, () -> PaperRepository.countPaper(user1, submission2, transaction, null));

    }

    @Test
    void testCountPaperNotFound() {
        Submission submission = submission1.clone();
        submission.setId(-10000);

        assertThrows(NotFoundException.class, () -> PaperRepository.countPaper(user1, submission, transaction, resultListParameters));
    }

    @Test
    void testSizeOfFile() throws NotFoundException, DataNotCompleteException {
        FileDTO fileDTO = PaperRepository.getPDF(paper2, transaction);
        int fileLength = fileDTO.getFile().length;

        assertEquals(pdf.getFile().length, fileLength);
    }

    @Test
    void testGetNewestPaper() throws DataNotWrittenException, NotFoundException {
        Paper newestPaper = paper1.clone();
        newestPaper.setSubmissionId(submission3.getId());
        newestPaper.setVersionNumber(1);
        newestPaper.setVisible(true);

        PaperRepository.add(newestPaper, pdf, transaction);
        newestPaper = PaperRepository.get(newestPaper, transaction);

        assertEquals(newestPaper, PaperRepository.getNewestPaperForSubmission(submission3, transaction));
    }

    @Test
    void testGetListNotFound() throws DataNotCompleteException, NotFoundException, DataNotWrittenException {
        Submission submission = new Submission();
        submission.setId(-999);

        Paper paper = paper2.clone();
        paper.setSubmissionId(-999);
        PaperRepository.add(paper2, pdf, transaction);

        resultListParameters.setSortColumn("");


        assertThrows(NotFoundException.class, () -> PaperRepository.getList(submission, transaction, user1, resultListParameters));

    }

}