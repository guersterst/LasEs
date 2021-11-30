package de.lases.business.service;

import de.lases.global.transport.Submission;
import de.lases.global.transport.User;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.repository.SubmissionRepository;
import de.lases.persistence.repository.Transaction;
import de.lases.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final String SECURE_EXAMPLE_PASSWORD = "Affe123!";
    private static final String VERY_RANDOM_SALT_BASE64 = "QWZmZW4gc2luZCBzdXBlcg==";
    // PBKDF2WithHmacSHA256, 128 bit, 75000 iterations. https://gist.github.com/schicho/616fb9529b624000a577344388ce3f3b
    private static final String SECURE_PASSWORD_HASH_BASE64 = "YXirI3Xsv1hhne/qI+um6Q==";

    @Test
    void testLoginSuccess() {
        User UserJustLogin = new User();
        UserJustLogin.setPasswordNotHashed(SECURE_EXAMPLE_PASSWORD);
        UserJustLogin.setId(1);

        User UserSuccessLogin = new User();
        UserSuccessLogin.setPasswordHashed(SECURE_PASSWORD_HASH_BASE64);
        UserSuccessLogin.setId(1);

        LoginService loginService = new LoginService();

        try(MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            mockedStatic.when(() -> UserRepository.get(UserJustLogin, null)).thenReturn(UserSuccessLogin);

            assertEquals(UserSuccessLogin,loginService.login(UserJustLogin));
        }
    }
}