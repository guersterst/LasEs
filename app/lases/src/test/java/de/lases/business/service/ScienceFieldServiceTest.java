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
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(WeldJunit5Extension.class)
@ExtendWith(MockitoExtension.class)
public class ScienceFieldServiceTest {

    private static final String NAME_MATH_SF = "Math";

    static MockedStatic<ScienceFieldRepository> mockedStatic;

    @BeforeAll
    static void mockRepository() {
        mockedStatic = mockStatic(ScienceFieldRepository.class);
    }

    @AfterAll
    static void closeMock() {
        mockedStatic.close();
    }

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

        Class clazz = ScienceFieldServiceTest.class;
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
    void testGetScienceFieldsOfUser() {
        ScienceFieldService scienceFieldService = new ScienceFieldService();
        User user = new User();
        user.setId(1);

        ScienceField mathField = new ScienceField();
        mathField.setName("Math");
        ScienceField csField = new ScienceField();
        csField.setName("Computer Science");

        ResultListParameters params = new ResultListParameters();

        List<ScienceField> userScienceFields = Arrays.asList(mathField, csField);

        mockedStatic.when(() -> ScienceFieldRepository.getList(eq(user), any(), eq(params))).thenReturn(userScienceFields);

        assertEquals(userScienceFields, scienceFieldService.getList(user, params));
    }
}
