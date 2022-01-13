package de.lases.business.service;

import de.lases.business.util.EmailUtil;
import de.lases.business.util.Hashing;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.UIMessage;
import de.lases.global.transport.User;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Thomas Kirz
 */
@ExtendWith(WeldJunit5Extension.class)
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private static final String EXAMPLE_EMAIL_ADDRESS = "jane.doe@mail.eh";
    private static final String EXAMPLE_FIRST_NAME = "Jane";
    private static final String EXAMPLE_LAST_NAME = "Doe";
    private static final String EXAMPLE_PASSWORD = "secRet!3";
    private static final String EXAMPLE_HASH = "thisissuppposedtobeahash";
    private static final String EXAMPLE_SALT = "Potassium boride";

    private static MockedStatic<Hashing> mockedHashing;
    private static MockedStatic<EmailUtil> mockedEmailUtil;
    private static MockedStatic<FacesContext> staticMockedFacesContext;

    private RegistrationService registrationService;
    private UserService userService;

    @Mock
    private FacesContext mockedFacesContext;

    @Mock
    private PropertyResourceBundle mockedResourceBundle;

    @Mock
    private Event<UIMessage> mockedEvent;

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(RegistrationServiceTest.class, ConnectionPool.class,
                    ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    @Produces
    PropertyResourceBundle propertyResourceBundleProducer() {
        return when(mock(PropertyResourceBundle.class).getString(any())).thenReturn("").getMock();
    }

    @BeforeAll
    static void mockStaticClasses() {
        mockedHashing = mockStatic(Hashing.class);
        mockedEmailUtil = mockStatic(EmailUtil.class);
        staticMockedFacesContext = mockStatic(FacesContext.class);

        ExternalContext externalContext = mock(ExternalContext.class);
        FacesContext facesContext = mock(FacesContext.class);

        staticMockedFacesContext.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
    }

    @AfterAll
    static void closeMocks() {
        mockedHashing.close();
        mockedEmailUtil.close();
        staticMockedFacesContext.close();
    }

    @BeforeEach
    void setServiceDependencies() throws Exception {
        registrationService = new RegistrationService();

        when(mockedFacesContext.getExternalContext()).thenReturn(mock(ExternalContext.class));
        Field facesContextField = registrationService.getClass().getDeclaredField("facesContext");
        facesContextField.setAccessible(true);
        facesContextField.set(registrationService, mockedFacesContext);

        Field bundleFieldReg = registrationService.getClass().getDeclaredField("message");
        bundleFieldReg.setAccessible(true);
        bundleFieldReg.set(registrationService, mockedResourceBundle);

        Field eventFieldReg = registrationService.getClass().getDeclaredField("uiMessageEvent");
        eventFieldReg.setAccessible(true);
        eventFieldReg.set(registrationService, mockedEvent);

        userService = new UserService();

        Field bundleFieldUser = userService.getClass().getDeclaredField("propertyResourceBundle");
        bundleFieldUser.setAccessible(true);
        bundleFieldUser.set(userService, mockedResourceBundle);

    }

    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class<RegistrationServiceTest> c = RegistrationServiceTest.class;
        InputStream inputStream = c.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testSelfRegister() throws Exception {
        User user = new User();
        user.setEmailAddress(EXAMPLE_EMAIL_ADDRESS);
        user.setFirstName(EXAMPLE_FIRST_NAME);
        user.setLastName(EXAMPLE_LAST_NAME);
        user.setPasswordNotHashed(EXAMPLE_PASSWORD);
        user.setRegistered(false);

        mockedHashing.when(Hashing::generateRandomSalt).thenReturn(EXAMPLE_SALT);
        mockedHashing.when(() -> Hashing.hashWithGivenSalt(eq(EXAMPLE_PASSWORD), anyString()))
                .thenReturn(EXAMPLE_HASH);

        registrationService.selfRegister(user);
        User registeredUser = userService.get(user);

        // remove user from database after test
        Transaction t = new Transaction();
        UserRepository.remove(user, t);
        t.commit();

        mockedEmailUtil.verify(() -> EmailUtil.sendEmail(any(String[].class), any(), any(), anyString()));
        assertAll(
                () -> assertEquals(EXAMPLE_EMAIL_ADDRESS, registeredUser.getEmailAddress()),
                () -> assertEquals(EXAMPLE_HASH, registeredUser.getPasswordHashed())
        );
    }
}