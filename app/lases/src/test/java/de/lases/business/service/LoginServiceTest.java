package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.UIMessage;
import de.lases.global.transport.User;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.UserRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Johann Schicho
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(WeldJunit5Extension.class)
class LoginServiceTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    @Mock
    private PropertyResourceBundle bundle;

    @Mock
    private Event<UIMessage> uiMessageEvent;

    private LoginService loginService;

    private static final String SECURE_EXAMPLE_PASSWORD = "Affe123!";
    private static final String VERY_RANDOM_SALT_BASE64 = "QWZmZW4gc2luZCBzdXBlcg==";
    // PBKDF2WithHmacSHA256, 128 bit, 75000 iterations. https://gist.github.com/schicho/616fb9529b624000a577344388ce3f3b
    private static final String SECURE_PASSWORD_HASH_BASE64 = "YXirI3Xsv1hhne/qI+um6Q==";

    @BeforeEach
    void startConnectionPool() throws Exception {
        FileDTO file = new FileDTO();

        Class<LoginServiceTest> clazz = LoginServiceTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();

        // mock resource bundle
        lenient().when(bundle.getString(any())).thenReturn("");
        loginService = new LoginService();

        Field bundleField = loginService.getClass().getDeclaredField("resourceBundle");
        bundleField.setAccessible(true);
        bundleField.set(loginService, bundle);

        // mock ui message event
        Field uiMessageEventField = loginService.getClass().getDeclaredField("uiMessageEvent");
        uiMessageEventField.setAccessible(true);
        uiMessageEventField.set(loginService, uiMessageEvent);
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testLoginSuccess() {
        User UserJustLogin = new User();
        UserJustLogin.setId(1);
        UserJustLogin.setPasswordNotHashed(SECURE_EXAMPLE_PASSWORD);

        User UserInDBWithID1 = new User();
        UserInDBWithID1.setId(1);
        UserInDBWithID1.setPasswordHashed(SECURE_PASSWORD_HASH_BASE64);
        UserInDBWithID1.setPasswordSalt(VERY_RANDOM_SALT_BASE64);

        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            mockedStatic.when(() -> UserRepository.get(eq(UserJustLogin), any())).thenReturn(UserInDBWithID1);

            assertEquals(UserInDBWithID1, loginService.login(UserJustLogin));
        }
    }

    @Test
    void testLoginFail() {
        User UserJustLogin = new User();
        UserJustLogin.setId(1);
        UserJustLogin.setPasswordNotHashed("incorrectPassword");

        User UserInDBWithID1 = new User();
        UserInDBWithID1.setId(1);
        UserInDBWithID1.setPasswordHashed(SECURE_PASSWORD_HASH_BASE64);
        UserInDBWithID1.setPasswordSalt(VERY_RANDOM_SALT_BASE64);

        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            mockedStatic.when(() -> UserRepository.get(eq(UserJustLogin), any())).thenReturn(UserInDBWithID1);

            assertNull(loginService.login(UserJustLogin));
        }
    }
}