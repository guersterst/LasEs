package de.lases.persistence.repository;

import de.lases.global.transport.FileDTO;
import de.lases.global.transport.ResultListParameters;
import de.lases.global.transport.ScienceField;
import de.lases.global.transport.ScientificForum;
import de.lases.persistence.exception.DataNotCompleteException;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.KeyExistsException;
import de.lases.persistence.exception.NotFoundException;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sebastian Vogt
 */
@ExtendWith(WeldJunit5Extension.class)
class ScienceFieldRepositoryTest {

    static ScienceField scienceField;

    @BeforeAll
    static void initDTOs() {
        scienceField = new ScienceField();

        // do not commit this science field to the database or the test will break
        scienceField.setName("NOT IN DB");
    }

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

        Class clazz = ScienceFieldRepositoryTest.class;
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
    @Test
    void testAdd() throws DataNotWrittenException, KeyExistsException, SQLException {
        Transaction transaction = new Transaction();

        Connection conn = transaction.getConnection();

        String query = """
                SELECT FROM science_field
                WHERE name = ?
                """;

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, scienceField.getName());
        ResultSet set = stmt.executeQuery();
        int i = 0;
        while (set.next()) {
            i++;
        }

        ScienceFieldRepository.add(scienceField, transaction);

        PreparedStatement stmt2 = conn.prepareStatement(query);
        stmt.setString(1, scienceField.getName());
        ResultSet set2 = stmt.executeQuery();
        int j = 0;
        while (set2.next()) {
            j++;
        }

        assertEquals(i + 1, j);

        set.close();
        set2.close();
        stmt.close();
        stmt2.close();

        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddKeyExistsException() throws DataNotWrittenException, KeyExistsException {
        Transaction transaction = new Transaction();

        // if it is in the database, remove it
        ScienceFieldRepository.remove(scienceField, transaction);

        ScienceFieldRepository.add(scienceField, transaction);

        assertThrows(KeyExistsException.class, () -> ScienceFieldRepository.add(scienceField, transaction));
        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testAddRemove() throws DataNotWrittenException, KeyExistsException, SQLException, NotFoundException {
        Transaction transaction = new Transaction();

        Connection conn = transaction.getConnection();

        String query = """
                SELECT FROM science_field
                WHERE name = ?
                """;

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, scienceField.getName());
        ResultSet set = stmt.executeQuery();
        int i = 0;
        while (set.next()) {
            i++;
        }

        ScienceFieldRepository.add(scienceField, transaction);
        ScienceFieldRepository.remove(scienceField, transaction);

        PreparedStatement stmt2 = conn.prepareStatement(query);
        stmt.setString(1, scienceField.getName());
        ResultSet set2 = stmt.executeQuery();
        int j = 0;
        while (set2.next()) {
            j++;
        }

        assertEquals(i, j);

        set.close();
        set2.close();
        stmt.close();
        stmt2.close();

        transaction.abort();
    }

    @Disabled
    @Test
    void testGetListForum() throws SQLException, DataNotCompleteException, NotFoundException, DataNotWrittenException,
            KeyExistsException {
        Transaction transaction = new Transaction();

        ScientificForum scientificForum = new ScientificForum();
        scientificForum.setId(2);

        List<ScienceField> scienceFieldList = ScienceFieldRepository.getList(scientificForum, transaction,
                new ResultListParameters());

        PreparedStatement stmt = transaction.getConnection().prepareStatement("""
                                                TRUNCATE science_field CASCADE
                                                """);
        stmt.executeUpdate();

        ScienceField s1 = new ScienceField();
        ScienceField s2 = new ScienceField();
        ScienceField s3 = new ScienceField();

        s1.setName("Biology");
        s2.setName("Computer Science");
        s3.setName("Philosophy");

        if (!ScienceFieldRepository.isScienceField(s1, transaction)) {
            ScienceFieldRepository.add(s1, transaction);
        }
        if (!ScienceFieldRepository.isScienceField(s2, transaction)) {
            ScienceFieldRepository.add(s2, transaction);
        }
        if (!ScienceFieldRepository.isScienceField(s3, transaction)) {
            ScienceFieldRepository.add(s3, transaction);
        }

        List<ScienceField> expected = List.of(s1, s2, s3);

        assertTrue(expected.containsAll(scienceFieldList) && scienceFieldList.containsAll(expected));

        transaction.abort();
    }

    @Disabled
    @Test
    void testGetListForumFilter() throws SQLException, DataNotCompleteException, NotFoundException {
        Transaction transaction = new Transaction();

        ScientificForum scientificForum = new ScientificForum();
        scientificForum.setId(2);

        ResultListParameters params = new ResultListParameters();
        params.setGlobalSearchWord("computer");

        List<ScienceField> scienceFieldList = ScienceFieldRepository.getList(scientificForum, transaction, params);


        ScienceField s2 = new ScienceField();

        s2.setName("Computer Science");

        List<ScienceField> expected = List.of(s2);

        assertTrue(expected.containsAll(scienceFieldList) && scienceFieldList.containsAll(expected));

        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testExists() throws DataNotWrittenException, KeyExistsException {
        Transaction transaction = new Transaction();

        ScienceFieldRepository.add(scienceField, transaction);

        assertTrue(ScienceFieldRepository.isScienceField(scienceField, transaction));

        transaction.abort();
    }

    /**
     * @author Sebastian Vogt
     */
    @Test
    void testNotExists() throws DataNotWrittenException, KeyExistsException {
        Transaction transaction = new Transaction();

        ScienceFieldRepository.remove(scienceField, transaction);

        assertFalse(ScienceFieldRepository.isScienceField(scienceField, transaction));

        transaction.abort();
    }



}