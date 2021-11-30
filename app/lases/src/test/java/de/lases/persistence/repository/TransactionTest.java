package de.lases.persistence.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @Test
    void testTest() throws NoSuchFieldException, IllegalAccessException,
            SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        // Inject out mock connection with reflection
        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        transaction.commit();
        Mockito.verify(mockConnection).commit();
    }

}