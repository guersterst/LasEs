package de.lases.persistence.repository;

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

    /*
     * Ich weiß halt nicht ob das gut ist, das testet ja internes verhalten.
     */
    @Test
    void testCommitCommits() throws NoSuchFieldException, IllegalAccessException,
            SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        // Inject our mock connection with reflection
        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(transaction, mockConnection);

        transaction.commit();
        Mockito.verify(mockConnection).commit();
    }

    @Test
    void testAbortPossible() {
        Transaction transaction = new Transaction();
        assertDoesNotThrow(transaction::abort);

    }

    @Test
    void testCommitPossible() {
        Transaction transaction = new Transaction();
        assertDoesNotThrow(transaction::commit);
    }

    @Test
    void cannotCommitAfterAbort() {
        Transaction transaction = new Transaction();
        transaction.abort();
        assertThrows(IllegalStateException.class, transaction::commit);
    }

    @Test
    void cannotCommitTwice() {
        Transaction transaction = new Transaction();
        transaction.commit();
        assertThrows(IllegalStateException.class, transaction::commit);
    }

    @Test
    void cannotAbortTwice() {
        Transaction transaction = new Transaction();
        transaction.abort();
        assertThrows(IllegalStateException.class, transaction::abort);
    }

    @Test
    void cannotGetConnectionAfterAbort() {
        Transaction transaction = new Transaction();
        transaction.abort();
        assertThrows(IllegalStateException.class, transaction::getConnection);
    }

    @Test
    void cannotGetConnectionAfterCommit() {
        Transaction transaction = new Transaction();
        transaction.commit();
        assertThrows(IllegalStateException.class, transaction::getConnection);
    }

}