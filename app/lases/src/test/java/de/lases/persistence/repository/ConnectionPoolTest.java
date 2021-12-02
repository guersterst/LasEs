package de.lases.persistence.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.util.HostSpec;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConnectionPoolTest {

    @Test
    void testGetConnectionUninitialized() {
        ConnectionPool pool = ConnectionPool.getInstance();
        assertThrows(IllegalStateException.class, pool::getConnection);
    }

    @Test
    void testGetConnectionOpen() throws SQLException {
        ConnectionPool.init();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        assertFalse(conn.isClosed());
    }

    @Test
    void testReleaseForeignConnection() {
        ConnectionPool.init();
        ConnectionPool pool = ConnectionPool.getInstance();
        assertThrows(IllegalArgumentException.class, () -> {
                pool.releaseConnection(Mockito.mock(Connection.class));
        });
    }

    @Test
    void testShutdownDeInitializes() {
        ConnectionPool.init();
        ConnectionPool.shutDown();
        ConnectionPool pool = ConnectionPool.getInstance();
        assertThrows(IllegalStateException.class, pool::getConnection);
    }

}