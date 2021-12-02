package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ErrorPageBackingTest {

    @Mock ConfigPropagator configPropagator;

    @Test
    void testErrorPageBacking() {
        ErrorPageBacking errorPageBacking = new ErrorPageBacking();
        ErrorMessage errorMessage = new ErrorMessage("test", "test");
        errorPageBacking.setErrorMessage(errorMessage);
        assertEquals(errorMessage, errorPageBacking.getErrorMessage());
    }

    @Test
    void testDevelopmentModeOn() {
        ErrorPageBacking errorPageBacking = new ErrorPageBacking();
        when(configPropagator.getProperty("DEBUG_AND_TEST_MODE")).thenReturn("true");
        assertTrue(errorPageBacking.isDevelopmentMode());
    }

    @Test
    void testDevelopmentModeOff() {
        ErrorPageBacking errorPageBacking = new ErrorPageBacking();
        when(configPropagator.getProperty("DEBUG")).thenReturn("false");
        assertFalse(errorPageBacking.isDevelopmentMode());

    }

}