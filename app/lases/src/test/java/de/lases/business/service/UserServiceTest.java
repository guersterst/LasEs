package de.lases.business.service;

import de.lases.global.transport.User;
import de.lases.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

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
}
