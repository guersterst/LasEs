package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.util.HostSpec;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Note: These tests do not work if executed in parallel because of the singleton nature of the conneciton pool.
 *
 * @author Sebastian Vogt
 */
@ExtendWith(WeldJunit5Extension.class)
@ExtendWith(MockitoExtension.class)
class ConnectionPoolTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    @BeforeEach
    void startConnectionPool() throws NoSuchMethodException {
        FileDTO file = new FileDTO();

        Class clazz = ScienceFieldRepositoryTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);

        Class<ConnectionPool> klass = ConnectionPool.class;
        Method initializer = klass.getDeclaredMethod("init");
        initializer.setAccessible(true);

        Method getConn = klass.getDeclaredMethod("getConnection");
        getConn.setAccessible(true);

        Method releaseConn = klass.getDeclaredMethod("releaseConnection", Connection.class);
        releaseConn.setAccessible(true);

    }

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