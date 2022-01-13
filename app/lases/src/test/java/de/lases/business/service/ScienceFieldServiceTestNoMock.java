package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.ScienceField;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(WeldJunit5Extension.class)
public class ScienceFieldServiceTestNoMock {
    private static final String NAME_TEST_SF = "Math";

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

        Class<ScienceFieldServiceTestNoMock> clazz = ScienceFieldServiceTestNoMock.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    /**
     * @author Stefanie Guerster
     */
    @Test
    void testRemoveScienceField() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        ScienceField mathField = new ScienceField();
        mathField.setName(NAME_TEST_SF);

        // remove it if it exists, do nothing if it doesn't exist
        scienceFieldService.remove(mathField);

        scienceFieldService.add(mathField);
        scienceFieldService.remove(mathField);

        assertFalse(scienceFieldService.exists(mathField));
    }

    /**
     * @author Stefanie Guerster
     */
    @Test
    void testAddScienceField() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        ScienceField mathField = new ScienceField();
        mathField.setName(NAME_TEST_SF);

        // remove it if it exists, do nothing if it doesn't exist
        scienceFieldService.remove(mathField);

        scienceFieldService.add(mathField);

        assertTrue(scienceFieldService.exists(mathField));

        scienceFieldService.remove(mathField);
    }

}
