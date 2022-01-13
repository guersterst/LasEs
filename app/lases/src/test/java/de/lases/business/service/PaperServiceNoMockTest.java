package de.lases.business.service;

import de.lases.business.util.EmailUtil;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Paper;
import de.lases.global.transport.UIMessage;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.Transaction;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Stefanie Gürster
 */
@ExtendWith(WeldJunit5Extension.class)
public class PaperServiceNoMockTest {
    private static Paper paper;

    private static FileDTO pdf;

    private static PaperService paperService;

    private PropertyResourceBundle resourceBundle;

    private Event<UIMessage> uiMessageEvent;

    private MockedStatic<EmailUtil> emailUtilMockedStatic;

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */

    @BeforeEach
    void startConnectionPool() throws IllegalAccessException, NoSuchFieldException {
        FileDTO file = new FileDTO();

        Class clazz = PaperServiceNoMockTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        paper = new Paper();
        paper.setSubmissionId(762);
        paper.setUploadTime(LocalDateTime.now());
        paper.setVersionNumber(1);
        paper.setVisible(false);

        pdf = new FileDTO();
        pdf.setFile(new byte[]{1, 2, 3, 4});

        emailUtilMockedStatic = Mockito.mockStatic(EmailUtil.class);

        resourceBundle = Mockito.mock(PropertyResourceBundle.class);
        paperService = new PaperService();
        Field bundleField = paperService.getClass().getDeclaredField("resourceBundle");
        bundleField.setAccessible(true);
        bundleField.set(paperService, resourceBundle);

        uiMessageEvent = Mockito.mock(Event.class);
        Field eventField = paperService.getClass().getDeclaredField("uiMessageEvent");
        eventField.setAccessible(true);
        eventField.set(paperService, uiMessageEvent);
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testGet() {
        paperService.add(pdf, paper);


        Paper gotten = paperService.get(paper);

        assertEquals(paper, gotten);

        // remove the paper again so the test will run through more than once
        paperService.remove(paper);
    }

    @Test
    void testChange() {
        paperService.add(pdf, paper);

        Paper changed = paper.clone();
        changed.setVisible(true);

        paperService.change(changed);

        assertEquals(changed, paper);

        // remove the paper again so the test will run through more than once
        paperService.remove(paper);
    }

//    @Test
//    void testRemove() {
//        paperService.add(pdf, paper);
//        paperService.remove(paper);
//        Paper find = paperService.get(paper);
//
//        assertNull(find);
//    }


//    @Test
//    void testGetPDF() {
//        FileDTO paperFile = new FileDTO();
//        paperFile.setFile(new byte[0]);
//        FileDTO fileDTO = paperService.getFile(paper);
//        int length = fileDTO.getFile().length;
//
//        assertEquals(paperFile.getFile().length, length);
//    }
}
