package de.lases.control.backing;

import de.lases.business.service.RegistrationService;
import de.lases.global.transport.User;
import de.lases.persistence.repository.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NewUserBackingTest {

    static NewUserBacking backing = new NewUserBacking();

    @Test
    void saveUserTest() throws NoSuchFieldException, IllegalAccessException {
        RegistrationService mockService
                = Mockito.mock(RegistrationService.class);
        User user = new User();
        Field serviceField
                = NewUserBacking.class.getDeclaredField("registrationService");
        serviceField.setAccessible(true);
        serviceField.set(backing, mockService);

        Field userField
                = NewUserBacking.class.getDeclaredField("newUser");
        userField.setAccessible(true);
        userField.set(backing, user);

        backing.saveUser();

        Mockito.verify(mockService).registerByAdmin(user);

    }

}