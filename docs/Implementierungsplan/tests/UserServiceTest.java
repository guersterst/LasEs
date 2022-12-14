@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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

        userService.change(newUser);

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

        userService.change(changedEmail);

        assertFalse(userService.getVerification(user).isVerified());
    }
}
