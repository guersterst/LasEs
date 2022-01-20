package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.exception.NotFoundException;
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

    private static Submission submission1;

    private static ScientificForum scientificForum1;

    private static User editor;

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

    void createData() throws Exception{
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

        scientificForum1 = ScientificForumRepository.add(scientificForum1,transaction);
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
    void testChange() throws SQLException, DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Connection connection = transaction.getConnection();
        PaperRepository.add(paperNonExistent, pdf, transaction);

        Paper changed = paperNonExistent.clone();
        changed.setVisible(true);

        PreparedStatement statement = connection.prepareStatement(
                """
                        UPDATE paper
                        SET is_visible = ?
                        WHERE version = ? AND submission_id = ?
                        """
        );
        statement.setBoolean(1, changed.isVisible());
        statement.setInt(2, changed.getVersionNumber());
        statement.setInt(3, changed.getSubmissionId());

        PaperRepository.change(changed, transaction);

        assertEquals(changed, paperNonExistent);
        transaction.abort();
    }

    @Test
    void testChangeNotFoundPaper() throws DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Paper notFoundPaper = new Paper();
        notFoundPaper.setSubmissionId(10000000);
        notFoundPaper.setVersionNumber(1000);

        assertThrows(NotFoundException.class, () -> {PaperRepository.change(notFoundPaper, transaction);});
        transaction.abort();
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
    void testRemove() throws SQLException, DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Connection conn = transaction.getConnection();

        PaperRepository.add(paperNonExistent, pdf, transaction);

        PreparedStatement stmt = conn.prepareStatement(
                """
                        SELECT * FROM paper
                        """);
        ResultSet resultSet = stmt.executeQuery();
        int i = 0;
        while (resultSet.next()) {
            i++;
        }

        PaperRepository.remove(paper, transaction);

        ResultSet resultSet2 = stmt.executeQuery();
        int j = 0;
        while (resultSet2.next()) {
            j++;
        }

        assertEquals(1, i - j);
        transaction.abort();
    }

    @Test
    void testRemoveNotFound() {
        Transaction transaction = new Transaction();
        Paper paper = new Paper();
        paper.setSubmissionId(900000);
        paper.setVersionNumber(9000000);

        assertThrows(NotFoundException.class, () -> PaperRepository.remove(paper, transaction));
        transaction.abort();
    }

    @Test
    void testFileSize() throws SQLException, NotFoundException, DataNotWrittenException {
        Transaction transaction = new Transaction();
        PaperRepository.add(paperNonExistent,pdf,transaction);
        FileDTO fileDTO = PaperRepository.getPDF(paperNonExistent,transaction);
        int fileLength = fileDTO.getFile().length;

        assertEquals(pdf.getFile().length, fileLength);
        transaction.abort();
    }

    @Test
    void testGetNewestPaper() throws DataNotWrittenException, NotFoundException {
        Transaction transaction = new Transaction();
        Submission submission = new Submission();
        submission.setId(5);
        submission.setAuthorId(1);

        User author = new User();
        author.setId(1);

        PaperRepository.add(paper,pdf,transaction);

        Paper newestPaper = new Paper();
        newestPaper.setSubmissionId(5);
        newestPaper.setUploadTime(LocalDateTime.of(2021,12,8,14,22));
        newestPaper.setVersionNumber(4);
        newestPaper.setVisible(false);
        pdf = new FileDTO();
        pdf.setFile(new byte[]{1, 2, 3, 4});

        PaperRepository.add(newestPaper,pdf,transaction);

        Paper getNewestPaper = PaperRepository.getNewestPaperForSubmission(submission,transaction);

        assertAll(
                () -> assertEquals(newestPaper.getVersionNumber(), newestPaper.getVersionNumber()),
                () -> assertEquals(newestPaper.getUploadTime(), getNewestPaper.getUploadTime())
        );
        transaction.abort();
    }

    @Test
    void testGetListNotFound() {
        Transaction transaction = new Transaction();
        Submission submission = new Submission();
        submission.setId(9990000);

        User user = new User();
        user.setId(99999999);

        ResultListParameters resultListParameters = new ResultListParameters();

        assertThrows(NotFoundException.class, () -> {PaperRepository.getList(submission, transaction,user,resultListParameters);});
        transaction.abort();
    }

}