package de.lases.business.service;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.User;
import de.lases.global.transport.Verification;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;


@ExtendWith(MockitoExtension.class)
@ExtendWith(WeldJunit5Extension.class)
public class UserServiceTest {

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

        Class clazz = UserServiceTestNoMocks.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    private final UserService userService = new UserService();

    static MockedStatic<UserRepository> mockedRepo;

    @BeforeAll
    static void mockRepository() {
        mockedRepo = mockStatic(UserRepository.class);
    }

    @AfterAll
    static void closeMock() {
        mockedRepo.close();
    }

    @Test
    void testGetUser() {
        User onlyId = new User();
        onlyId.setId(1);
        User fullData = new User();
        fullData.setId(1);
        fullData.setFirstName("Fabi");
        fullData.setLastName("Dorfner");

        mockedRepo.when(() -> UserRepository.get(eq(onlyId), any())).thenReturn(fullData);

        assertEquals(fullData, userService.get(onlyId));
    }

    @Test
    void testChangeUserName() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setFirstName("Steffi");
        newUser.setLastName("Dorfner");

        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setFirstName("Stefanie");
        oldUser.setLastName("Gürster");

        mockedRepo.when(() -> UserRepository.get(eq(oldUser), any())).thenReturn(oldUser);

        assertEquals(oldUser, userService.get(oldUser));

        mockedRepo.when(() -> UserRepository.get(eq(oldUser), any())).thenReturn(newUser);

        userService.change(newUser, new User());

        assertEquals(newUser, userService.get(oldUser));
    }

    @Test
    void testChangeEmailVerified() {
        User user = new User();
        user.setId(1);
        user.setEmailAddress("stefanie@gmail.com");

        User changedEmail = new User();
        changedEmail.setId(1);
        changedEmail.setEmailAddress("st@gmail.com");

        Verification verification = new Verification();
        verification.setUserId(1);
        verification.setVerified(false);

        mockedRepo.when(() -> UserRepository.getVerification(eq(user), any())).thenReturn(verification);

        mockedRepo.when(() -> UserRepository.get(eq(user), any())).thenReturn(changedEmail);

        userService.change(changedEmail, new User());

        assertFalse(userService.getVerification(user).isVerified());
    }
}
