@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final String SECURE_EXAMPLE_PASSWORD = "Affe123!";
    private static final String VERY_RANDOM_SALT_BASE64 = "QWZmZW4gc2luZCBzdXBlcg==";
    // PBKDF2WithHmacSHA256, 128 bit, 75000 iterations.
    private static final String SECURE_PASSWORD_HASH_BASE64 = "YXirI3Xsv1hhne/qI+um6Q==";

    @Test
    void testLoginSuccess() {
        User UserJustLogin = new User();
        UserJustLogin.setId(1);
        UserJustLogin.setPasswordNotHashed(SECURE_EXAMPLE_PASSWORD);

        User UserInDBWithID1 = new User();
        UserInDBWithID1.setId(1);
        UserInDBWithID1.setPasswordHashed(SECURE_PASSWORD_HASH_BASE64);

        LoginService loginService = new LoginService();

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

        LoginService loginService = new LoginService();

        try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
            mockedStatic.when(() -> UserRepository.get(eq(UserJustLogin), any())).thenReturn(UserInDBWithID1);

            assertNull(loginService.login(UserJustLogin));
        }
    }
}
