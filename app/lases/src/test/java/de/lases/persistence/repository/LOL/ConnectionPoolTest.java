package de.lases.persistence.repository.LOL;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.internal.ConfigReader;
import de.lases.persistence.repository.ConnectionPool;
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
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

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

    Method initializer;

    Method shutDown;

    Method getConn;

    Method releaseConn;

    Method getInst;

    @BeforeEach
    void startConnectionPool() throws NoSuchMethodException {
        FileDTO file = new FileDTO();

        Class clazz = ConnectionPoolTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);

        Class<ConnectionPool> klass = ConnectionPool.class;
        initializer = klass.getDeclaredMethod("init");
        initializer.setAccessible(true);

        shutDown = klass.getDeclaredMethod("shutDown");
        shutDown.setAccessible(true);

        getConn = klass.getDeclaredMethod("getConnection");
        getConn.setAccessible(true);

        releaseConn = klass.getDeclaredMethod("releaseConnection", Connection.class);
        releaseConn.setAccessible(true);

        getInst = klass.getDeclaredMethod("getInstance");
        getInst.setAccessible(true);

    }

    @Test
    void testGetConnectionUninitialized() throws InvocationTargetException, IllegalAccessException {
        ConnectionPool pool = (ConnectionPool) getInst.invoke(null);

        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> getConn.invoke(pool));
        assertTrue(e.getCause() instanceof IllegalStateException);
    }

    @Test
    void testGetConnectionOpen() throws SQLException, InvocationTargetException, IllegalAccessException {
        initializer.invoke(null);
        ConnectionPool pool = (ConnectionPool) getInst.invoke(null);
        Connection conn = (Connection) getConn.invoke(pool);
        assertFalse(conn.isClosed());
    }

    @Test
    void testReleaseForeignConnection() throws InvocationTargetException, IllegalAccessException {
        initializer.invoke(null);
        ConnectionPool pool = (ConnectionPool) getInst.invoke(null);
        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> {
                releaseConn.invoke(pool, Mockito.mock(Connection.class));
        });
        assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void testShutdownDeInitializes() throws InvocationTargetException, IllegalAccessException {
        initializer.invoke(null);
        shutDown.invoke(null);
        ConnectionPool pool = (ConnectionPool) getInst.invoke(null);

        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> getConn.invoke(pool));
        assertTrue(e.getCause() instanceof IllegalStateException);
    }

}