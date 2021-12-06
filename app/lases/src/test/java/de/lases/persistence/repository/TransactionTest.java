package de.lases.persistence.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @BeforeAll
    static void initConnectionPool() {
        ConnectionPool.init();
    }

    @AfterAll
    static void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    @Test
    void testCommitCommitsOnConnection() throws NoSuchFieldException,
            IllegalAccessException, SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        transaction.commit();
        Mockito.verify(mockConnection).commit();
    }

    @Test
    void testAbortAbortsOnConnection() throws NoSuchFieldException,
            IllegalAccessException, SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        transaction.abort();
        Mockito.verify(mockConnection).rollback();
    }

    @Test
    void testCommitReturnsConnection() {
        Transaction transaction = new Transaction();
        transaction.commit();
        assertEquals(ConnectionPool.INITIAL_POOL_SIZE,
                ConnectionPool.getInstance().getNumberOfFreeConnections());
    }

    @Test
    void testAbortReturnsConnection() {
        Transaction transaction = new Transaction();
        transaction.abort();
        assertEquals(ConnectionPool.INITIAL_POOL_SIZE,
                ConnectionPool.getInstance().getNumberOfFreeConnections());
    }

}