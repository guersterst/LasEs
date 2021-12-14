package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.ScienceField;
import de.lases.global.transport.User;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.ScienceFieldRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(WeldJunit5Extension.class)
public class ScienceFieldServiceTestNoMock {
    private static final String NAME_MATH_SF = "Math";

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

        Class clazz = ScienceFieldServiceTestNoMock.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testAddScienceField() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        ScienceField mathField = new ScienceField();
        mathField.setName(NAME_MATH_SF);
        scienceFieldService.add(mathField);

        //assertTrue(scienceFieldService.exists(mathField));
        //scienceFieldService.remove(mathField);
    }

    @Test
    void testRemoveScienceField() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        ScienceField mathField = new ScienceField();
        mathField.setName(NAME_MATH_SF);
        scienceFieldService.add(mathField);

        scienceFieldService.remove(mathField);

        assertFalse(scienceFieldService.exists(mathField));
    }
}
