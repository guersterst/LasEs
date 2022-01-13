package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WeldJunit5Extension.class)
@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ConnectionPool.class, ConfigReader.class, ConfigReader.class)
            .activate(RequestScoped.class, SessionScoped.class).build();

    /*
     * Unfortunately we have to do this before every single test, since @BeforeAll methods are static and static
     * methods don't work with our weld plugin.
     */
    @BeforeEach
    void startConnectionPool() {
        FileDTO file = new FileDTO();

        Class<TransactionTest> clazz = TransactionTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/config.properties");

        file.setInputStream(inputStream);

        weld.select(ConfigReader.class).get().setProperties(file);
        ConnectionPool.init();
    }

    @AfterEach
    void shutDownConnectionPool() {
        ConnectionPool.shutDown();
    }

    /**
     * @author Sebastian Vogt
     */
    @SuppressWarnings("unchecked")
    @Test
    void testCommitCommitsOnConnection() throws NoSuchFieldException,
            IllegalAccessException, SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        Field usedConnections = ConnectionPool.class.getDeclaredField("usedConnections");
        usedConnections.setAccessible(true);
        List<Connection> connectionList = (List<Connection>) usedConnections.get(ConnectionPool.getInstance());
        connectionList.add(mockConnection);

        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        ConnectionPool.getInstance().releaseConnection((Connection) connectionField.get(transaction));
        connectionField.set(transaction, mockConnection);

        transaction.commit();
        Mockito.verify(mockConnection).commit();
    }

    /**
     * @author Sebastian Vogt
     */
    @SuppressWarnings("unchecked")
    @Test
    void testAbortAbortsOnConnection() throws NoSuchFieldException,
            IllegalAccessException, SQLException {
        Transaction transaction = new Transaction();
        Connection mockConnection = Mockito.mock(Connection.class);

        Field usedConnections = ConnectionPool.class.getDeclaredField("usedConnections");
        usedConnections.setAccessible(true);
        List<Connection> connectionList = (List<Connection>) usedConnections.get(ConnectionPool.getInstance());
        connectionList.add(mockConnection);

        Field connectionField
                = Transaction.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        ConnectionPool.getInstance().releaseConnection((Connection) connectionField.get(transaction));
        connectionField.set(transaction, mockConnection);

        transaction.abort();
        Mockito.verify(mockConnection).rollback();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testCommitReturnsConnection() {
        int numberOfFreeConnectionAtBeginning =
                ConnectionPool.getInstance().getNumberOfFreeConnections();
        Transaction transaction = new Transaction();
        transaction.commit();
        assertEquals(numberOfFreeConnectionAtBeginning,
                ConnectionPool.getInstance().getNumberOfFreeConnections());
    }

    /**
     * @author Sebstian Vogt
     */
    @Test
    void testAbortReturnsConnection() {
        int numberOfFreeConnectionAtBeginning =
                ConnectionPool.getInstance().getNumberOfFreeConnections();
        Transaction transaction = new Transaction();
        transaction.abort();
        assertEquals(numberOfFreeConnectionAtBeginning,
                ConnectionPool.getInstance().getNumberOfFreeConnections());
    }

}