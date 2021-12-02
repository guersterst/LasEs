package de.lases.business.service;

import de.lases.business.util.Hashing;
import de.lases.global.transport.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

class RegistrationServiceTest {

    private static final String EXAMPLE_EMAIL_ADDRESS = "jane.doe@mail.eh";
    private static final String EXAMPLE_FIRST_NAME = "Jane";
    private static final String EXAMPLE_LAST_NAME = "Doe";
    private static final String EXAMPLE_PASSWORD = "secRet!3";
    private static final String EXAMPLE_HASH = "thisissuppposedtobeahash";

    private static MockedStatic<Hashing> mockedHashing;

    @BeforeAll
    static void mockHashing() {
        mockedHashing = mockStatic(Hashing.class);
    }

    @BeforeAll
    static void closeHashingMock() {
        mockedHashing.close();
    }

    @Test
    void testSelfRegister() {
        User user = new User();
        user.setEmailAddress(EXAMPLE_EMAIL_ADDRESS);
        user.setFirstName(EXAMPLE_FIRST_NAME);
        user.setLastName(EXAMPLE_LAST_NAME);
        user.setPasswordNotHashed(EXAMPLE_PASSWORD);

        mockedHashing.when(() -> Hashing.hashWithGivenSalt(eq(EXAMPLE_PASSWORD), anyString()))
                .thenReturn(EXAMPLE_HASH);

        RegistrationService registrationService = new RegistrationService();
        UserService userService = new UserService();

        registrationService.selfRegister(user);
        User registeredUser = userService.get(user);

        assertAll(
                () -> assertEquals(EXAMPLE_EMAIL_ADDRESS, registeredUser.getEmailAddress()),
                () -> assertEquals(EXAMPLE_HASH, registeredUser.getPasswordHashed())
        );
    }
}