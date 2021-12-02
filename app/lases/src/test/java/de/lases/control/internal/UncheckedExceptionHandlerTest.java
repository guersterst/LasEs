package de.lases.control.internal;

import com.sun.faces.context.FacesContextImpl;
import de.lases.global.transport.ErrorMessage;
import de.lases.persistence.exception.DatasourceQueryFailedException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEventContext;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UncheckedExceptionHandlerTest {

    @Mock
    FacesContext facesContext;

    @Mock

    ErrorMessage errorMessage;

    @BeforeAll
    static void init() {
    }

    void testHandle() {
        //DatasourceQueryFailedException

    }
}
