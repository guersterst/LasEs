package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Paper;
import de.lases.global.transport.UIMessage;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.PaperRepository;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

/**
 * @author Johannes Garstenauer
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(WeldJunit5Extension.class)
public class ServicePaperTest {

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

        Class clazz = ServicePaperTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        // Reset the dto's value's after each test.
        paper = new Paper();
        paper.setSubmissionId(EXAMPLE_SUBMISSION_ID);
        paper.setVersionNumber(EXAMPLE_VERSION_NUMBER);
        fileDTO = new FileDTO();
        fileDTO.setFile(EXAMPLE_PDF);
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

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
    static void init() throws NoSuchFieldException, IllegalAccessException {

        // Mock repositories and initialize services.
        paperRepoMocked = mockStatic(PaperRepository.class);
        paperService = new PaperService();

        // Mock get to return a paper or file if the ids are correct.
        paperRepoMocked.when(() -> PaperRepository.get(eq(paper), any(Transaction.class))).thenReturn(paper);
        paperRepoMocked.when(() -> PaperRepository.getPDF(eq(paper), any(Transaction.class))).thenReturn(fileDTO);

        PropertyResourceBundle bundle = Mockito.mock(PropertyResourceBundle.class);
        Field bundleField = paperService.getClass().getDeclaredField("resourceBundle");
        bundleField.setAccessible(true);
        bundleField.set(paperService, bundle);

        Event<UIMessage> uiMessageEvent = Mockito.mock(Event.class);
        Field uiMessageField = paperService.getClass().getDeclaredField("uiMessageEvent");
        uiMessageField.setAccessible(true);
        uiMessageField.set(paperService, uiMessageEvent);
    }

    @AfterAll
    static void close() {

        // Close the mocks
        paperRepoMocked.close();
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
