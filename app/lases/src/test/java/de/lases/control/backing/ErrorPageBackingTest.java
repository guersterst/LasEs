
package de.lases.control.backing;

import de.lases.business.internal.ConfigPropagator;
import de.lases.global.transport.ErrorMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorPageBackingTest {

    private static final String TEST_ERROR_MESSAGE = "Test Error Message";
    private static final String TEST_STACKTRACE = "Test Stacktrace";

    @Mock ConfigPropagator configPropagator;

    @Mock
    FacesContext facesContext;

    @Mock
    ExternalContext externalContext;

    @Mock
    Map<String, Object> sessionMap;

    private final ErrorPageBacking errorPageBacking = new ErrorPageBacking();

    @BeforeEach
    void setBackingDependencies() throws Exception {
        Field configPropagatorField = errorPageBacking.getClass().getDeclaredField("configPropagator");
        configPropagatorField.setAccessible(true);
        configPropagatorField.set(errorPageBacking, configPropagator);
    }

    @Test
    void testErrorPageBacking() throws Exception{
        Field facesContextField = errorPageBacking.getClass().getDeclaredField("facesContext");
        facesContextField.setAccessible(true);
        facesContextField.set(errorPageBacking, facesContext);

        // Mock FacesContext and session map
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getSessionMap()).thenReturn(sessionMap);
        when(sessionMap.get("internal_error_message"))
                .thenReturn(new ErrorMessage(TEST_ERROR_MESSAGE, TEST_STACKTRACE));

        errorPageBacking.init();
        assertAll(
                () -> assertEquals(TEST_ERROR_MESSAGE, errorPageBacking.getErrorMessage().getMessage()),
                () -> assertEquals(TEST_STACKTRACE, errorPageBacking.getErrorMessage().getStackTrace())
        );
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