package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorPageBackingTest {

    @Mock ConfigPropagator configPropagator;

    private final ErrorPageBacking errorPageBacking = new ErrorPageBacking();

    @BeforeEach
    void setBackingDependencies() throws Exception {
        Field configPropagatorField = errorPageBacking.getClass().getDeclaredField("configPropagator");
        configPropagatorField.setAccessible(true);
        configPropagatorField.set(errorPageBacking, configPropagator);
    }

    @Test
    void testErrorPageBacking() {
        ErrorMessage errorMessage = new ErrorMessage("test", "test");
        errorPageBacking.setErrorMessage(errorMessage);
        assertEquals(errorMessage, errorPageBacking.getErrorMessage());
    }

    @Test
    void testDevelopmentModeOn() {
        when(configPropagator.getProperty("DEBUG_AND_TEST_MODE")).thenReturn("true");
        assertTrue(errorPageBacking.isDevelopmentMode());
    }

    @Test
    void testDevelopmentModeOff() {
        when(configPropagator.getProperty("DEBUG_AND_TEST_MODE")).thenReturn("false");
        assertFalse(errorPageBacking.isDevelopmentMode());

    }

}