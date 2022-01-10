package de.lases.control.validation;

import jakarta.enterprise.inject.Produces;
import jakarta.faces.validator.ValidatorException;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(WeldJunit5Extension.class)
class PasswordValidatorTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(PasswordValidatorTest.class).build();

    @Produces
    PropertyResourceBundle propertyResourceBundleProducer() {
        return Mockito.when(Mockito.mock(PropertyResourceBundle.class).getString(any())).thenReturn("").getMock();
    }

    @Test
    void testValidate() {
        PasswordValidator validator = new PasswordValidator();

        // test strings adhering to the password policy:
        // they contains uppercase and lowercase letters, numbers and special characters
        // and they are between 8 and 100 characters long
        String validPassword1 = "12345AaB'";
        String validPassword2 = "??345XöB;";
        String validPassword3 = validPassword2.repeat(4);
        assertAll(
                () -> assertDoesNotThrow(() -> validator.validate(null, null, validPassword1)),
                () -> assertDoesNotThrow(() -> validator.validate(null, null, validPassword2)),
                () -> assertDoesNotThrow(() -> validator.validate(null, null, validPassword3))
        );
    }

    @Test
    void testValidate_invalid() {
        PasswordValidator validator = new PasswordValidator();

        // test strings that do not adhere to the password policy:
        String validPassword1 = "1aA;'";
        String validPassword2 = "12345678";
        String validPassword3 = "Aa;" + "0".repeat(98);
        assertAll(
                () -> assertThrows(ValidatorException.class, () -> validator.validate(null, null, validPassword1)),
                () -> assertThrows(ValidatorException.class, () -> validator.validate(null, null, validPassword2)),
                () -> assertThrows(ValidatorException.class, () -> validator.validate(null, null, validPassword3))
        );
    }
}